package sg.activewealth.roboadvisor.banking.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.banking.enums.PaymentTransactionStatus;
import sg.activewealth.roboadvisor.banking.model.UserPayment;
import sg.activewealth.roboadvisor.banking.model.UserPaymentTransaction;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;

@Repository
public class UserPaymentTransactionDao extends AbstractDao<UserPaymentTransaction> {

	public void add(String description, PaymentTransactionStatus status, UserPayment userPayment,
			String createdBy) {
		UserPaymentTransaction userPaymentTransaction = new UserPaymentTransaction();
		userPaymentTransaction.setDescription(description);
		userPaymentTransaction.setStatus(status);
		userPaymentTransaction.setUserPayment(userPayment);
		userPaymentTransaction.setCreatedBy(createdBy);
		userPaymentTransaction.setCreatedOn(LocalDateTime.now());
		save(userPaymentTransaction);
	}

	public List<UserPaymentTransaction> getByCreatedOnAndUser(LocalDateTime startDateTime,LocalDateTime endDateTime,String userId) {
		List<UserPaymentTransaction> userPaymentTransactions = new ArrayList<>();
		DetachedCriteria criteria = DetachedCriteria.forClass(UserPaymentTransaction.class);
		criteria.createAlias("userPayment", "userPayment");
		criteria.add(Restrictions.eq("userPayment.user.id", userId));
		criteria.add(Restrictions.ge("createdOn", startDateTime));
		criteria.add(Restrictions.le("createdOn", endDateTime));
		userPaymentTransactions = findByCriteria(criteria,true);
		return userPaymentTransactions;
	}
}
