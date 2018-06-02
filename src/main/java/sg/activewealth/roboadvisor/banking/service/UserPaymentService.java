package sg.activewealth.roboadvisor.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.banking.dao.UserPaymentDao;
import sg.activewealth.roboadvisor.banking.dao.UserPaymentTransactionDao;
import sg.activewealth.roboadvisor.banking.enums.PaymentStatus;
import sg.activewealth.roboadvisor.banking.model.UserPayment;
import sg.activewealth.roboadvisor.common.dao.UserDao;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.service.AbstractService;

import java.util.List;

@Service
public class UserPaymentService extends AbstractService<UserPayment> {

  public UserPaymentService() {
    super(UserPayment.class);
  }

  public UserPaymentService(Class<UserPayment> modelClass) {
    super(modelClass);
  }

  @Autowired
  public void setDao(UserPaymentDao dao) {
    super.dao = dao;
  }
  
  @Autowired
  UserService userService;
  
  @Autowired
  private UserPaymentTransactionDao userPaymentTransactionDao;

  @Autowired
  private UserDao userDao;

  //TODO : Need to remove Hardcoded Description 
  @Override
  public UserPayment postSave(final UserPayment model) {
    if(model.getStatus() == PaymentStatus.PaymentCompleted){
      final User user = model.getUser();
      user.addToAccountSummary(model.getAmount());
      userDao.save(user);
    }
    userPaymentTransactionDao.add("description", PaymentStatus.getPaymentTransactionStatus(model.getStatus()), model, userService.getSystemUser().getId());
    return super.postSave(model);
  }
  
  public List<UserPayment> getUserPaymentsByStatus(final PaymentStatus paymentStatus){
    return ((UserPaymentDao) dao).retrieveByPaymentStatus(paymentStatus);
  }
}
