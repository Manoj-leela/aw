package sg.activewealth.roboadvisor.dealing.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import sg.activewealth.roboadvisor.dealing.model.ExternalFundSubscriptionResold;
import sg.activewealth.roboadvisor.dealing.service.ExternalFundSubscriptionResoldService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Controller
@RequestMapping("/admin/externalFundSubscriptionResold")
public class ExternalFundSubscriptionResoldController  extends CRUDController<ExternalFundSubscriptionResold, ExternalFundSubscriptionResoldService> {

    @Autowired
    public ExternalFundSubscriptionResoldController(ExternalFundSubscriptionResoldService service) {
        super(ExternalFundSubscriptionResold.class, service);
    }
    @Autowired
	public void setService(ExternalFundSubscriptionResoldService service) {
		this.service = service;
	}
    @Override
	public Object[] preCreateUpdateGet(ExternalFundSubscriptionResold model, HttpServletRequest request) {
		return new Object[] { "model", model};
	}
    
    
    @Override
    public Object list(PagingDto<ExternalFundSubscriptionResold> pagingDto, String ids, HttpServletRequest request) {
    	
    	String queryFundName = request.getParameter("name");
    	String queryTransactionDate = request.getParameter("transactionDate");
    	String buyPrice = request.getParameter("buyPrice");
    	String sellPrice = request.getParameter("sellPrice");
    	
    	BigDecimal queryBuyPriceAmount = null;
    	BigDecimal querySellPriceAmount = null;
    	
    	LocalDate transactionDate = null;
    	if(!ValidationUtils.getInstance().isEmptyString(queryTransactionDate)){
			try{
				transactionDate = LocalDate.parse(queryTransactionDate);
			} catch (DateTimeParseException e) {
				logger.error("Date parsing error occurred:"+e.getMessage());
			}
		}
    	
		if(!ValidationUtils.getInstance().isEmptyString(buyPrice)){
			queryBuyPriceAmount = new BigDecimal(buyPrice).setScale(3);
		}
		
		if(!ValidationUtils.getInstance().isEmptyString(sellPrice)){
			querySellPriceAmount = new BigDecimal(sellPrice).setScale(3);
		}
		
		pagingDto = service.retrieveForListPage(pagingDto, queryFundName, transactionDate, queryBuyPriceAmount, querySellPriceAmount);
		return modelAndView(getFullJspPath("list"), "list", pagingDto);
    }
    
    
	@RequestMapping("/generateReport")
	public void downloadReport(HttpServletResponse response) throws WriteException, IOException {
		service.exportAsExcel(((ExternalFundSubscriptionResoldService)service).new Report() {

			@Override
			protected void writeRow(WritableSheet sheet, ExternalFundSubscriptionResold item, int row) throws WriteException {
				sheet.addCell(new Label(0, row, item.getId()));
				sheet.addCell(new Label(1, row, item.getCreatedBy()));
				sheet.addCell(new Label(2, row, item.getUpdatedBy() !=null ? item.getUpdatedBy() : " "));
				sheet.addCell(new Label(3, row, String.valueOf(item.getCreatedOn())));
				sheet.addCell(new Label(4, row, String.valueOf(item.getUpdatedOn() !=null ? item.getUpdatedOn() : " ")));
				
				sheet.addCell(new Label(6, row, item.getExternalFundSubscription() !=null ? item.getExternalFundSubscription().getId() : " "));
				sheet.addCell(new Label(7, row, item.getExternalFundPrice() !=null ? item.getExternalFundPrice().getId() : " "));
				sheet.addCell(new Label(8, row, String.valueOf(item.getTransactionDate() !=null ? item.getTransactionDate() : " ")));
				sheet.addCell(new Label(9, row, String.valueOf(item.getTotalSubscriptionAmount() !=null ? item.getTotalSubscriptionAmount() : " ")));
				sheet.addCell(new Label(10, row, String.valueOf(item.getNetInvestAmount() !=null ? item.getNetInvestAmount() : " ")));
				sheet.addCell(new Label(11, row, String.valueOf(item.getShares() !=null ?  item.getShares() : " ")));
				
				
				
				
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
				
				sheet.addCell(new Label(6, row, "External Fund Subscription ID"));
				sheet.addCell(new Label(7, row, "External Fund Price ID"));
				sheet.addCell(new Label(8, row, "Transaction Date"));
				sheet.addCell(new Label(9, row, "Total Subscription Amount"));
				sheet.addCell(new Label(10, row, "Net Invest Amount"));
				sheet.addCell(new Label(11, row, "Shares"));

			}

			@Override
			protected String getSheetName() {
				return "External Fund Subscription Resold";
			}

			@Override
			protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {
				return super.getWorkbook(response);
			}

		}, null, null, response);
	}
    
}
