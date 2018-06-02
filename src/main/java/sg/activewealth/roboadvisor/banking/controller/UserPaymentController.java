package sg.activewealth.roboadvisor.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sg.activewealth.roboadvisor.banking.enums.PaymentStatus;
import sg.activewealth.roboadvisor.banking.model.UserPayment;
import sg.activewealth.roboadvisor.banking.service.UserPaymentService;
import sg.activewealth.roboadvisor.common.jobs.runner.PaymentInitiationJobRunner;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;

@Controller
@RequestMapping("/admin/userPayment")
public class UserPaymentController extends CRUDController<UserPayment, UserPaymentService>{

	public UserPaymentController() {
		super();
	}

	public UserPaymentController(Class<UserPayment> modelClass, UserPaymentService service) {
		super(modelClass, service);
	}

	@Autowired
	public void setService(UserPaymentService service) {
		this.service = service;
	}
	
	@Autowired
	public UserPaymentController(UserPaymentService service) {
		super(UserPayment.class, service);
	} 
	
	@Autowired
	PaymentInitiationJobRunner paymentInitiationJobRunner;
	
	@RequestMapping(value = { "/initiatePayment/{action}" }, method = RequestMethod.GET)
	public Object initiatePaymentRequest(@PathVariable(value = "action") String paymentStatusAction){
		if(paymentStatusAction.equals("readyPayment")){
			paymentInitiationJobRunner.initiatePayment(PaymentStatus.ReadyForPayment);
		}
		if(paymentStatusAction.equals("paymentError")){
			paymentInitiationJobRunner.initiatePayment(PaymentStatus.PaymentError);
		}
		return "redirect:/r/admin/transaction/list";
		
	}
}
