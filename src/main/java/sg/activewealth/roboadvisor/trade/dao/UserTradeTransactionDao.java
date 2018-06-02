package sg.activewealth.roboadvisor.trade.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.trade.enums.UserTradeTransactionStatus;
import sg.activewealth.roboadvisor.trade.model.UserTrade;
import sg.activewealth.roboadvisor.trade.model.UserTradeTransaction;

@Repository
public class UserTradeTransactionDao extends AbstractDao<UserTradeTransaction> {

  public UserTradeTransaction add(final UserTradeTransactionStatus status, final UserTrade userTrade, final String description){
    final UserTradeTransaction userTradeTransactions = new UserTradeTransaction();
    userTradeTransactions.setUserTrade(userTrade);
    userTradeTransactions.setInitiateDate(LocalDateTime.now());
    userTradeTransactions.setStatus(status);
    userTradeTransactions.setDescription(description);
    userTradeTransactions.setCreatedOn(LocalDateTime.now());
    // userTradeTransactions.setCreatedBy(userService.getSystemUser().getId());
	userTradeTransactions.setCreatedBy("SYSTEM");
    userTradeTransactions.setLastRetryOn(LocalDateTime.now());
    return save(userTradeTransactions);
  }

  public void deleteUserTradeTransactions(String userTradeId){
	  Session session = getCurrentSession();
	  Query query = session.createQuery("DELETE FROM UserTradeTransaction WHERE userTrade = '"+userTradeId+"'");
	  query.executeUpdate();
  }
  
	public PagingDto<UserTradeTransaction> retrieveForListPage(PagingDto<UserTradeTransaction> pagingDto,
			String queryFirstName, String[] status) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserTradeTransaction.class);

		if (!ValidationUtils.getInstance().isEmptyString(queryFirstName)) {
			criteria.createAlias("userTrade", "_userTrade");
			criteria.createAlias("_userTrade.userPortfolio", "_userPortfolio");
			criteria.createAlias("_userPortfolio.user", "_user");
			criteria.add(Restrictions.like("_user.firstName", queryFirstName, MatchMode.START));
		}
		if (null != status && status.length != 0) {
			List<UserTradeTransactionStatus> statusList = new ArrayList<UserTradeTransactionStatus>(status.length);
			for (String s : status) {
				statusList.add(UserTradeTransactionStatus.get(s));
			}
			criteria.add(Restrictions.in("status", statusList));
		}

		criteria.addOrder(Order.desc("createdOn"));
		return findByCriteria(criteria, pagingDto, false);
	}

  
}
