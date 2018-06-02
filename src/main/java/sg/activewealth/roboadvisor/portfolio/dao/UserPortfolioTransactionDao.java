package sg.activewealth.roboadvisor.portfolio.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioTransactionStatus;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolioTransaction;

@Repository
public class UserPortfolioTransactionDao extends AbstractDao<UserPortfolioTransaction> {

    @Autowired
    private UserService userService;

    public UserPortfolioTransaction add(final UserPortfolioTransactionStatus userPortfolioTransactionStatus, final UserPortfolio userPortfolio, final BigDecimal fundAmount) {
        final UserPortfolioTransaction userPortfolioTransaction = new UserPortfolioTransaction();
        userPortfolioTransaction.setUserPortfolio(userPortfolio);
        userPortfolioTransaction.setStatus(userPortfolioTransactionStatus);
        userPortfolioTransaction.setAmount(fundAmount);
        userPortfolioTransaction.setLastRetryOn(LocalDateTime.now());
        // userPortfolioTransaction.setCreatedBy(userService.getSystemUser().getId());
        userPortfolioTransaction.setCreatedBy("SYSTEM");
        userPortfolioTransaction.setCreatedOn(LocalDateTime.now());
        return save(userPortfolioTransaction);
    }

    public List<UserPortfolioTransaction> getByCreatedOnAndUser(LocalDateTime startDateTime, LocalDateTime endDateTime, String userId) {
        List<UserPortfolioTransaction> userPortfolioTransactions = new ArrayList<>();
        DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolioTransaction.class);
        criteria.createAlias("userPortfolio", "userPortfolio");
        criteria.add(Restrictions.eq("userPortfolio.user.id", userId));
        criteria.add(Restrictions.ge("createdOn", startDateTime));
        criteria.add(Restrictions.le("createdOn", endDateTime));
        userPortfolioTransactions = findByCriteria(criteria, true);
        return userPortfolioTransactions;
    }

    public List<UserPortfolioTransaction> retrieveUserPortFolio(String queryStatus, LocalDateTime localDateTime) {
        PagingDto<UserPortfolioTransaction> pagingDto = new PagingDto<>();
        DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolioTransaction.class);
        criteria.createAlias("userPortfolio", "userPortfolio");
        if (ValidationUtils.getInstance().isEmptyString(queryStatus)) {

            UserPortfolioStatus userPortfolioStatus = UserPortfolioStatus.get(queryStatus);
            if (userPortfolioStatus != null) {
                criteria.add(Restrictions.eq("userPortfolio.executionStatus", userPortfolioStatus));
            }
            UserPortfolioTransactionStatus transactionStatus = UserPortfolioTransactionStatus.getStatusByUserPortfolioStatus(userPortfolioStatus);
            criteria.add(Restrictions.eq("status", transactionStatus));
        }

        criteria.add(Restrictions.between("createdOn", localDateTime.minusHours(1), localDateTime));

        criteria.addOrder(Order.desc("createdOn"));
        return findByCriteria(criteria, pagingDto, false).getResults();
    }

    public PagingDto<UserPortfolioTransaction> retrieveForListPage(PagingDto<UserPortfolioTransaction> pagingDto, String queryFirstName, String queryPortfolioName, String[] status) {

        DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolioTransaction.class);

        criteria.createAlias("userPortfolio", "_userPortfolio");

        if (!ValidationUtils.getInstance().isEmptyString(queryFirstName)) {
            criteria.createAlias("_userPortfolio.user", "_user");
            criteria.add(Restrictions.like("_user.firstName", queryFirstName, MatchMode.START));
        }
        if (!ValidationUtils.getInstance().isEmptyString(queryPortfolioName)) {
            criteria.createAlias("_userPortfolio.portfolio", "_portfolio");
            criteria.add(Restrictions.ilike("_portfolio.name", queryPortfolioName, MatchMode.START));
        }
        if (null != status && status.length != 0) {
            List<UserPortfolioTransactionStatus> statusList = new ArrayList<UserPortfolioTransactionStatus>(status.length);
            for (String s : status) {
                statusList.add(UserPortfolioTransactionStatus.get(s));
            }
            criteria.add(Restrictions.in("status", statusList));
        }

        criteria.addOrder(Order.desc("createdOn"));
        return findByCriteria(criteria, pagingDto, false);

    }

}
