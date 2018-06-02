package sg.activewealth.roboadvisor.trade.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.trade.enums.UserTradeTransactionStatus;
import sg.activewealth.roboadvisor.trade.model.UserTradeTransaction;
import sg.activewealth.roboadvisor.trade.service.UserTradeTransactionService;

@Controller
@RequestMapping("/admin/userTradeTransaction")
public class UserTradeTransactionController
		extends CRUDController<UserTradeTransaction, UserTradeTransactionService> {

	public UserTradeTransactionController() {
		super();
	}

	public UserTradeTransactionController(Class<UserTradeTransaction> userTradeTransaction,
			UserTradeTransactionService service) {
		super(userTradeTransaction, service);
	}

	@Autowired
	public void setService(UserTradeTransactionService service) {
		this.service = service;
	}

	@Autowired
	public UserTradeTransactionController(UserTradeTransactionService service) {
		super(UserTradeTransaction.class, service); 
	}

	@Override
	public Object list(PagingDto<UserTradeTransaction> pagingDto, String ids, HttpServletRequest request) {
		String userName = request.getParameter("user.firstName");
		String[] status = request.getParameterValues("status");
		pagingDto = service.retrieveForListPage(pagingDto, userName, status);
		
		return modelAndView(getFullJspPath("list"), "list", pagingDto, "statuses", UserTradeTransactionStatus.values());
	}

}
