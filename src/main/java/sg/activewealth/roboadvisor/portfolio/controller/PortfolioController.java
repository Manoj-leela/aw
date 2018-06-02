package sg.activewealth.roboadvisor.portfolio.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.dto.FormOptionDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.portfolio.enums.InstrumentType;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioAssignmentCategory;
import sg.activewealth.roboadvisor.portfolio.enums.UserRiskProfile;
import sg.activewealth.roboadvisor.portfolio.model.AssetClassAllocation;
import sg.activewealth.roboadvisor.portfolio.model.Instrument;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.model.PortfolioInstrument;
import sg.activewealth.roboadvisor.portfolio.service.InstrumentService;
import sg.activewealth.roboadvisor.portfolio.service.PortfolioService;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;
import sg.activewealth.roboadvisor.trade.service.UserTradeService;

@Controller
@RequestMapping("/admin/portfolio")
public class PortfolioController extends CRUDController<Portfolio, PortfolioService> {

	@Autowired
	public void setService(PortfolioService service) {
		this.service = service;
	}

	@Autowired
	public PortfolioController(PortfolioService service) {
		super(Portfolio.class, service);
	}

	public PortfolioController(Class<Portfolio> portfolio, PortfolioService service) {
		super(portfolio, service);
	}

	@Autowired
	private InstrumentService instrumentService;

	@Autowired
	private UserPortfolioService userPortfolioService;

	@Autowired
	private UserTradeService userTradeService;

	@Override
	public Object[] preCreateUpdateGet(Portfolio model, HttpServletRequest request) {
		List<Instrument> instruments = instrumentService.retrieve("name", null).getResults();

		List<FormOptionDto> userRiskProfiles = new ArrayList<FormOptionDto>();
		for (UserRiskProfile riskProfile : UserRiskProfile.values()) {
			userRiskProfiles.add(new FormOptionDto(riskProfile.name(), riskProfile.getKey()));
		}
		if (model.getPortfolioInstruments().size() > 0) {
			for (PortfolioInstrument portfolioInstrument : model.getPortfolioInstruments()) {
				portfolioInstrument.setInUse(userTradeService.isPortfolioInstrumentInUse(portfolioInstrument.getId()));
			}
		}
		List<FormOptionDto> portfolioCategoryList = new ArrayList<FormOptionDto>();
		for (PortfolioAssignmentCategory portfolioCategory : PortfolioAssignmentCategory.values()) {
			portfolioCategoryList
					.add(new FormOptionDto(portfolioCategory.name(), portfolioCategory.getLabel()));
		}
		
		Boolean isPortfolioInUse = false;
		if(model.getId() != null){
			isPortfolioInUse = userPortfolioService.isPortfolioInUse(model.getId());
		}
		
		List<FormOptionDto> instrumentTypeList = new ArrayList<FormOptionDto>();
		for (InstrumentType instrumentType : InstrumentType.values()) {
			instrumentTypeList.add(new FormOptionDto(instrumentType.name(), instrumentType.getLabel()));
		}
		return new Object[] { "userProfileTypes", userRiskProfiles, "instruments", instruments,
				"portfolioCategoryList", portfolioCategoryList,"isPortfolioInUse",isPortfolioInUse,"instrumentTypeList",instrumentTypeList };
	}

	@Override
	public Portfolio preCreateUpdatePost(Portfolio model, HttpServletRequest request) {
		model = super.preCreateUpdatePost(model, request);

		if (model.getPortfolioInstruments() != null) {
			Iterator<PortfolioInstrument> portFolioInstrumentIterator = model.getPortfolioInstruments().iterator();
			while (portFolioInstrumentIterator.hasNext()) {
				PortfolioInstrument portfolioInstrument = portFolioInstrumentIterator.next();
				if (portfolioInstrument.getWeightage() == null
						|| portfolioInstrument.getWeightage() == new BigDecimal(0)) {
					portFolioInstrumentIterator.remove();
					continue;
				}
				portfolioInstrument.setPortfolio(model);
			}
		}
		if (model.getAssetClassAllocations() != null) {
			Iterator<AssetClassAllocation> assetClassAllocationIterator = model.getAssetClassAllocations().iterator();
			while (assetClassAllocationIterator.hasNext()) {
				AssetClassAllocation assetClassAllocation = assetClassAllocationIterator.next();
				if (assetClassAllocation.getTotalWeightage() == null
						|| assetClassAllocation.getTotalWeightage() == new BigDecimal(0)) {
					assetClassAllocationIterator.remove();
					continue;
				}
				assetClassAllocation.setPortfolio(model);
			}
		}
		return model;
	}
	
	@Override
	public Object list(PagingDto<Portfolio> pagingDto, String ids, HttpServletRequest request) {
		String queryName = request.getParameter("name");
		String[] queryUserRiskProfile = request.getParameterValues("userRiskProfile");
		pagingDto = service.retrieveForListPage(pagingDto,queryName,queryUserRiskProfile);
		List<FormOptionDto> userRiskProfileList = new ArrayList<FormOptionDto>();
		for (UserRiskProfile riskProfile : UserRiskProfile.values()) {
			userRiskProfileList.add(new FormOptionDto(riskProfile.getKey(), riskProfile.getKey()));
		}
		return modelAndView(getFullJspPath("list"), "list", pagingDto,"userRiskProfileList", userRiskProfileList);
	}

	@RequestMapping("/generateReport")
	public void downloadReport(HttpServletResponse response) throws WriteException, IOException {
		service.exportAsExcel(((PortfolioService)service).new Report() {

			@Override
			protected void writeRow(WritableSheet sheet, Portfolio item, int row) throws WriteException {
				sheet.addCell(new Label(0, row, item.getId()));
				sheet.addCell(new Label(1, row, item.getCreatedBy()));
				sheet.addCell(new Label(2, row, item.getUpdatedBy() !=null ? item.getUpdatedBy() : " "));
				sheet.addCell(new Label(3, row, String.valueOf(item.getCreatedOn())));
				sheet.addCell(new Label(4, row, String.valueOf(item.getUpdatedOn() !=null ? item.getUpdatedOn() : " ")));
				
				sheet.addCell(new Label(6, row, item.getName()));
				sheet.addCell(new Label(7, row, item.getDescription() !=null ? item.getDescription() : " "));
				sheet.addCell(new Label(8, row, String.valueOf(item.getRiskProfile().getValue())));
				sheet.addCell(new Label(9, row, String.valueOf(item.getProjectedReturns() !=null ? item.getProjectedReturns() : " ")));
				sheet.addCell(new Label(10, row, String.valueOf(item.getAssignmentCategory().getValue())));
				sheet.addCell(new Label(11, row, String.valueOf(item.getStatus().getValue())));
				
			}

			@Override
			protected void writeMoreHeadings(WritableSheet sheet, int row) throws WriteException {
				return;
			}

			@Override
			protected void writeHeadings(WritableSheet sheet, int row) throws WriteException {
				sheet.addCell(new Label(0, row, "ID"));
				sheet.addCell(new Label(1, row, "Created By"));
				sheet.addCell(new Label(2, row, "Updated By"));
				sheet.addCell(new Label(3, row, "Created On"));
				sheet.addCell(new Label(4, row, "Updated On"));
				sheet.addCell(new Label(5, row, "Deleted"));
				
				sheet.addCell(new Label(6, row, "Name"));
				sheet.addCell(new Label(7, row, "Description"));
				sheet.addCell(new Label(8, row, "User Risk Profile"));
				sheet.addCell(new Label(9, row, "Projected Returns"));
				sheet.addCell(new Label(10, row, "Assignment Category"));
				sheet.addCell(new Label(11, row, "Portfolio Status"));
			}

			@Override
			protected String getSheetName() {
				return "Portfolio";
			}

			@Override
			protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {
				return super.getWorkbook(response);
			}

		}, null, null, response);
	}
}
