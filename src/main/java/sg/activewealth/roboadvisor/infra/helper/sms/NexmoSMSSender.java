package sg.activewealth.roboadvisor.infra.helper.sms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.infra.exception.OTPNotMatchingException;
import sg.activewealth.roboadvisor.infra.exception.SystemException;
import sg.activewealth.roboadvisor.infra.helper.HttpHelper;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NexmoSMSSender implements SMSSender{

  private Logger logger = Logger.getLogger(NexmoSMSSender.class);

  private static final String NEXMO_API_URL = "https://api.nexmo.com";
  private static final String SEND_SMS_REST_JSON_URL = "https://rest.nexmo.com/sms/json";

  @Autowired
  private PropertiesHelper propertiesHelper;

  @Autowired
  private HttpHelper httpHelper;


  @Override
  public String sendChallenge(final String mobileNumberInMSISDN)  {

    //Check whether SMS service is enabled or not
    if (!propertiesHelper.appIsSendNotification) {
      logger.warn("Sending SMS is disabled, check the conf.app.isSendNotification setting");
      return null;
    }
    logger.info("sending OTP to number: " + mobileNumberInMSISDN);

    String jsonResponse = new String(httpHelper.postContent(NEXMO_API_URL + "/verify/json",
                                                   "api_key", propertiesHelper.nexmoKey,
                                                   "api_secret", propertiesHelper.nexmoSecret,
                                                   "brand", propertiesHelper.appNameStylizedShort,
                                                   "sender_id", propertiesHelper.appNameStylizedShort,
                                                   "code_length", "4",
                                                   "number", mobileNumberInMSISDN));

    logger.info("sent challenge: " + jsonResponse);

    final Map<String, String> props;
    try {
      props = new ObjectMapper().readValue(jsonResponse, new TypeReference<HashMap<String,String>>() {});
    } catch (IOException e) {
      throw new SystemException(e.getMessage(),e);
    }
    if (!"0".equals(props.get("status")))
      throw new SystemException("Error while sending challenge to Nexmo : Status code ="+props.get("status"));
    else { //success
      return  props.get("request_id");
    }
  }


  @Override
  public void verifyCode(final String token, final String otpCode) {

    logger.info("verifying challenge: " + token);
    if (!propertiesHelper.appIsSendNotification) {
      logger.warn("Sending SMS is disabled, check the conf.app.isSendNotification setting");
      return;
    }

    String jsonResponse = new String(httpHelper.postContent(NEXMO_API_URL + "/verify/check/json",
                                                   "api_key", propertiesHelper.nexmoKey,
                                                   "api_secret", propertiesHelper.nexmoSecret,
                                                   "request_id", token,
                                                   "code", otpCode));

    logger.info("verified challenge: " + jsonResponse);

    final Map<String, String> props;
    try {
      props = new ObjectMapper().readValue(jsonResponse, new TypeReference<HashMap<String,String>>() {});
    } catch (IOException e) {
      throw new SystemException(e.getMessage(),e);
    }
    final String responseStatus =props.get("status");
    if ("0".equals(responseStatus)) {
      return;
    }
    else if("16".equals(responseStatus)) {
      throw new OTPNotMatchingException();
    }
    else {
      //https://docs.nexmo.com/verify/api-reference/api-reference#cresponse
      throw new SystemException("Error while verifying OTP challenge : Status code = "+responseStatus);
    }
  }


  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Boolean sendSms(String mobileNumberInMSISDN,String content) {
	  
	//Check whether SMS service is enabled or not
    if (!propertiesHelper.appIsSendNotification) {
      logger.warn("Sending SMS is disabled, check the conf.app.isSendNotification setting");
      return false;
    }
    logger.info("sending OTP to number: " + mobileNumberInMSISDN);

    String jsonResponse = new String(httpHelper.postContent(SEND_SMS_REST_JSON_URL,
                                                   "api_key", propertiesHelper.nexmoKey,
                                                   "api_secret", propertiesHelper.nexmoSecret,
                                                   "from", propertiesHelper.smsFromNumber,
                                                   "to", mobileNumberInMSISDN,
                                                   "text", content));

    logger.info("Sent Message: " + jsonResponse);

    final Map<String, Object> props;
    try {
      props = new ObjectMapper().readValue(jsonResponse, new TypeReference<HashMap<String,Object>>() {});
      logger.info("Response of Message: " + props);
    } catch (IOException e) {
    	logger.error("Error while sending sms to Nexmo : Status code ="+e.getMessage());
    	return false;
    }
	List list = (List) props.get("messages");
	Map<String,Object> map = new HashMap<>();
	if(list.size() >0 ){
		 map = (Map<String, Object>) list.get(0);
	}
    if (!"0".equals(map.get("status"))){
    	logger.error("Error while sending sms to Nexmo : Status code ="+props.get("status"));
    	return false;
    } else { //success
    	logger.info("SMS sent successfully");
    	return  true;
    }
  }
}
