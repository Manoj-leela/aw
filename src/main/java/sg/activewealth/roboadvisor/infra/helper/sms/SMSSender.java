package sg.activewealth.roboadvisor.infra.helper.sms;


public interface SMSSender {

	String sendChallenge(final String mobileNumberInMSISDN);

	void verifyCode(final String token, final String otpCode);
	
	Boolean sendSms(final String mobileNumberInMSISDN,final String content);
}
