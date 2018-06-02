package sg.activewealth.roboadvisor.trade.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.trade.enums.TradeStatus;
import sg.activewealth.roboadvisor.trade.model.UserTrade;

@Repository
public class UserTradeDao extends AbstractDao<UserTrade> {

    @SuppressWarnings("unchecked")
    public List<UserTrade> getUserTradesByPortfolioInstrument(String portfolioInstrumentId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserTrade.class);
        criteria.add(Restrictions.eq("portfolioInstrument.id", portfolioInstrumentId));
        return findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    public List<UserTrade> getUserTradeByUserPortfolioId(String userPortfolioId) {
        List<UserTrade> trades = new ArrayList<>();
        DetachedCriteria userTradeCriteria = DetachedCriteria.forClass(UserTrade.class);
        userTradeCriteria.createAlias("userPortfolio", "userPortfolio");
        userTradeCriteria.add(Restrictions.eq("userPortfolio.id", userPortfolioId));
        trades = findByCriteria(userTradeCriteria);
        return trades;
    }

    @SuppressWarnings("unchecked")
    public List<UserTrade> getCompletedUserTradeByUserPortfolioId(String userPortfolioId) {
        List<UserTrade> trades = new ArrayList<>();
        DetachedCriteria userTradeCriteria = DetachedCriteria.forClass(UserTrade.class);
        userTradeCriteria.createAlias("userPortfolio", "userPortfolio");
        userTradeCriteria.add(Restrictions.eq("userPortfolio.id", userPortfolioId));
        userTradeCriteria.add(Restrictions.eq("executionStatus", TradeStatus.PlaceOrderCompleted));
        trades = findByCriteria(userTradeCriteria);
        return trades;
    }

    @SuppressWarnings("unchecked")
    public UserTrade getUserTrade(String userPortfolioId, String portfolioInstrumentId) {
        List<UserTrade> trades = new ArrayList<>();
        DetachedCriteria userTradeCriteria = DetachedCriteria.forClass(UserTrade.class);
        userTradeCriteria.createAlias("userPortfolio", "userPortfolio");
        userTradeCriteria.createAlias("portfolioInstrument", "portfolioInstrument");
        userTradeCriteria.add(Restrictions.eq("userPortfolio.id", userPortfolioId));
        userTradeCriteria.add(Restrictions.eq("portfolioInstrument.id", portfolioInstrumentId));
        trades = findByCriteria(userTradeCriteria);
        if (!trades.isEmpty() && trades.size() > 0) {
            return trades.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public UserTrade retrieveByRoboOrderId(String orderId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserTrade.class);
        criteria.add(Restrictions.eq("roboOrderId", orderId));
        List<UserTrade> userTrades = findByCriteria(criteria);
        if (userTrades != null && !userTrades.isEmpty()) {
            return userTrades.get(0);
        }
        return null;
    }

    public PagingDto<UserTrade> retrieveForListPage(PagingDto<UserTrade> pagingDto, String queryFirstName, String[] executionStatus, LocalDate createdOn) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserTrade.class);

        criteria.createAlias("userPortfolio", "_userPortfolio");
        criteria.createAlias("_userPortfolio.user", "_user");

        if (!ValidationUtils.getInstance().isEmptyString(queryFirstName)) {
            criteria.add(Restrictions.like("_user.firstName", queryFirstName, MatchMode.START));
        }
        if (null != executionStatus && executionStatus.length != 0) {
            List<TradeStatus> executionStatuses = new ArrayList<TradeStatus>(executionStatus.length);
            for (String s : executionStatus) {
                executionStatuses.add(TradeStatus.get(s));
            }
            criteria.add(Restrictions.in("executionStatus", executionStatuses));
        }

        if (createdOn != null) {
            criteria.add(Restrictions.between("createdOn", createdOn.atStartOfDay(), createdOn.atTime(23, 59)));
        }
        criteria.addOrder(Order.desc("createdOn"));
        return findByCriteria(criteria, pagingDto, false);
    }

    @SuppressWarnings("unchecked")
    public List<UserTrade> retrieveByUser(String userId) {
        List<UserTrade> trades = new ArrayList<>();
        DetachedCriteria userTradeCriteria = DetachedCriteria.forClass(UserTrade.class);
        userTradeCriteria.createAlias("userPortfolio.user", "user");
        userTradeCriteria.add(Restrictions.eq("user.id", userId));
        trades = findByCriteria(userTradeCriteria);
        return trades;
    }

}
