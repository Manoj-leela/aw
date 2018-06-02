package sg.activewealth.roboadvisor.banking.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.banking.enums.RedemptionStatus;
import sg.activewealth.roboadvisor.banking.model.Redemption;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Repository
public class RedemptionDao extends AbstractDao<Redemption> {

    public PagingDto<Redemption> retrieveForListPage(String[] userPortfolioIds, PagingDto<Redemption> pagingDto, BigDecimal queryRedemptionAmount, LocalDate redemptionDate, String redemptionStatus) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Redemption.class);
        criteria.createAlias("userPortfolio", "userPortfolio");
        List<String> userPortfolioIdList = new ArrayList<>();
        if (userPortfolioIds != null && userPortfolioIds.length > 0) {
            userPortfolioIdList = Arrays.asList(userPortfolioIds);
            criteria.add(Restrictions.in("userPortfolio.id", userPortfolioIdList));
        }
        if (redemptionDate != null) {
            criteria.add(Restrictions.eq("redemptionDate", redemptionDate));
        }
        if (queryRedemptionAmount != null) {
            criteria.add(Restrictions.eq("redemptionAmount", queryRedemptionAmount));
        }
        if (!ValidationUtils.getInstance().isEmptyString(redemptionStatus) && RedemptionStatus.fromString(redemptionStatus) != null) {
            criteria.add(Restrictions.in("redemptionStatus", RedemptionStatus.valueOf(redemptionStatus)));
        }
        criteria.addOrder(Order.desc("createdOn"));
        return findByCriteria(criteria, pagingDto, false);
    }

    public List<Redemption> getRedemptionsByUserId(String userId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Redemption.class);
        criteria.createAlias("userPortfolio", "userPortfolio");
        criteria.createAlias("userPortfolio.user", "user");
        criteria.add(Restrictions.eq("user.id", userId));
        return findByCriteria(criteria, false);
    }

    public Redemption getRedemptionsByUserPortfolio(String userPortfolioId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Redemption.class);
        criteria.createAlias("userPortfolio", "userPortfolio");
        criteria.add(Restrictions.eq("userPortfolio.id", userPortfolioId));
        List<Redemption> redemptionList = findByCriteria(criteria, false);
        if (redemptionList != null && !redemptionList.isEmpty()) {
            return redemptionList.get(0);
        } else {
            return null;
        }
    }

    public RedemptionStatus getRedemptionStatus(String id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Redemption.class).setProjection(Projections.property("redemptionStatus")).add(Restrictions.eq("id", id));
        List<RedemptionStatus> redemptionStatusList = findByCriteria(criteria);
        if (redemptionStatusList != null && !redemptionStatusList.isEmpty()) {
            return redemptionStatusList.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<LocalDate> getRedemptionBatchesDesc(String[] userPortfolioIds, String strRedemptionStatus) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Redemption.class).setProjection(Projections.distinct(Projections.property("brokerBatch")));
        criteria.createAlias("userPortfolio", "userPortfolio");

        List<String> userPortfolioIdList = new ArrayList<String>();
        if (userPortfolioIds != null && userPortfolioIds.length > 0) {
            userPortfolioIdList = Arrays.asList(userPortfolioIds);
            criteria.add(Restrictions.in("userPortfolio.id", userPortfolioIdList));
        }
        if (!ValidationUtils.getInstance().isEmptyString(strRedemptionStatus) && RedemptionStatus.fromString(strRedemptionStatus) != null) {
            criteria.add(Restrictions.in("redemptionStatus", RedemptionStatus.valueOf(strRedemptionStatus)));
        }
        criteria.add(Restrictions.isNotNull("brokerBatch"));
        criteria.addOrder(Order.desc("brokerBatch"));
        return findByCriteria(criteria);
    }
}
