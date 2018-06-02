package sg.activewealth.roboadvisor.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.activewealth.roboadvisor.common.model.UserMessage;
import sg.activewealth.roboadvisor.common.service.UserMessageService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;

@Controller
@RequestMapping("/userMessage")
public class UserMessageController extends CRUDController<UserMessage, UserMessageService> {

	public UserMessageController() {
		super();
	}
	public UserMessageController(Class<UserMessage> userMessage, UserMessageService service) {
		super(userMessage, service);
	}
	
	

}
