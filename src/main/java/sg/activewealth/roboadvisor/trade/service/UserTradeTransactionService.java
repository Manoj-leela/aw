package sg.activewealth.roboadvisor.trade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.trade.dao.UserTradeTransactionDao;
import sg.activewealth.roboadvisor.trade.model.UserTradeTransaction;

@Service
public class UserTradeTransactionService extends AbstractService<UserTradeTransaction> {

	public UserTradeTransactionService() {
		super(UserTradeTransaction.class);
	}

	@Autowired
	public void setDao(UserTradeTransactionDao dao) {
		super.dao = dao;
	}
	
	
	public PagingDto<UserTradeTransaction> retrieveForListPage(PagingDto<UserTradeTransaction> pagingDto,
			String queryFirstName, String[] status) {
		pagingDto = ((UserTradeTransactionDao) dao).retrieveForListPage(pagingDto, queryFirstName, status);
		return pagingDto;
	}
}
