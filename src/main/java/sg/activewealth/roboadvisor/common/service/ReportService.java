package sg.activewealth.roboadvisor.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.common.dao.ReportDao;
import sg.activewealth.roboadvisor.common.dto.ManagementDataReportDto;
import sg.activewealth.roboadvisor.infra.service.AbstractService;

@Service
public class ReportService  extends AbstractService<ManagementDataReportDto.Main> {

	public ReportService() {
		super(ManagementDataReportDto.Main.class);
	}
	

	@Autowired
	public void setDao(ReportDao dao) {
		super.dao = dao;
	}
	
}
