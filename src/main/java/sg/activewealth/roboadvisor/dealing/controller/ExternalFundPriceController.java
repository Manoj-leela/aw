package sg.activewealth.roboadvisor.dealing.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import sg.activewealth.roboadvisor.dealing.model.ExternalFund;
import sg.activewealth.roboadvisor.dealing.model.ExternalFundPrice;
import sg.activewealth.roboadvisor.dealing.model.ExternalFundSubscription;
import sg.activewealth.roboadvisor.dealing.service.ExternalFundPriceService;
import sg.activewealth.roboadvisor.dealing.service.ExternalFundService;
import sg.activewealth.roboadvisor.dealing.service.ExternalFundSubscriptionService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Controller
@RequestMapping("/admin/externalFundPrice")
public class ExternalFundPriceController extends CRUDController<ExternalFundPrice, ExternalFundPriceService> {

	@Autowired
	public void setService(ExternalFundPriceService service) {
		this.service = service;
	}

	@Autowired
	public ExternalFundPriceController(ExternalFundPriceService service) {
		super(ExternalFundPrice.class, service);
	}

	@Autowired
	private ExternalFundService externalFundService;

	@Autowired
	private ExternalFundSubscriptionService externalFundSubscriptionService;

	@Override
	public Object[] preCreateUpdateGet(ExternalFundPrice model, HttpServletRequest request) {
		PagingDto<ExternalFund> externalFunds = externalFundService.retrieve("createdOn", null);
		ExternalFundSubscription externalFundSubscription = externalFundSubscriptionService
				.isFundPriceUsed(model.getId());
		Boolean isFundPriceUsed = false;
		if (externalFundSubscription != null) {
			isFundPriceUsed = true;
		}
		return new Object[] { "externalFundList", externalFunds.getResults(), "isFundPriceUsed", isFundPriceUsed };
	}

	@Override
	public ExternalFundPrice preCreateUpdatePost(ExternalFundPrice model, HttpServletRequest request) {
		model = super.preCreateUpdatePost(model, request);
		String externalFundId = model.getExternalFund().getId();
		ExternalFund fund = externalFundService.retrieve(externalFundId);
		model.setExternalFund(fund);
		return model;
	}

	@Override
	public Object list(PagingDto<ExternalFundPrice> pagingDto, String ids, HttpServletRequest request) {
		String queryName = request.getParameter("name");
		String queryPriceDate = request.getParameter("priceDate");
		LocalDateTime priceDate = null;

		if(!ValidationUtils.getInstance().isEmptyString(queryPriceDate)){
			try{
				priceDate = LocalDateTime.parse(queryPriceDate);
			} catch (DateTimeParseException e) {
				logger.error("Date parsing error occurred:"+e.getMessage());
			}
		}
		String[] queryDealings = request.getParameterValues("dealing");
		List<Boolean> queryDealingList = new ArrayList<>();
		if(queryDealings != null) {
			for (String querydealing : queryDealings) {
				if(querydealing.equalsIgnoreCase("Yes")){
					queryDealingList.add(true);
				}
				if(querydealing.equalsIgnoreCase("No")){
					queryDealingList.add(false);
				}
			}
		}
		
		pagingDto = service.retrieveForListPage(pagingDto,queryName,priceDate,queryDealingList);
		PagingDto<ExternalFund> externalFunds = externalFundService.retrieve("createdOn", null);
		return modelAndView(getFullJspPath("list"), "list", pagingDto, "externalFunds", externalFunds);
	}

	@RequestMapping("/getfundprice")
	public @ResponseBody Object getFundPrice(@RequestParam String fundId) {
		List<ExternalFundPrice> externalFundPriceList = service.retrieveFundPriceListByFundId(fundId);
		return externalFundPriceList;
	}
	
	
	@RequestMapping("/generateReport")
	public void downloadReport(HttpServletResponse response) throws WriteException, IOException {
		service.exportAsExcel(((ExternalFundPriceService)service).new Report() {

			@Override
			protected void writeRow(WritableSheet sheet, ExternalFundPrice item, int row) throws WriteException {
				sheet.addCell(new Label(0, row, item.getId()));
				sheet.addCell(new Label(1, row, item.getCreatedBy()));
				sheet.addCell(new Label(2, row, item.getUpdatedBy() !=null ? item.getUpdatedBy() : " "));
				sheet.addCell(new Label(3, row, String.valueOf(item.getCreatedOn())));
				sheet.addCell(new Label(4, row, String.valueOf(item.getUpdatedOn() !=null ? item.getUpdatedOn() : " ")));
				
				sheet.addCell(new Label(6, row, item.getExternalFund().getId()));
				sheet.addCell(new Label(7, row, String.valueOf(item.getBuyPrice() !=null ? item.getBuyPrice() : " ")));
				sheet.addCell(new Label(8, row, String.valueOf(item.getSellPrice() !=null ? item.getSellPrice() : " ")));
				sheet.addCell(new Label(9, row, String.valueOf(item.getDealingPrice())));
				sheet.addCell(new Label(10, row, String.valueOf(item.getPriceDate() !=null ? item.getPriceDate() : " ")));
				
				
				
				
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
				
				sheet.addCell(new Label(6, row, "Fund ID"));
				sheet.addCell(new Label(7, row, "Buy Price"));
				sheet.addCell(new Label(8, row, "Sell Price"));
				sheet.addCell(new Label(9, row, "Dealing"));
				sheet.addCell(new Label(10, row, "Price Date"));

			}

			@Override
			protected String getSheetName() {
				return "External Fund Price";
			}

			@Override
			protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {
				return super.getWorkbook(response);
			}

		}, null, null, response);
	}

}
