package sg.activewealth.roboadvisor.infra.utils;

import org.apache.log4j.Logger;

public class NumberUtils {
	
	private Logger logger = Logger.getLogger(NumberUtils.class);
		
	private static NumberUtils me;

	public static NumberUtils getInstance() {
		if (me == null) me = new NumberUtils();

		return me;
	}

	public String buildDigitNumber(int number, int noOfDigits) {
		String numberStr = new Integer(number).toString();
		if (numberStr.length() == noOfDigits)
			return numberStr;
		else {
			StringBuffer ret = new StringBuffer();
			for (int i = 0; i < noOfDigits - numberStr.length(); i++) {
				ret.append("0");
			}
			ret.append(number);

			return ret.toString();
		}
	}

}
