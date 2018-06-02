
package sg.activewealth.roboadvisor.common.controller;
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
import sg.activewealth.roboadvisor.common.model.Agent;
import sg.activewealth.roboadvisor.common.service.AgentService;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;

@Controller
@RequestMapping("/admin/agent")
public class AgentController extends CRUDController<Agent, AgentService> {

	@Autowired
	public void setService(AgentService service) {
		this.service = service;
	}

	@Autowired
	public AgentController(AgentService service) {
		super(Agent.class, service);
	}
	
	@Autowired
	private UserService userService;

	@Override
	public Object[] preCreateUpdateGet(Agent model, HttpServletRequest request) {
		super.preCreateUpdateGet(model, request);
		Boolean isAgentInUse = false;
		if(model.getId() != null) {
			isAgentInUse = userService.isAgentInUse(model.getId());
		}
		return new Object[] {"isAgentInUse",isAgentInUse};
	}
	
	@Override
	public Object list(PagingDto<Agent> pagingDto, String ids, HttpServletRequest request) {
		String queryName = request.getParameter("name");
		String queryAgentCode = request.getParameter("agentCode");
		String queryMobileNumber = request.getParameter("mobileNumber");
		pagingDto = service.retrieveForListPage(pagingDto,queryName,queryAgentCode,queryMobileNumber);
		return modelAndView(getFullJspPath("list"), "list", pagingDto);
	}
	
	
	@RequestMapping("/generateReport")
	public void downloadReport(HttpServletResponse response) throws WriteException, IOException {
		service.exportAsExcel(((AgentService)service).new Report() {

			@Override
			protected void writeRow(WritableSheet sheet, Agent item, int row) throws WriteException {
				sheet.addCell(new Label(0, row, item.getId()));
				sheet.addCell(new Label(1, row, item.getCreatedBy()));
				sheet.addCell(new Label(2, row, item.getUpdatedBy() !=null ? item.getUpdatedBy() : " "));
				sheet.addCell(new Label(3, row, String.valueOf(item.getCreatedOn())));
				sheet.addCell(new Label(4, row, String.valueOf(item.getUpdatedOn() !=null ? item.getUpdatedOn() : " ")));
				
				sheet.addCell(new Label(6, row, item.getName()));
				sheet.addCell(new Label(7, row, item.getAgentCode()));
				sheet.addCell(new Label(8, row, item.getMobileNumber()));
				
				
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
				sheet.addCell(new Label(7, row, "Agent Code"));
				sheet.addCell(new Label(8, row, "Mobile Number"));

			}

			@Override
			protected String getSheetName() {
				return "Agent";
			}

			@Override
			protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {
				return super.getWorkbook(response);
			}

		}, null, null, response);
	}

	
}
