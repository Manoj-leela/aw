package sg.activewealth.roboadvisor.common.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sg.activewealth.roboadvisor.banking.enums.PaymentStatus;
import sg.activewealth.roboadvisor.banking.model.UserPayment;
import sg.activewealth.roboadvisor.banking.service.UserPaymentService;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.model.UserPaymentMethod;
import sg.activewealth.roboadvisor.common.service.UserPaymentMethodService;
import sg.activewealth.roboadvisor.common.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        locations = {"classpath:test-appContext-infra.xml", "classpath:test-webContext-infra.xml"})
public class UserPaymentTest {
  
  @Autowired
  private UserService userService;
  
  @Autowired
  private UserPaymentService userPaymentService;
  
  @Autowired
  private UserPaymentMethodService userPaymentMethodService;

  User user = null;
  
  @Test
  public void addUserPayment(){
	user = userService.retrieve("40287e815cb170a2015cb17191c70000");  
    UserPayment userPayment = new UserPayment();
    userPayment.setAmount(new BigDecimal(100));
    userPayment.setStatus(PaymentStatus.ReadyForPayment);
    userPayment.setUser(user);
    UserPaymentMethod userPaymentMethod = userPaymentMethodService.retrieveByUserId("40287e815cb170a2015cb17191c70000").get(0); 
    userPayment.setPaymentMethod(userPaymentMethod);
    userPaymentService.save(userPayment);
  }

}
