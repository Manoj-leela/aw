package sg.activewealth.roboadvisor.banking.helper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Source;
import com.stripe.model.Subscription;
import com.stripe.model.Token;

@Component
public class StripePaymentHelper {

	// TODO : Move this to config.properties
	public StripePaymentHelper() {
		Stripe.apiKey = "sk_test_l2LJgq5FqVIFuTmCrlpaMIXh";
	}

	public Customer addPaymentMethod(Customer customer, Map<String, Object> cardParamsMap)
			throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException,
			APIException {
		Map<String, Object> tokenParams = new HashMap<String, Object>();
		Map<String, Object> customerParamsMap = new LinkedHashMap<>();

		tokenParams.put("card", cardParamsMap);
		Token token = Token.create(tokenParams);

		customerParamsMap.put("source", token.getId());
		customer.getSources().create(customerParamsMap);
		Customer cust = Customer.retrieve(customer.getId());
		return cust;
	}

	/**
	 * User registration at stripe
	 * 
	 * @throws APIException
	 * @throws CardException
	 * @throws APIConnectionException
	 * @throws InvalidRequestException
	 * @throws AuthenticationException
	 */
	public Customer registerCustomer(String email) throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException {
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("email", email);
		return Customer.create(params);
	}

	public Subscription schedulePayment(String customerId) throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("customer", customerId);
		params.put("plan", "basic-monthly");

		return Subscription.create(params);
	}

	public Charge doPayment(String customerId, BigDecimal amount, String currency, String description)
			throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException,
			APIException {

		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("amount", amount);
		params.put("currency", currency);
		params.put("description", description);
		params.put("customer", customerId);
		return Charge.create(params);

	}

	/**
	 * Get stripe customer object from Future Wealth user
	 * 
	 * @param user
	 * @return
	 * @throws APIException
	 * @throws CardException
	 * @throws APIConnectionException
	 * @throws InvalidRequestException
	 * @throws AuthenticationException
	 */
	public Customer getCustomer(String customerId) throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException {
		return Customer.retrieve(customerId);
	}

	public Source createSourceForCustomer(String customerId, Map<String, Object> cardDetailsMap)
			throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException,
			APIException {
		Map<String, Object> sourceParams = new HashMap<String, Object>();
		sourceParams.put("card", cardDetailsMap);
		sourceParams.put("customer", customerId);
		sourceParams.put("type", "card");
		sourceParams.put("usage", "reusable");

		Source createdSource = Source.create(sourceParams);
		return createdSource;
	}

	public Customer updateDefaultSource(Customer customer, String cardId) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException {
		Map<String, Object> customerParams = new HashMap<String, Object>();
		Customer customerObj = Customer.retrieve(customer.getId());

		customerParams.put("default_source", cardId);
		
		customerObj.update(customerParams);
		return customerObj;

	}
}
