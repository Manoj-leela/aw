package sg.activewealth.roboadvisor.infra.utils;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;

public class FormattingUtils {
	
	private Logger logger = Logger.getLogger(FormattingUtils.class);
		
	private static FormattingUtils me;

	private DecimalFormat decimalFormat, decimalFormatODP, rewardFormat;

	public FormattingUtils() {
		decimalFormat = new DecimalFormat("#0.00");
		decimalFormatODP = new DecimalFormat("#0.0");
		rewardFormat = new DecimalFormat("#000000");
	}
	
	public static FormattingUtils getInstance() {
		if (me == null) me = new FormattingUtils();

		return me;
	}

	// odp : one decimal place
	public String formatDecimalODP(Double value) {
		if (value != null)
			return decimalFormatODP.format(value);
		return "0.0";
	}

	public String formatDecimal(Double value) {
		if (value != null)
			return decimalFormat.format(value);
		return "0.00";
	}
	
	public String formatIntegerForRewardSerialNumber(Integer value) {
		if (value != null)
			return rewardFormat.format(value);
		return "";
	}

}
