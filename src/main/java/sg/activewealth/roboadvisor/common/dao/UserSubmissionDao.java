package sg.activewealth.roboadvisor.common.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.common.model.UserSubmission;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;

@Repository
public class UserSubmissionDao extends AbstractDao<UserSubmission> {
	
	public List<UserSubmission> getUserSubmissionsByUserId(String userId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserSubmission.class);
		criteria.createAlias("user", "user");
		criteria.add(Restrictions.eq("user.id", userId)).addOrder(Order.desc("createdOn"));
		return findByCriteria(criteria, false);
	}
}
