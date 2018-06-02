package sg.activewealth.roboadvisor.dealing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.dealing.dao.CPFundAccountTransactionDao;
import sg.activewealth.roboadvisor.dealing.model.CPFundAccountTransaction;
import sg.activewealth.roboadvisor.infra.service.AbstractService;

@Service
public class CPFundAccountTransactionService extends AbstractService<CPFundAccountTransaction> {

	public CPFundAccountTransactionService() {
		super(CPFundAccountTransaction.class);
	}
	
	@Autowired
	public void setDao(CPFundAccountTransactionDao dao) {
		super.dao = dao;
	}
}
