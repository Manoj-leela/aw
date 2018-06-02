package sg.activewealth.roboadvisor.dealing.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.dealing.model.ExternalFundPrice;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Repository
public class ExternalFundPriceDao extends AbstractDao<ExternalFundPrice> {

	@Override
	protected void initProxies(ExternalFundPrice entity, boolean fullInit) {
		super.initProxies(entity, fullInit);
	}

	public PagingDto<ExternalFundPrice> retrieveForListPage(PagingDto<ExternalFundPrice> pagingDto, String name,
			LocalDateTime priceDate, List<Boolean> dealingList) {
		DetachedCriteria fundPriceCriteria = DetachedCriteria.forClass(ExternalFundPrice.class);
		fundPriceCriteria.createAlias("externalFund", "externalFund");
		if(!ValidationUtils.getInstance().isEmptyString(name)){
			fundPriceCriteria.add(Restrictions.like("externalFund.name", name, MatchMode.START));
		}
		if(priceDate != null){
			fundPriceCriteria.add(Restrictions.eq("priceDate", priceDate));
		}
		if(!dealingList.isEmpty()){
			fundPriceCriteria.add(Restrictions.in("dealingPrice", dealingList));
		}
		fundPriceCriteria.addOrder(Order.desc("createdOn"));
		return findByCriteria(fundPriceCriteria, pagingDto, false);
	}
	
	@SuppressWarnings("unchecked")
	public List<ExternalFundPrice> retrieveFunPriceListByFundId(String id) {
		DetachedCriteria fundPriceCriteria = DetachedCriteria.forClass(ExternalFundPrice.class);
		fundPriceCriteria.createAlias("externalFund", "externalFund");
		fundPriceCriteria.add(Restrictions.eq("externalFund.id", id)).add(Restrictions.eq("dealingPrice", true));
		fundPriceCriteria.addOrder(Order.asc("createdOn"));
		List<ExternalFundPrice> externalFundPriceList = findByCriteria(fundPriceCriteria);
		return externalFundPriceList;
	}

	public ExternalFundPrice retrieveClosestPriceBeforeNowWithDealingTrueForFund(String fundName) {
		return retrieveClosestPriceBeforeNowWithDealingForFund(fundName,Boolean.TRUE);
	}
	@SuppressWarnings("unchecked")	
	public ExternalFundPrice retrieveClosestPriceBeforeNowWithDealingForFund(String fundName,Boolean dealing) {
		DetachedCriteria c = DetachedCriteria.forClass(ExternalFundPrice.class);
		c.createAlias("externalFund", "externalFund");
		c.add(Restrictions.eq("externalFund.name", fundName));
		c.add(Restrictions.eq("dealingPrice", dealing));
		c.add(Restrictions.lt("priceDate", LocalDateTime.now()));
		c.addOrder(Order.desc("priceDate"));
		List<ExternalFundPrice> externalFundPriceList = findByCriteria(c);
		if (externalFundPriceList != null && externalFundPriceList.size() > 0 && externalFundPriceList.get(0) != null) {
			return externalFundPriceList.get(0);
		}
		return null;
	}

	public ExternalFundPrice isFundUsed(String fundId) {
		DetachedCriteria fundCriteria = DetachedCriteria.forClass(ExternalFundPrice.class);
		fundCriteria.createAlias("externalFund", "externalFund");
		fundCriteria.add(Restrictions.eq("externalFund.id", fundId));
		List<ExternalFundPrice> fundPriceList = findByCriteria(fundCriteria, false);
		if (fundPriceList.size() > 0) {
			return fundPriceList.get(0);
		}
		return null;
	}
	
	
}
