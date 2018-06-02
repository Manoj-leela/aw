package sg.activewealth.roboadvisor.banking.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.banking.enums.PaymentStatus;
import sg.activewealth.roboadvisor.banking.model.UserPayment;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;

@Repository
public class UserPaymentDao extends AbstractDao<UserPayment> {

  @SuppressWarnings("unchecked")
  public List<UserPayment> retrieveByPaymentStatus(final PaymentStatus paymentStatus) {
    DetachedCriteria criteria = DetachedCriteria.forClass(UserPayment.class);
    criteria.add(Restrictions.eq("status",paymentStatus));
    List<UserPayment> userPayments = findByCriteria(criteria);
    return userPayments.size() > 0 ? userPayments : Collections.emptyList();
  }
  
  
}
