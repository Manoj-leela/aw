package sg.activewealth.roboadvisor.dealing.controller;

import java.io.IOException;

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
@RequestMapping("/admin/externalFund")
public class ExternalFundController extends CRUDController<ExternalFund, ExternalFundService> {

	@Autowired
	public ExternalFundController(ExternalFundService service) {
		super(ExternalFund.class, service);
	}
	
	@Autowired
	public void setService(ExternalFundService service) {
		this.service = service;
	}
	
	@Autowired
	private ExternalFundPriceService externalFundPriceService;
	
	@Autowired
	private ExternalFundSubscriptionService externalFundSubscriptionService;
	
	@Override
	public Object[] preCreateUpdateGet(ExternalFund model, HttpServletRequest request) {
		super.preCreateUpdateGet(model, request);
		Boolean isFundUsed = false;
		if(model.getId() != null) {
			ExternalFundPrice fundPrice = externalFundPriceService.isFundUsed(model.getId());
			ExternalFundSubscription fundSubscription = externalFundSubscriptionService.isFundUsed(model.getId());
			if(fundPrice != null || fundSubscription != null ) {
				isFundUsed = true;
			}
		}
		return new Object[] {"isFundUsed",isFundUsed};
	}
	@Override
	public Object list(PagingDto<ExternalFund> pagingDto, String ids, HttpServletRequest request) {
		String queryName = request.getParameter("name");
		pagingDto = service.retrieveForListPage(pagingDto,queryName);
		return modelAndView(getFullJspPath("list"), "list", pagingDto);
	}

	@RequestMapping("/generateReport")
	public void downloadReport(HttpServletResponse response) throws WriteException, IOException {
		service.exportAsExcel(((ExternalFundService)service).new Report() {

			@Override
			protected void writeRow(WritableSheet sheet, ExternalFund item, int row) throws WriteException {
				sheet.addCell(new Label(0, row, item.getId()));
				sheet.addCell(new Label(1, row, item.getCreatedBy()));
				sheet.addCell(new Label(2, row, item.getUpdatedBy() !=null ? item.getUpdatedBy() : " "));
				sheet.addCell(new Label(3, row, String.valueOf(item.getCreatedOn())));
				sheet.addCell(new Label(4, row, String.valueOf(item.getUpdatedOn() !=null ? item.getUpdatedOn() : " ")));
				
				sheet.addCell(new Label(6, row, item.getName()));

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
			}

			@Override
			protected String getSheetName() {
				return "External Fund";
			}
			
			@Override
			protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {
				return super.getWorkbook(response);
			}
		}, null, null, response);
	}
	
}
