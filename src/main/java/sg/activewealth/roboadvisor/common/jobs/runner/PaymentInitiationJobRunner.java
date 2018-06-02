package sg.activewealth.roboadvisor.common.jobs.runner;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stripe.model.Charge;

import sg.activewealth.roboadvisor.banking.enums.PaymentStatus;
import sg.activewealth.roboadvisor.banking.helper.StripePaymentHelper;
import sg.activewealth.roboadvisor.banking.model.UserPayment;
import sg.activewealth.roboadvisor.banking.service.UserPaymentService;
import sg.activewealth.roboadvisor.common.model.User;

@Component
public class PaymentInitiationJobRunner {

  private static final String STRIPE_PAYMENT_SUCCEEDED = "succeeded";
  private Logger logger = Logger.getLogger(PaymentInitiationJobRunner.class);

  @Autowired
  private StripePaymentHelper stripePaymentHelper;

  @Autowired
  private UserPaymentService userPaymentService;

  public void initiatePayment(final PaymentStatus paymentStatus) {
	  /*
    //TODO : Need to remove Hardcoded Description and Currency
    final String description = "Amount Transferred to RoboAdviser";
    final String currency = "usd";

    //Get all the User Payments whose stats is ReadyForPayment.
    //TODO:Should we have batch size here?
    final List<UserPayment> userPayments = userPaymentService.getUserPaymentsByStatus(paymentStatus);

    //Process each Payment, If any Payment fails continue with next Payment.
    for (UserPayment userPayment : userPayments) {
      try{
    	  User user = userPayment.getPaymentMethod().getUser();
          //Customer Id must be there to do Stripe Payment.
          String customerId = user.getCustomerId();
          if(StringUtils.isBlank(customerId))
            throw new IllegalStateException("CustomerId is expected to be not null here for userPaymentId = "+userPayment.getId());

          //Set the stats PaymentInProgress before starting the actual Payment process.
          userPayment.setStatus(PaymentStatus.PaymentInProgress);
          userPayment = userPaymentService.save(userPayment);
          // Do Stripe Payment
          final Charge charge = stripePaymentHelper.doPayment(customerId, userPayment.getAmount().setScale(0), currency, description);
          final String status = charge.getStatus();
          if(STRIPE_PAYMENT_SUCCEEDED.equals(status)){
            userPayment.setStatus(PaymentStatus.PaymentCompleted);
          }else {
            markUserPaymentAttemptAsFailure(userPayment);
          }
          userPaymentService.save(userPayment);
      }
      catch(Exception e){
          logger.error("Error occurred while processing Stripe Payment", e);
          markUserPaymentAttemptAsFailure(userPayment);
          userPaymentService.save(userPayment);
      }
    }*/
  }

  private void markUserPaymentAttemptAsFailure(final UserPayment userPayment) {
    userPayment.setStatus(PaymentStatus.PaymentError);
    userPayment.incrementRetryCount();
  }
}
