package sg.activewealth.roboadvisor.dealing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.activewealth.roboadvisor.dealing.model.CPFundAccount;
import sg.activewealth.roboadvisor.dealing.service.CPFundAccountService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;

@Controller
@RequestMapping("/admin/cpfund")
public class CPFundAccountController extends CRUDController<CPFundAccount, CPFundAccountService> {

	@Autowired
	public void setService(CPFundAccountService service) {
		this.service = service;
	}

	@Autowired
	public CPFundAccountController(CPFundAccountService service) {
		super(CPFundAccount.class, service);
	}
}
