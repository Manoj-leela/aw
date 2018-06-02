package sg.activewealth.roboadvisor.portfolio.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import sg.activewealth.roboadvisor.portfolio.model.Instrument;
import sg.activewealth.roboadvisor.portfolio.service.InstrumentService;
import sg.activewealth.roboadvisor.portfolio.service.PortfolioInstrumentService;

@Controller
@RequestMapping("/admin/instrument")
public class InstrumentController extends CRUDController<Instrument, InstrumentService> {
	
	@Autowired
	public void setService(InstrumentService service) {
		this.service = service;
	}

	@Autowired
	public InstrumentController(InstrumentService service) {
		super(Instrument.class, service);
	}

	public InstrumentController(Class<Instrument> instrument, InstrumentService service) {
		super(instrument, service);
	}
	
	@Autowired
	PortfolioInstrumentService portfolioInstrumentService;
	
	@Override
	public Object[] preCreateUpdateGet(Instrument model, HttpServletRequest request) {

		List<FormOptionDto> instrumentTypes = new ArrayList<FormOptionDto>();
		for (InstrumentType instrumentType : InstrumentType.values()) {
			instrumentTypes.add(new FormOptionDto(instrumentType.name(),instrumentType.getLabel()));
		}
		Boolean isInstrumentInUse = false;
		if(model.getId() != null){
			isInstrumentInUse = portfolioInstrumentService.isInstrumentInUse(model.getId());
		}
		return new Object[] {"instrumentTypes", instrumentTypes,"isInstrumentInUse",isInstrumentInUse};
	}
	
	@Override
	public Object list(PagingDto<Instrument> pagingDto, String ids, HttpServletRequest request) {
		String queryName = request.getParameter("name");
		String queryInstrumentCode = request.getParameter("code");
		String[] queryInstrumentType = request.getParameterValues("type");
		pagingDto = service.retrieveForListPage(pagingDto,queryName,queryInstrumentCode,queryInstrumentType);
		List<FormOptionDto> instrumentTypeList = new ArrayList<FormOptionDto>();
		for (InstrumentType instrument : InstrumentType.values()) {
			instrumentTypeList.add(new FormOptionDto(instrument.getLabel(), instrument.getLabel()));
		}
		return modelAndView(getFullJspPath("list"), "list", pagingDto,"instrumentTypeList", instrumentTypeList);
	}
	
	
	@RequestMapping("/generateReport")
	public void downloadReport(HttpServletResponse response) throws WriteException, IOException {
		service.exportAsExcel(((InstrumentService)service).new Report() {

			@Override
			protected void writeRow(WritableSheet sheet, Instrument item, int row) throws WriteException {
				sheet.addCell(new Label(0, row, item.getId()));
				sheet.addCell(new Label(1, row, item.getCreatedBy()));
				sheet.addCell(new Label(2, row, item.getUpdatedBy() !=null ? item.getUpdatedBy() : ""));
				sheet.addCell(new Label(3, row, String.valueOf(item.getCreatedOn())));
				sheet.addCell(new Label(4, row, String.valueOf(item.getUpdatedOn() !=null ? item.getUpdatedOn() : " ")));
				
				sheet.addCell(new Label(6, row, item.getName()));
				sheet.addCell(new Label(7, row, String.valueOf(item.getInstrumentType().getValue())));
				sheet.addCell(new Label(8, row, item.getCode() !=null ? item.getCode()  : " "));
				sheet.addCell(new Label(9, row, item.getSaxoInstrumentId() !=null ? item.getSaxoInstrumentId() : " "));
				sheet.addCell(new Label(10, row, item.getSaxoAssetType() !=null ? item.getSaxoAssetType() : " "));
				sheet.addCell(new Label(11, row, String.valueOf(item.getFeesPerTradeLeg() !=null ? item.getFeesPerTradeLeg() : " ")));
				
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
				sheet.addCell(new Label(7, row, "Instrument Type"));
				sheet.addCell(new Label(8, row, "Instrument Code"));
				sheet.addCell(new Label(9, row, "Saxo Instrument ID"));
				sheet.addCell(new Label(10, row, "Saxo Asset Type"));
				sheet.addCell(new Label(11, row, "Fees Per Trade Leg"));

			}

			@Override
			protected String getSheetName() {
				return "Instrument";
			}

			@Override
			protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {
				return super.getWorkbook(response);
			}

		}, null, null, response);
	}
	
	
}
