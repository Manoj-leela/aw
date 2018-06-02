package sg.activewealth.roboadvisor.dealing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.dealing.dao.CPFundAccountDao;
import sg.activewealth.roboadvisor.dealing.model.CPFundAccount;
import sg.activewealth.roboadvisor.infra.service.AbstractService;

@Service
public class CPFundAccountService extends AbstractService<CPFundAccount> {

	public CPFundAccountService() {
		super(CPFundAccount.class);
	}
	
	@Autowired
	public void setDao(CPFundAccountDao dao) {
		super.dao = dao;
	}
}
