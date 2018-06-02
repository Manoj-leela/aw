package sg.activewealth.roboadvisor.dealing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.activewealth.roboadvisor.dealing.model.CPFundAccountTransaction;
import sg.activewealth.roboadvisor.dealing.service.CPFundAccountTransactionService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;

@Controller
@RequestMapping("/admin/cpfund/transaction")
public class CPFundAccountTransactionController extends CRUDController<CPFundAccountTransaction, CPFundAccountTransactionService> {

	@Autowired
	public void setService(CPFundAccountTransactionService service) {
		this.service = service;
	}

	@Autowired
	public CPFundAccountTransactionController(CPFundAccountTransactionService service) {
		super(CPFundAccountTransaction.class, service);
	}
}
