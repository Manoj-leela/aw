package sg.activewealth.roboadvisor.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.common.dao.UserPaymentMethodDao;
import sg.activewealth.roboadvisor.common.model.UserPaymentMethod;
import sg.activewealth.roboadvisor.infra.service.AbstractService;

@Service
public class UserPaymentMethodService extends AbstractService<UserPaymentMethod> {

	public UserPaymentMethodService() {
		super(UserPaymentMethod.class);
	}

	@Autowired
	public void setDao(UserPaymentMethodDao dao) {
		super.dao = dao;
	}
	
	public List<UserPaymentMethod> retrieveByUserId(String userId){
		return ((UserPaymentMethodDao) dao).retrieveByUserId(userId);
	}
}
