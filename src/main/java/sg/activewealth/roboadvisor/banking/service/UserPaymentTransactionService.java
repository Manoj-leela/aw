package sg.activewealth.roboadvisor.banking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.banking.dao.UserPaymentTransactionDao;
import sg.activewealth.roboadvisor.banking.model.UserPaymentTransaction;
import sg.activewealth.roboadvisor.infra.service.AbstractService;

@Service
public class UserPaymentTransactionService extends AbstractService<UserPaymentTransaction> {

	public UserPaymentTransactionService() {
		super(UserPaymentTransaction.class);
	}

	@Autowired
	public void setDao(UserPaymentTransactionDao dao) {
		super.dao = dao;
	}
	
	public List<UserPaymentTransaction> getByCreatedOnAndUser(LocalDateTime startDateTime,LocalDateTime endDateTime,String userId) {
		return ((UserPaymentTransactionDao) dao).getByCreatedOnAndUser(startDateTime, endDateTime,userId);
	}
}
