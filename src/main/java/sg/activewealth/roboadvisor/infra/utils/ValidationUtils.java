package sg.activewealth.roboadvisor.infra.utils;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;

public class ValidationUtils {
	
	private Logger logger = Logger.getLogger(ValidationUtils.class);
		
	private static ValidationUtils me;

	public static ValidationUtils getInstance() {
		if (me == null) me = new ValidationUtils();

		return me;
	}

	public boolean isEmptyString(String string) {
		return (string == null || string.trim().length() == 0);
	}

	public boolean isInteger(String string) {
		try {
			Long.parseLong(string);
		}
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public boolean isValidEmailAddress(String emailAddress) {
		if (emailAddress == null || emailAddress.trim().length() == 0) {
			return false;
		}
		return EmailValidator.getInstance().isValid(emailAddress);
	}
	
	public boolean isValidMobileNumber(String mobileNumber) {
		if (mobileNumber != null && mobileNumber.trim().length() > 0) {
			/*take away length validation*/
//			if (mobileNumber.length() != 8) return false;
			try {
				Long.parseLong(mobileNumber);
			}
			catch (NumberFormatException e) {
				return false;
			}
			return true;
		}
		return false;
	}
	
}
