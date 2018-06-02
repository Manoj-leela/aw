package sg.activewealth.roboadvisor.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.banking.dao.BankDetailDao;
import sg.activewealth.roboadvisor.banking.model.BankDetail;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioAssignmentCategory;

@Service
public class BankDetailService extends AbstractService<BankDetail> {

	public BankDetailService() {
		super(BankDetail.class);
	}

	@Autowired
	public void setDao(BankDetailDao dao) {
		super.dao = dao;
	}
	
	public BankDetail retrieveByCategory(PortfolioAssignmentCategory category) {
		return  ((BankDetailDao)dao).retrieveByCategory(category);
	}
	
}
