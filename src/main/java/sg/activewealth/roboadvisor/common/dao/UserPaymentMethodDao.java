package sg.activewealth.roboadvisor.common.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.common.model.UserPaymentMethod;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;

@Repository
public class UserPaymentMethodDao extends AbstractDao<UserPaymentMethod> {

	@SuppressWarnings("unchecked")
	public List<UserPaymentMethod> retrieveByUserId(String userId){
		List<UserPaymentMethod> userPaymentMethodList = new ArrayList<>();
		DetachedCriteria userPaymentMethodCriteria = DetachedCriteria.forClass(UserPaymentMethod.class);
		userPaymentMethodCriteria.createAlias("user", "user");
		userPaymentMethodCriteria.add(Restrictions.eq("user.id", userId));
		userPaymentMethodList = findByCriteria(userPaymentMethodCriteria);
		return userPaymentMethodList;
	}
}
