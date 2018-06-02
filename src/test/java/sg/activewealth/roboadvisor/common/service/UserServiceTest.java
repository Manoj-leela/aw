package sg.activewealth.roboadvisor.common.service;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
import com.stripe.model.Source;
import com.stripe.model.Token;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sg.activewealth.roboadvisor.banking.helper.StripePaymentHelper;
import sg.activewealth.roboadvisor.common.enums.BankDetailsStatus;
import sg.activewealth.roboadvisor.common.enums.KycStatus;
import sg.activewealth.roboadvisor.common.enums.ResidenceCountry;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.infra.utils.SystemUtils;
import sg.activewealth.roboadvisor.portfolio.enums.UserRiskProfile;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.service.PortfolioService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
    locations = {"classpath:test-appContext-infra.xml", "classpath:test-webContext-infra.xml"})
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserPaymentMethodService userPaymentMethodService;
  
  @Autowired
  private StripePaymentHelper stripePaymentHelper;
  
  @Autowired
  private PortfolioService portfolioService;
  
  @Test
  public void makePaymentTest() {
	  try {
		  Charge charge = stripePaymentHelper.doPayment("cus_BAr0vXXpQK732l", new BigDecimal("50.00").setScale(0), "usd", "Amount Transferred to RoboAdviser Test");
		  System.out.println(charge);
	      } catch (AuthenticationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (InvalidRequestException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (APIConnectionException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (CardException e) {
	    	  System.out.println("Status is: " + e.getCode());
	    	  System.out.println("Message is: " + e.getMessage());
	      } catch (APIException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
  }
  
  @Test
  public void setTokenForStripeCustomer(){
	 
	  try {
		  Customer cu = Customer.retrieve("cus_Aso6K4kC2FNYHN");
		  Map<String, Object> updateParams = new HashMap<String, Object>();
		  updateParams.put("source", "tok_1Ak6dm2eZvKYlo2CmcLKBdMn");
		  cu.update(updateParams);
	      } catch (AuthenticationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (InvalidRequestException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (APIConnectionException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (CardException e) {
	    	  System.out.println("Status is: " + e.getCode());
	    	  System.out.println("Message is: " + e.getMessage());
	      } catch (APIException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
  }


  
  
  
  @Test
  public void saveuser() {
    for (Integer cnt = 0; cnt <= 1; cnt++) {
      User user = new User();
      user.setFirstName("FNAME" + cnt);
      user.setLastName("LNAME" + cnt);
      user.setEmail("fname.lname."+ RandomStringUtils.random(2,true,true) + cnt + "@gmail.com");
      user.setPassword("Welcome123#");
      user.setRepassword("Welcome123#");
      user.setIsAdmin(false);
      user.setMobileNumber("7718"+RandomStringUtils.randomNumeric(5));
      user.setVersion(cnt);
      user.setResidenceCountry(ResidenceCountry.SINGAPORE);
      user.setAnnualIncome(BigDecimal.TEN);
      userService.save(user);
    }
  }

	@Test
	public void testEmailNotification() {
		User user = new User();
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("apatel"+RandomStringUtils.random(5,true,true)+"@staunchsys.co.in");
		user.setPassword("Welcome123#");
		user.setRepassword("Welcome123#");
		user.setIsAdmin(false);
		user.setMobileNumber("7718266"+RandomStringUtils.randomNumeric(3));
		user.setResidenceCountry(ResidenceCountry.SINGAPORE);
		user.setAnnualIncome(BigDecimal.TEN);

		userService.save(user);

		user.setKycStatus(KycStatus.AwaitingApproval);
		userService.save(user);

		user.setBankDetailsStatus(BankDetailsStatus.AwaitingApproval);
		userService.save(user);

		user.setKycStatus(KycStatus.SubmissionIssues);
		userService.save(user);

		user.setKycStatus(KycStatus.Completed);
		userService.save(user);

        user.setBankDetailsStatus(BankDetailsStatus.Completed);
        userService.save(user);

	}
  
  @Test
  public void createCardToken() throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
	  Stripe.apiKey = "sk_test_l2LJgq5FqVIFuTmCrlpaMIXh";

	  Map<String, Object> tokenParams = new HashMap<String, Object>();
	  Map<String, Object> cardParams = new HashMap<String, Object>();
	  cardParams.put("number", "4242424242424242");
	  cardParams.put("exp_month", 7);
	  cardParams.put("exp_year", 2018);
	  cardParams.put("cvc", "314");
	  cardParams.put("customer", "cus_Aso6K4kC2FNYHN");
	  tokenParams.put("card", cardParams);

	  Token createdToken = Token.create(tokenParams);
	  System.out.println(createdToken);
  }
  
  @Test
  public void createBankToken(){
	  Stripe.apiKey = "sk_test_BQokikJOvBiI2HlWgH4olfQ2";
  }
  
  @Test
  public void dopayment(){
	  Stripe.apiKey = "sk_test_l2LJgq5FqVIFuTmCrlpaMIXh";
	  
	  Map<String, Object> params = new HashMap<String, Object>(4);
      params.put("amount", new BigDecimal("50"));
      params.put("currency", "usd");
      params.put("description", "Amount Transferred to RoboAdviser Test");
      params.put("customer", "cus_B6izmlX6THRXsI");
      
      try{
    	  Charge charge = Charge.create(params);
    	  System.out.println(charge);
      } catch (AuthenticationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (InvalidRequestException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (APIConnectionException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (CardException e) {
	    	  System.out.println("Status is: " + e.getCode());
	    	  System.out.println("Message is: " + e.getMessage());
	      } catch (APIException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
       
  }
  
  @Test 
  public void getStripeCustomerByCustId(){
	  Stripe.apiKey = "sk_test_l2LJgq5FqVIFuTmCrlpaMIXh";
	  
	  try{
		  Customer customer = Customer.retrieve("cus_BAr0vXXpQK732l");
    	  System.out.println(customer);
      } catch (AuthenticationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (InvalidRequestException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (APIConnectionException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (CardException e) {
	    	  System.out.println("Status is: " + e.getCode());
	    	  System.out.println("Message is: " + e.getMessage());
	      } catch (APIException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
	  
  }
  
  @Test 
  public void getStripeTokenByTokenId(){
	  Stripe.apiKey = "sk_test_l2LJgq5FqVIFuTmCrlpaMIXh";
	  
	  try{
		  Token token = Token.retrieve("tok_1Ak93PAvQJGeO6wNqTnwC1ce");
    	  System.out.println(token);
      } catch (AuthenticationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (InvalidRequestException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (APIConnectionException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (CardException e) {
	    	  System.out.println("Status is: " + e.getCode());
	    	  System.out.println("Message is: " + e.getMessage());
	      } catch (APIException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } 
  }
  
  @Test
  public void updateCustomerCard(){
	  Stripe.apiKey = "sk_test_l2LJgq5FqVIFuTmCrlpaMIXh";
	  
	  Map<String, Object> params = new HashMap<String, Object>();
	  params.put("source", "tok_1Ak93PAvQJGeO6wNqTnwC1ce");
	  
	  try{
		  Customer customer = Customer.retrieve("cus_Aso6K4kC2FNYHN");
		  ExternalAccount externalAccount = customer.getSources().create(params);
		  System.out.println(externalAccount);
      } catch (AuthenticationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (InvalidRequestException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (APIConnectionException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (CardException e) {
	    	  System.out.println("Status is: " + e.getCode());
	    	  System.out.println("Message is: " + e.getMessage());
	      } catch (APIException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
	  
  }
  
  @Test 
  public void createSourceForPayment() throws AuthenticationException, InvalidRequestException, 
  							APIConnectionException, CardException, APIException{
	  Stripe.apiKey = "sk_test_l2LJgq5FqVIFuTmCrlpaMIXh";
	  
	  Map<String, Object> cardParams = new HashMap<String, Object>();
	  Map<String, Object> sourceParams = new HashMap<String, Object>();
			  
	  cardParams.put("number", "4242424242424242");
	  cardParams.put("exp_month", 7);
	  cardParams.put("exp_year", 2018);
	  cardParams.put("cvc", "314");
	  sourceParams.put("card", cardParams);
//	  sourceParams.put("customer", "cus_Aso6K4kC2FNYHN");
	  
	  sourceParams.put("type", "card");
	  sourceParams.put("usage","reusable");
	  
	  
	  try{
		  Source createdSource = Source.create(sourceParams);
		  System.out.println(createdSource);
      } catch (AuthenticationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (InvalidRequestException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (APIConnectionException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      } catch (CardException e) {
	    	  System.out.println("Status is: " + e.getCode());
	    	  System.out.println("Message is: " + e.getMessage());
	      } catch (APIException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
	  
  }
  
  /*This method is used to update the default source by the source of the choice from the customer Object.*/
  @Test
  public void updateCustomer() throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
	  Stripe.apiKey = "sk_test_l2LJgq5FqVIFuTmCrlpaMIXh";
	  
	  Map<String, Object> sourceParams = new HashMap<String, Object>();
	  Customer customer = Customer.retrieve("cus_Aso6K4kC2FNYHN");
	  
	  sourceParams.put("default_source", customer.getSources().getData().get(0).getId());
	  
	  customer.update(sourceParams);
  }
  
  @Test
  public void getPorfolioByRiskProfile(){
	  Portfolio portfolio = portfolioService.retriveByRiskProfile(UserRiskProfile.Aggressive,true).get(0);
	  System.out.println(portfolio);
  }
  
  @Test
	public void generateRandomValue() {
		for (int i = 0; i < 20; i++) {
			System.out.println(SystemUtils.getInstance().getRandomRiskProfile().toString());
		}
	}
  
  @Test
  public void updateCardParams() throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
	  Stripe.apiKey = "sk_test_l2LJgq5FqVIFuTmCrlpaMIXh";
	  
	  Customer customer = Customer.retrieve("cus_Aso4MMlkIb1OyN");
	  for(int i = 0;i<customer.getSources().getData().size();i++){
		  String s = customer.getSources().getData().get(0).getId();
		  System.out.println(s);
	  }
  }
  
  @Test 
  public void addNumberOfCards() throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
	  Stripe.apiKey = "sk_test_l2LJgq5FqVIFuTmCrlpaMIXh";
	  
	  Map<String, Object> cardParams = new HashMap<String, Object>();
//	  Map<String, Object> customerParams = new HashMap<String, Object>(1);
	  Map<String, Object> params = new HashMap<String, Object>();
	  Map<String, Object> tokenParams = new HashMap<String, Object>();
	  
//	  customerParams.put("email", "puravndoshi123@abc.com");
//	  Customer customer = Customer.create(customerParams);
	  
	  Customer customer = Customer.retrieve("cus_B9i9OOLsqo03ps");
	  
	  cardParams.put("number", "4242424242424242");
	  cardParams.put("exp_month", 7);
	  cardParams.put("exp_year", 2018);
	  cardParams.put("cvc", "314");
	  
	  tokenParams.put("card", cardParams);

	  Token createdToken = Token.create(tokenParams);
	  System.out.println(createdToken);
	  
	  params.put("source", createdToken.getId());
	  customer.getSources().create(params);
	  
	  Customer customer1 = Customer.retrieve(customer.getId());
	  System.out.println(customer1);
  }
}

