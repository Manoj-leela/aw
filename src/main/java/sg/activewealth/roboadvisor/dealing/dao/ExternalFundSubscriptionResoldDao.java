package sg.activewealth.roboadvisor.dealing.dao;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.dealing.model.ExternalFundSubscriptionResold;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Repository
public class ExternalFundSubscriptionResoldDao extends AbstractDao<ExternalFundSubscriptionResold> {

	@Override
	protected void initProxies(ExternalFundSubscriptionResold entity, boolean fullInit) {
		super.initProxies(entity, fullInit);
	}
	
	
	public PagingDto<ExternalFundSubscriptionResold> retrieveForListPage(PagingDto<ExternalFundSubscriptionResold> pagingDto,
			String fundName, LocalDate transactionDate, BigDecimal buyPrice, BigDecimal sellPrice ) {
		DetachedCriteria resoldCriteria = DetachedCriteria.forClass(ExternalFundSubscriptionResold.class);
		resoldCriteria.createAlias("externalFundPrice", "_externalFundPrice");
		resoldCriteria.createAlias("_externalFundPrice.externalFund", "_externalFund");
		
		if (!ValidationUtils.getInstance().isEmptyString(fundName)) {
			resoldCriteria.add(Restrictions.like("_externalFund.name", fundName, MatchMode.START));
		}
		if (transactionDate!=null) {
			resoldCriteria.add(Restrictions.between("transactionDate", transactionDate.atStartOfDay(),transactionDate.atTime(23,59)));
		}
		
		if (buyPrice!=null) {
			resoldCriteria.add(Restrictions.eq("_externalFundPrice.buyPrice", buyPrice));
		}
		if (sellPrice!=null) {
			resoldCriteria.add(Restrictions.eq("_externalFundPrice.sellPrice", sellPrice));
		}
		
		resoldCriteria.addOrder(Order.desc("createdOn"));
		return findByCriteria(resoldCriteria, pagingDto, false);
	}
}
