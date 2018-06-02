package sg.activewealth.roboadvisor.dealing.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.dealing.model.ExternalFund;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Repository
public class ExternalFundDao extends AbstractDao<ExternalFund> {

	@Override
	protected void initProxies(ExternalFund entity, boolean fullInit) {
		super.initProxies(entity, fullInit);
	}

	public PagingDto<ExternalFund> retrieveForListPage(PagingDto<ExternalFund> pagingDto, String name) {
		DetachedCriteria fundCriteria = DetachedCriteria.forClass(ExternalFund.class);
		if(!ValidationUtils.getInstance().isEmptyString(name)){
			fundCriteria.add(Restrictions.like("name", name, MatchMode.START));
		}
		fundCriteria.addOrder(Order.desc("createdOn"));
		return findByCriteria(fundCriteria, pagingDto, false);
	}
	
	public ExternalFund isFundNameUsed(String fundName) {
		DetachedCriteria fundCriteria = DetachedCriteria.forClass(ExternalFund.class).add(Restrictions.eq("name", fundName));
		List<ExternalFund> fundList = findByCriteria(fundCriteria, false);
		if (fundList.size() > 0) {
			return fundList.get(0);
		}
		return null;
	}
	
}
