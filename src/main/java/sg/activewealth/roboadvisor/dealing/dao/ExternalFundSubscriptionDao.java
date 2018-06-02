package sg.activewealth.roboadvisor.dealing.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.dealing.model.ExternalFundSubscription;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Repository
public class ExternalFundSubscriptionDao extends AbstractDao<ExternalFundSubscription> {

	@Override
	protected void initProxies(ExternalFundSubscription entity, boolean fullInit) {
		super.initProxies(entity, fullInit);
	}

	public PagingDto<ExternalFundSubscription> retrieveForListPage(PagingDto<ExternalFundSubscription> pagingDto,
			String fundName) {
		DetachedCriteria fundSubscriptionCriteria = DetachedCriteria.forClass(ExternalFundSubscription.class);
		fundSubscriptionCriteria.createAlias("externalFund", "externalFund");
		if (!ValidationUtils.getInstance().isEmptyString(fundName)) {
			fundSubscriptionCriteria.add(Restrictions.like("externalFund.name", fundName, MatchMode.START));
		}
		fundSubscriptionCriteria.addOrder(Order.desc("createdOn"));
		return findByCriteria(fundSubscriptionCriteria, pagingDto, false);
	}

	@SuppressWarnings("unchecked")
	public ExternalFundSubscription retrieveClosestHigherShares(BigDecimal shares, String fundName) {
		DetachedCriteria c = DetachedCriteria.forClass(ExternalFundSubscription.class);
		c.createAlias("externalFund", "_externalFund");
		c.add(Restrictions.ge("balanceShares", shares));
		c.add(Restrictions.eq("_externalFund.name", fundName));
		c.addOrder(Order.asc("balanceShares"));
		List<ExternalFundSubscription> externalFundSubscriptionList = findByCriteria(c);
		if (externalFundSubscriptionList != null && !externalFundSubscriptionList.isEmpty()
				&& externalFundSubscriptionList.get(0) != null) {
			return externalFundSubscriptionList.get(0);
		}
		return null;
	}

	public ExternalFundSubscription isFundUsed(String fundId) {
		DetachedCriteria fundCriteria = DetachedCriteria.forClass(ExternalFundSubscription.class);
		fundCriteria.createAlias("externalFund", "externalFund");
		fundCriteria.add(Restrictions.eq("externalFund.id", fundId));
		List<ExternalFundSubscription> fundSubscriptionList = findByCriteria(fundCriteria, false);
		if (fundSubscriptionList.size() > 0) {
			return fundSubscriptionList.get(0);
		}
		return null;
	}

	public ExternalFundSubscription isFundPriceUsed(String fundPriceId) {
		DetachedCriteria fundCriteria = DetachedCriteria.forClass(ExternalFundSubscription.class);
		fundCriteria.createAlias("externalFundPrice", "externalFundPrice");
		fundCriteria.add(Restrictions.eq("externalFundPrice.id", fundPriceId));
		List<ExternalFundSubscription> fundSubscriptionList = findByCriteria(fundCriteria, false);
		if (fundSubscriptionList.size() > 0) {
			return fundSubscriptionList.get(0);
		}
		return null;
	}
}
