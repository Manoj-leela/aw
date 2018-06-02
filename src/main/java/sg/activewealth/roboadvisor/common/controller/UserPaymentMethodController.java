package sg.activewealth.roboadvisor.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.activewealth.roboadvisor.common.model.UserPaymentMethod;
import sg.activewealth.roboadvisor.common.service.UserPaymentMethodService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;

@Controller
@RequestMapping("/userPaymentMethod")
public class UserPaymentMethodController extends  CRUDController<UserPaymentMethod, UserPaymentMethodService> {

	public UserPaymentMethodController() {
		super();
	}
	
	public UserPaymentMethodController(Class<UserPaymentMethod> userPaymentMethod, UserPaymentMethodService service) {
		super(userPaymentMethod, service);
	}
	

}
