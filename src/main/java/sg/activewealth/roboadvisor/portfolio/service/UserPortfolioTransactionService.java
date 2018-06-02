package sg.activewealth.roboadvisor.portfolio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.portfolio.dao.UserPortfolioTransactionDao;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolioTransaction;

@Service
public class UserPortfolioTransactionService extends AbstractService<UserPortfolioTransaction> {

	public UserPortfolioTransactionService() {
		super(UserPortfolioTransaction.class);
	}

	@Autowired
	public void setDao(UserPortfolioTransactionDao dao) {
		super.dao = dao;
	}

	public List<UserPortfolioTransaction> getByCreatedOnAndUser(LocalDateTime startDateTime, LocalDateTime endDateTime,
			String userId) {
		return ((UserPortfolioTransactionDao) dao).getByCreatedOnAndUser(startDateTime, endDateTime, userId);
	}

	public List<UserPortfolioTransaction> retrieveUserPortFolio(String queryStatus, LocalDateTime localDateTime) {
		return ((UserPortfolioTransactionDao) dao).retrieveUserPortFolio(queryStatus, localDateTime);
	}

	public PagingDto<UserPortfolioTransaction> retrieveForListPage(PagingDto<UserPortfolioTransaction> pagingDto,
			String queryFirstName, String queryPortfolioName,String[] status) {
		pagingDto = ((UserPortfolioTransactionDao) dao).retrieveForListPage(pagingDto, queryFirstName,
				queryPortfolioName, status);
		return pagingDto;
	}
}
