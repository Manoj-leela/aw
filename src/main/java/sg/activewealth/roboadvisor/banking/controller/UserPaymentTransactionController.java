package sg.activewealth.roboadvisor.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.activewealth.roboadvisor.banking.model.UserPaymentTransaction;
import sg.activewealth.roboadvisor.banking.service.UserPaymentTransactionService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;

@Controller
@RequestMapping("/admin/userPaymentTransaction")
public class UserPaymentTransactionController extends CRUDController<UserPaymentTransaction, UserPaymentTransactionService> {

	public UserPaymentTransactionController() {
		super();
	}

	public UserPaymentTransactionController(Class<UserPaymentTransaction> modelClass,
			UserPaymentTransactionService service) {
		super(modelClass, service);
	}
	
	@Autowired
	public void setService(UserPaymentTransactionService service) {
		this.service = service;
	}
	
	@Autowired
	public UserPaymentTransactionController(UserPaymentTransactionService service) {
		super(UserPaymentTransaction.class, service);
	} 
}
