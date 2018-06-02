package sg.activewealth.roboadvisor.portfolio.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioTransactionStatus;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolioTransaction;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioTransactionService;

@Controller
@RequestMapping("/admin/userPortfolioTransaction")
public class UserPortfolioTransactionController extends  CRUDController<UserPortfolioTransaction, UserPortfolioTransactionService> {

	public UserPortfolioTransactionController() {
		super();
	}
	public UserPortfolioTransactionController(Class<UserPortfolioTransaction> userPortfolioTransaction, UserPortfolioTransactionService service) {
		super(userPortfolioTransaction, service);
	}
	
	@Autowired
	public void setService(UserPortfolioTransactionService service) {
		this.service = service;
	}
	
	@Autowired
	public UserPortfolioTransactionController(UserPortfolioTransactionService service) {
		super(UserPortfolioTransaction.class, service);
	} 
	
	@Override
	public Object list(PagingDto<UserPortfolioTransaction> pagingDto, String ids, HttpServletRequest request) {
		
		String queryFirstName= request.getParameter("userPortfolio.user.firstName");
		String queryPortfolioName = request.getParameter("userPortfolio.portfolio.name");
		String[] status = request.getParameterValues("status");
		
		pagingDto = service.retrieveForListPage(pagingDto, queryFirstName, queryPortfolioName, status);
		return modelAndView(getFullJspPath("list"), "list", pagingDto, "statuses", UserPortfolioTransactionStatus.values());
	}
}
