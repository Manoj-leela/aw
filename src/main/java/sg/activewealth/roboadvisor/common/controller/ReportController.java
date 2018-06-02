package sg.activewealth.roboadvisor.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.activewealth.roboadvisor.common.dto.ManagementDataReportDto;
import sg.activewealth.roboadvisor.common.service.AgentService;
import sg.activewealth.roboadvisor.common.service.ReportService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.enums.ReportEnum;
import sg.activewealth.roboadvisor.infra.service.AbstractService;

@Controller
@RequestMapping("/admin/report")
public class ReportController extends CRUDController<ManagementDataReportDto.Main, ReportService> {
	protected Logger logger = Logger.getLogger(AbstractService.class);

	@Autowired
	public ReportController(ReportService service) {
		super(ManagementDataReportDto.Main.class, service);
	}

	@Autowired
	public void setService(ReportService service) {
		this.service = service;
	}
	
	@Autowired
	AgentService agentService;

	@Override
	public Object[] preCreateUpdateGet(ManagementDataReportDto.Main model, HttpServletRequest request) {

		return new Object[] { "model", model,"reports",ReportEnum.values()};
	}
	
}
