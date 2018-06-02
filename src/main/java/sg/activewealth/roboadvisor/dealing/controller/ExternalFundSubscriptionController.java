package sg.activewealth.roboadvisor.dealing.controller;

import java.io.IOException;
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
import sg.activewealth.roboadvisor.dealing.model.ExternalFund;
import sg.activewealth.roboadvisor.dealing.model.ExternalFundPrice;
import sg.activewealth.roboadvisor.dealing.model.ExternalFundSubscription;
import sg.activewealth.roboadvisor.dealing.service.ExternalFundPriceService;
import sg.activewealth.roboadvisor.dealing.service.ExternalFundService;
import sg.activewealth.roboadvisor.dealing.service.ExternalFundSubscriptionService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;

@Controller
@RequestMapping("/admin/externalFundSubscription")
public class ExternalFundSubscriptionController
		extends CRUDController<ExternalFundSubscription, ExternalFundSubscriptionService> {

	@Autowired
	public ExternalFundSubscriptionController(ExternalFundSubscriptionService service) {
		super(ExternalFundSubscription.class, service);
	}

	@Autowired
	public void setService(ExternalFundSubscriptionService service) {
		this.service = service;
	}

	@Autowired
	private ExternalFundService externalFundService;

	@Autowired
	private ExternalFundPriceService externalFundPriceService;

	@Override
	public Object[] preCreateUpdateGet(ExternalFundSubscription model, HttpServletRequest request) {
		PagingDto<ExternalFund> fundPagingDto = externalFundService.retrieve("createdOn", null);
		PagingDto<ExternalFundPrice> fundPricePagingDto = new PagingDto<>();
		
		if(model.getExternalFundPrice() != null) {
			List<ExternalFundPrice> externalFundPriceList = externalFundPriceService.retrieveFundPriceListByFundId(model.getExternalFund().getId());
			fundPricePagingDto.setResults(externalFundPriceList);
		} else {
			fundPricePagingDto = externalFundPriceService.retrieve("createdOn", null);
		}
		
		return new Object[] { "fundList", fundPagingDto.getResults(), "fundPriceList",
				fundPricePagingDto.getResults() };
	}

	@Override
	public ExternalFundSubscription preCreateUpdatePost(ExternalFundSubscription model, HttpServletRequest request) {
		model = super.preCreateUpdatePost(model, request);
		String fundId = model.getExternalFund().getId();
		String priceId = model.getExternalFundPrice().getId();

		ExternalFund externalFund = externalFundService.retrieve(fundId);
		ExternalFundPrice externalFundPrice = externalFundPriceService.retrieve(priceId);

		model.setExternalFund(externalFund);
		model.setExternalFundPrice(externalFundPrice);
		return model;
	}
	@Override
	public Object list(PagingDto<ExternalFundSubscription> pagingDto, String ids, HttpServletRequest request) {
		String queryFundName = request.getParameter("name");
		pagingDto = service.retrieveForListPage(pagingDto,queryFundName);
		return modelAndView(getFullJspPath("list"), "list", pagingDto);

	}
	
	@RequestMapping("/generateReport")
	public void downloadReport(HttpServletResponse response) throws WriteException, IOException {
		service.exportAsExcel(((ExternalFundSubscriptionService)service).new Report() {

			@Override
			protected void writeRow(WritableSheet sheet, ExternalFundSubscription item, int row) throws WriteException {
				sheet.addCell(new Label(0, row, item.getId()));
				sheet.addCell(new Label(1, row, item.getCreatedBy()));
				sheet.addCell(new Label(2, row, item.getUpdatedBy() !=null ? item.getUpdatedBy() : " "));
				sheet.addCell(new Label(3, row, String.valueOf(item.getCreatedOn())));
				sheet.addCell(new Label(4, row, String.valueOf(item.getUpdatedOn() !=null ? item.getUpdatedOn() : " ")));
				
				sheet.addCell(new Label(6, row, item.getExternalFund().getId()));
				sheet.addCell(new Label(7, row, item.getExternalFundPrice().getId()));
				sheet.addCell(new Label(8, row, String.valueOf(item.getTotalSubscriptionAmount() !=null ? item.getTotalSubscriptionAmount() : "")));
				sheet.addCell(new Label(9, row, String.valueOf(item.getNetInvestAmount() !=null ? item.getNetInvestAmount() : " ")));
				sheet.addCell(new Label(10, row, String.valueOf(item.getShares() !=null ? item.getShares() : " ")));
				sheet.addCell(new Label(11, row, String.valueOf(item.getBalanceShares() !=null ? item.getBalanceShares() : " ")));
				
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
				
				sheet.addCell(new Label(6, row, "External Fund ID"));
				sheet.addCell(new Label(7, row, "External Fund Price ID"));
				sheet.addCell(new Label(8, row, "Subscription Amount"));
				sheet.addCell(new Label(9, row, "Net Invest Amount"));
				sheet.addCell(new Label(10, row, "Shares"));
				sheet.addCell(new Label(11, row, "Balance Shares"));
			}

			@Override
			protected String getSheetName() {
				return "External Fund Subscription";
			}

			@Override
			protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {
				return super.getWorkbook(response);
			}

		}, null, null, response);
	}
}
