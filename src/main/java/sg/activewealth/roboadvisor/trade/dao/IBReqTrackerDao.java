package sg.activewealth.roboadvisor.trade.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.trade.model.IBReqTracker;

@Repository
public class IBReqTrackerDao extends AbstractDao<IBReqTracker> {

	@SuppressWarnings("unchecked")
	public IBReqTracker retrieveByReqId(int reqId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(IBReqTracker.class);
		criteria.add(Restrictions.eq("reqId", reqId));
		List<IBReqTracker> list = findByCriteria(criteria);
		if (list != null && !list.isEmpty() && list.get(0) != null) {
			return list.get(0);
		}
		return null;	
	}

}
