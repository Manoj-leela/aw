package sg.activewealth.roboadvisor.infra.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import sg.activewealth.roboadvisor.infra.dto.UserSessionDto;
import sg.activewealth.roboadvisor.infra.exception.SystemException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class WebUtils {
	
	private Logger logger = Logger.getLogger(WebUtils.class);
		
	private static WebUtils me;

	public static final String SHOW_IMAGE_URL = "/attachment";
	
	public static final String LOGIN_URL = "login";

	public static WebUtils getInstance() {
		if (me == null) me = new WebUtils();

		return me;
	}

	public String getRequestPage(HttpServletRequest request) {
		return getRequestPage(request, true);
	}
	
	public String getRequestPage(HttpServletRequest request, Boolean withRequestString) {
		StringBuffer requestPg = new StringBuffer(request.getContextPath() + request.getRequestURI().substring(request.getContextPath().length()));
		if (withRequestString && request.getQueryString() != null) requestPg.append("?" + request.getQueryString());

		return requestPg.toString();
	}

	public String getFullUrl(HttpServletRequest request, String forwardUrl) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + forwardUrl;
	}
	
	public String getEncodedValue(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new SystemException(e);
		}
	}

	public String getDecodedValue(String value) {
		try {
			return URLDecoder.decode(value, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new SystemException(e);
		}
	}
	
	//http://stackoverflow.com/questions/607176/java-equivalent-to-javascripts-encodeuricomponent-that-produces-identical-outpu
	public String encodeURIComponent(String s) {
		String result;

		try {
			result = URLEncoder.encode(s, "UTF-8")
					.replaceAll("\\+", "%20")
					.replaceAll("\\%21", "!")
					.replaceAll("\\%27", "'")
					.replaceAll("\\%28", "(")
					.replaceAll("\\%29", ")")
					.replaceAll("\\%7E", "~");
		}
		catch (UnsupportedEncodingException e) {
			result = s;
		}

		return result;
	}

	public boolean isMobile(HttpServletRequest request) {
		if (request.getHeader("User-Agent") == null || request.getHeader("User-Agent").trim().length() == 0)
			return false;
		//Extracted from http://detectmobilebrowsers.com/ on 16 Sep '12
		String ua=request.getHeader("User-Agent").toLowerCase();
		if(ua.matches("(?i).*(android.+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|meego.+mobile|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")||
				ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")||
				ua.contains("ipad")) {
			return true;
		}
		return false;
	}
	
	public Map<String, String> getQueryMap(String query)	{  
		String[] params = query.split("&");  
		Map<String, String> map = new HashMap<String, String>();  
		for (String param : params) {  
			String name = param.split("=")[0];  
			String value = param.split("=")[1];  
			map.put(name, value);  
		}  
		return map;	
	}

	public String addEmbeddedPageParameterIfNecessary(String url, HttpServletRequest request) {
		if (!ValidationUtils.getInstance().isEmptyString(request.getParameter("isEmbeddedPage")) && request.getParameter("isEmbeddedPage").equals("true")) {
			if (url.contains("?")) url += "&";
			else url += "?";
			
			url += "isEmbeddedPage=true";
		}
		return url;
	}
	
	public String addParameter(String url, String parameter) {
		if (url == null || parameter == null) return url;
		
		if (url.contains("?")) url += "&";
		else url += "?";
		url += parameter;
		return url;
	}

	public StringBuffer buildLog(StringBuffer accessLog, HttpServletRequest request, String url, UserSessionDto userSession, boolean fullLog, boolean newLine) {
		String delim = (newLine) ? "\n" : ", ";
		String paramprefixdelim = (newLine) ? "\n\t" : " ";		
		
		// prehandle timing
		Long preHandleTiming = (Long) request.getAttribute(PREHANDLE_TIMING_ATTRIBUTE_NAME);
		long processTime = (preHandleTiming != null) ? System.currentTimeMillis() - preHandleTiming : 0;
		if (processTime > 0) accessLog.append("process: " + processTime + "ms" + delim);
		
		//ip
		accessLog.append("ip: " + request.getRemoteAddr() + delim);

		//usersession
		if (userSession != null && userSession.getUserId() != null && userSession.getUser() != null) {
			accessLog.append("user: " + userSession.getUser().getEmail() + ":" + userSession.getUserId() + delim);
		}		

		//url
		accessLog.append("url: " + url + " (" + request.getMethod() + ")" + delim);

		//user agent
		String useragent = request.getHeader("user-agent");
		if (useragent != null) {
			accessLog.append("user-agent: " + useragent + delim);
		}
		
		//params
		//if (fullLog) {
			List<String> sortedRequestKeys = new ArrayList<String>();
			if (request.getMethod() != null && !request.getMethod().toLowerCase().equals("get")) //add params only if not get. get have their params in url already!
				sortedRequestKeys.addAll(request.getParameterMap().keySet());
			Collections.sort(sortedRequestKeys, new Comparator<String>() {
				@Override
				public int compare(String arg0, String arg1) {
					return arg0.compareTo(arg1);
				}
			});
			if (!sortedRequestKeys.isEmpty()) accessLog.append("parameters:");
			for (Iterator<String> itr = sortedRequestKeys.iterator(); itr.hasNext(); ) {
				String key = itr.next();
				String[] value = request.getParameterMap().get(key);
				accessLog.append(paramprefixdelim + "(" + key + ": " + StringUtils.getInstance().buildStringFromStringArray(value, ",") + ")");
			}
			if (!sortedRequestKeys.isEmpty()) accessLog.append(delim);
			
			String json = (String) request.getAttribute(JSON_ATTRIBUTE_NAME);
			if (json != null)
				accessLog.append("json:" + json + delim);
		//}		
		
		return accessLog;
	}

	private static final String JSON_ATTRIBUTE_NAME = "json.attr";

	private static final String PREHANDLE_TIMING_ATTRIBUTE_NAME = "prehandletiming.attr";
	
	//this is called right after POST/PUT from CRUDController
	public void setJsonRequestAttribute(String json, HttpServletRequest request) {
		request.setAttribute(JSON_ATTRIBUTE_NAME, json);
	}

	//this is called from SecurityInterceptor
	public void setPrehandleTiming(HttpServletRequest request) {
		request.setAttribute(PREHANDLE_TIMING_ATTRIBUTE_NAME, System.currentTimeMillis());		
	}

	public void logAccess(HttpServletRequest request, UserSessionDto userSession, boolean fullLog) {
		String url = WebUtils.getInstance().getRequestPage(request);
		if (!url.contains(SHOW_IMAGE_URL)) {
			logger.info(buildLog(new StringBuffer(), request, url, userSession, fullLog, false).toString());
		}
	}
	
	public void logRestTemplateErrors() {
		
	}
	
	public void generateReport(String templatePath, Map<String, Object> data,
			Writer out) {

		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(this.getClass(), "/");

		try {
			Template template = cfg.getTemplate(templatePath);

			template.process(data, out);
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch (TemplateException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}
}