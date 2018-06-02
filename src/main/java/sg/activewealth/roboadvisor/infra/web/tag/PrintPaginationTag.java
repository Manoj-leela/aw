package sg.activewealth.roboadvisor.infra.web.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.SystemException;
import sg.activewealth.roboadvisor.infra.utils.WebUtils;

@SuppressWarnings("serial")
public class PrintPaginationTag extends BaseTag {

	private Logger logger = Logger.getLogger(PrintPaginationTag.class);
	
	private static final Integer MULTIPAGE_CURRENTPAGE_DEFAULT = 0;

	private static final Integer MULTIPAGE_TOTALPAGES_DEFAULT = 5;

	private static final Integer MULTIPAGE_NO_TO_PRINT_MAXPAGE = 10;

	private static final String LABEL_MULTIPAGE_PREV = "Prev";
	
	private static final String LABEL_MULTIPAGE_NEXT = "Next";

	private String urlPageVar;

	private String pagingDtoPageVar;
	
	private String printMode;
	
	private String ulClass;
	
	private String liClass;

	private String PRINT_MODE_BOTH = "both";
	
	private String PRINT_MODE_SUMMARY = "summary";
	
	private String PRINT_MODE_PAGES = "pages";
	
	private String summaryTag;
	
	private String pagesUlTag;
	
	private String pagesLiTag;

	private static final String DEFAULT_SUMMARY_TAG = "span";

	private static final String DEFAULT_PAGES_UL_TAG = "ul";

	private static final String DEFAULT_PAGES_LI_TAG = "li";
	
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	@SuppressWarnings("rawtypes")
	public int doEndTag() throws JspException {
		try {
			StringBuffer output = new StringBuffer("");

			HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
			HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();

			// parse parameters
			String url = (String) this.pageContext.findAttribute(getUrlPageVar());
			if (url == null) url = getUrlPageVar();
			
			url = this.cleanUrl(request, url);
			
			PagingDto pagingDto = (PagingDto) this.pageContext.findAttribute(getPagingDtoPageVar());
			if (pagingDto != null) {
				int resultsPerPage = pagingDto.getResultsPerPage();
				int currentPage = pagingDto.getCurrentPage();

				Integer collectionTotalSize = pagingDto.getResultsSize(), collectionSize = pagingDto.getResults().size();
				
				int totalPages = MULTIPAGE_TOTALPAGES_DEFAULT;
				if (collectionTotalSize > resultsPerPage) {
					totalPages = collectionTotalSize / resultsPerPage;
					if (collectionTotalSize % resultsPerPage == 0) 
						totalPages--;
				}
				else
					totalPages = 0;

				//output.append("<div class=\"pagination\">");
				if (getPrintMode() == null || getPrintMode().toLowerCase().contains(PRINT_MODE_BOTH)) {
					output = this.buildSummaryLinkLabel(totalPages, output, request, response, url, currentPage, resultsPerPage, collectionSize, collectionTotalSize);
					output = this.buildAllPagesLinkLabel(totalPages, output, request, response, url, currentPage, resultsPerPage, collectionSize);
				}
				else if (getPrintMode().toLowerCase().contains(PRINT_MODE_SUMMARY))
					output = this.buildSummaryLinkLabel(totalPages, output, request, response, url, currentPage, resultsPerPage, collectionSize, collectionTotalSize);
				else if (getPrintMode().toLowerCase().contains(PRINT_MODE_PAGES))
					output = this.buildAllPagesLinkLabel(totalPages, output, request, response, url, currentPage, resultsPerPage, collectionSize);
				
				//output.append("</div>");
				this.pageContext.getOut().write(output.toString());
			}

		}
		catch (IOException e) {
			throw new SystemException(e);
		}

		return super.doEndTag();
	}

	private String cleanUrl(HttpServletRequest request, String url) {
		//remove currentPage & resultsPerPage from url
		String[] paramsToRemove = new String[]{"currentPage", "resultsPerPage", "sortBy"};
		for (int i = 0; i < paramsToRemove.length; i++) {
			String param = paramsToRemove[i];
			if (url.contains(param)) {
				String paramValue = WebUtils.getInstance().getEncodedValue(request.getParameter(param)).replace("+", "%20");
				url = url.replace("&" + param + "=" + paramValue, "");
				url = url.replace("?" + param + "=" + paramValue, "");
				url = url.replace(param + "=" + paramValue, "");
			}	
		}
		if (url.indexOf("?") == url.length()-1) url = url.substring(0, url.length()-1);

		return url;
	}
	
	private StringBuffer buildSummaryLinkLabel(int totalPages, StringBuffer output, HttpServletRequest request, HttpServletResponse response, String url, 
			int currentPage, int resultsPerPage, int collectionSize, int collectionTotalSize) {
		output.append("<" + (getSummaryTag() == null?DEFAULT_SUMMARY_TAG:getSummaryTag()) + " class=\"_pagination_total\">");
			output.append("<" + (getSummaryTag() == null?DEFAULT_SUMMARY_TAG:getSummaryTag()) + " class=\"_current_page_start\">");
			output.append((resultsPerPage * currentPage) + 1);
			output.append("</" + (getSummaryTag() == null?DEFAULT_SUMMARY_TAG:getSummaryTag()) + ">");
			output.append(" - ");
			output.append("<" + (getSummaryTag() == null?DEFAULT_SUMMARY_TAG:getSummaryTag()) + " class=\"_current_page_end\">");
			if (((resultsPerPage * (currentPage + 1))) > collectionTotalSize) {
				output.append(collectionTotalSize);
			}
			else {
				output.append((resultsPerPage * currentPage) + (resultsPerPage - 1) + 1);
			}
			output.append("</" + (getSummaryTag() == null?DEFAULT_SUMMARY_TAG:getSummaryTag()) + ">");
			output.append(" of <" + (getSummaryTag() == null?DEFAULT_SUMMARY_TAG:getSummaryTag()) + " class=\"_total_records\">" + collectionTotalSize + " records(s)</" + (getSummaryTag() == null?DEFAULT_SUMMARY_TAG:getSummaryTag()) + ">");
		output.append("</" + (getSummaryTag() == null?DEFAULT_SUMMARY_TAG:getSummaryTag()) + ">");
				
		return output;
	}
	
	private StringBuffer buildAllPagesLinkLabel(int totalPages, StringBuffer output, HttpServletRequest request, HttpServletResponse response, String url, 
			int currentPage, int resultsPerPage, int collectionSize) {
		if (totalPages >= 0) {
			output.append("<" + (getPagesUlTag() == null?DEFAULT_PAGES_UL_TAG:getPagesUlTag()) + " " + (getUlClass() == null?"":"class=\"" + getUlClass() + "\"") + ">");
			
			String encodedUrl = response.encodeURL(url);
			// if current page is not first page
			if (currentPage != MULTIPAGE_CURRENTPAGE_DEFAULT) {
				output.append(this.buildLinkLabel(request, encodedUrl, LABEL_MULTIPAGE_PREV, currentPage - 1, resultsPerPage, totalPages, collectionSize));
			}
			else {
				output.append(this.buildLinkLabel(LABEL_MULTIPAGE_PREV, false));
			}
			
			//cap within maxpages
			int cappedBuffer = MULTIPAGE_NO_TO_PRINT_MAXPAGE / 2;
			int startPage = MULTIPAGE_CURRENTPAGE_DEFAULT, endPage = totalPages + 1;
			if (endPage > MULTIPAGE_NO_TO_PRINT_MAXPAGE) {
				if (currentPage - cappedBuffer >= 0) startPage = currentPage - cappedBuffer;
				endPage = startPage + MULTIPAGE_NO_TO_PRINT_MAXPAGE;
				if (endPage > totalPages) {
					endPage = totalPages + 1;
					if (endPage - MULTIPAGE_NO_TO_PRINT_MAXPAGE > 0) startPage = endPage - MULTIPAGE_NO_TO_PRINT_MAXPAGE;
				}
			}
			
			if (startPage != 0) output.append(this.buildLinkLabel(" .. ", false));				
			for (int i = startPage; i < endPage; i++) {
				if (i != currentPage)
					output.append(this.buildLinkLabel(request, encodedUrl, new Integer(i + 1).toString(), i, resultsPerPage, totalPages, collectionSize));
				else
					output.append(this.buildLinkLabel(new Integer(i + 1).toString(), true));
			}				
			if (endPage != totalPages+1) output.append(this.buildLinkLabel(" .. ", false));
			
			// if current page is not last page
			if (currentPage != totalPages) {
				output.append(this.buildLinkLabel(request, encodedUrl, LABEL_MULTIPAGE_NEXT, currentPage + 1, resultsPerPage, totalPages, collectionSize));
			}
			else {
				output.append(this.buildLinkLabel(LABEL_MULTIPAGE_NEXT, false));
			}
			
			output.append("</" + (getPagesUlTag() == null?DEFAULT_PAGES_UL_TAG:getPagesUlTag()) + ">");
		}
		
		return output;
	}
	
	private String buildLinkLabel(HttpServletRequest request, String url, String label, int currentPage, int resultsPerPage, int totalPages, int collectionSize) {
		String sortBy = request.getParameter("sortBy");
		String sortType = request.getParameter("sortType");
		String sortOrder = request.getParameter("sortOrder");

		StringBuffer link = new StringBuffer(url + (url.contains("?") ? "&" : "?") + "currentPage=" + currentPage + "&resultsPerPage=" + resultsPerPage);
		if (sortBy != null) link.append("&sortBy=" + sortBy);
		if (sortType != null) link.append("&sortType=" + sortType);
		if (sortOrder != null) link.append("&sortOrder=" + sortOrder);

		String ret = "<" + (getPagesLiTag() == null?DEFAULT_PAGES_LI_TAG:getPagesLiTag()) + " " + (getLiClass() == null?"":"class=\"" + getLiClass() + "\"") + "><a href=\"" + link.toString() + "\">" + label + "</a></" + (getPagesLiTag() == null?DEFAULT_PAGES_LI_TAG:getPagesLiTag()) + ">";

		return ret;
	}

	private String buildLinkLabel(String label, boolean active) {
		return "<" + (getPagesLiTag() == null?DEFAULT_PAGES_LI_TAG:getPagesLiTag()) + " class=\"" + (active?"active":"disabled") + "\"><a href=\"#\">" + label + "</a></" + (getPagesLiTag() == null?DEFAULT_PAGES_LI_TAG:getPagesLiTag()) + ">";
	}

	public String getUrlPageVar() {
		return urlPageVar;
	}

	public void setUrlPageVar(String urlPageVar) {
		this.urlPageVar = urlPageVar;
	}

	public String getPagingDtoPageVar() {
		return pagingDtoPageVar;
	}

	public void setPagingDtoPageVar(String pagingDtoPageVar) {
		this.pagingDtoPageVar = pagingDtoPageVar;
	}

	public String getPrintMode() {
		return printMode;
	}

	public void setPrintMode(String printMode) {
		this.printMode = printMode;
	}

	public String getUlClass() {
		return ulClass;
	}

	public void setUlClass(String ulClass) {
		this.ulClass = ulClass;
	}

	public String getLiClass() {
		return liClass;
	}

	public void setLiClass(String liClass) {
		this.liClass = liClass;
	}

	public String getSummaryTag() {
		return summaryTag;
	}

	public void setSummaryTag(String summaryTag) {
		this.summaryTag = summaryTag;
	}

	public String getPagesUlTag() {
		return pagesUlTag;
	}

	public void setPagesUlTag(String pagesUlTag) {
		this.pagesUlTag = pagesUlTag;
	}

	public String getPagesLiTag() {
		return pagesLiTag;
	}

	public void setPagesLiTag(String pagesLiTag) {
		this.pagesLiTag = pagesLiTag;
	}

}