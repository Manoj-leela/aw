package sg.activewealth.roboadvisor.infra.utils;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

public class RoundingUtils {
	
	private Logger logger = Logger.getLogger(RoundingUtils.class);
		
	private static RoundingUtils me;

	public static RoundingUtils getInstance() {
		if (me == null) me = new RoundingUtils();

		return me;
	}

	public Double roundUpDoubleTo0DP(Double dub) {
		dub = new BigDecimal(dub).setScale(0, BigDecimal.ROUND_UP).doubleValue();
		return dub;
	}

	public Double roundUpDoubleTo2DP(Double dub) {
		dub = new BigDecimal(dub * 100).setScale(0, BigDecimal.ROUND_UP).doubleValue();
		dub = dub / 100;
		return dub;
	}

	public Double roundUpDoubleTo0DPNearest1Dollar(Double dub) {
		Double ret = roundUpDoubleTo0DP(dub);
		Double ROUNDUP_FACTOR = 1.00;
		//multiply by 20 to work with integer division
		BigDecimal res = new BigDecimal((ret*20) / (ROUNDUP_FACTOR*20));
		return new Double(FormattingUtils.getInstance().formatDecimal(Math.ceil(res.doubleValue()) * ROUNDUP_FACTOR));
	}
	
	public Double roundUpDoubleTo2DPNearest5Cents(Double dub) {
		Double ret = roundUpDoubleTo2DP(dub);
		Double ROUNDUP_FACTOR = 0.05;
		//multiply by 20 to work with integer division
		BigDecimal res = new BigDecimal((ret*20) / (ROUNDUP_FACTOR*20));
		return new Double(FormattingUtils.getInstance().formatDecimal(Math.ceil(res.doubleValue()) * ROUNDUP_FACTOR));
	}
	
	public Double roundDownDoubleTo2DPNearest5Cents(Double dub) {
		Double ret = roundDownDoubleTo2DP(dub);
		Double ROUNDUP_FACTOR = 0.05;
		//multiply by 20 to work with integer division
		BigDecimal res = new BigDecimal((ret*20) / (ROUNDUP_FACTOR*20));
		return new Double(FormattingUtils.getInstance().formatDecimal(Math.floor(res.doubleValue()) * ROUNDUP_FACTOR));
	}

	public Double roundDownDoubleTo2DP(Double dub) {
		dub = new BigDecimal(dub * 100).setScale(0, BigDecimal.ROUND_DOWN).doubleValue();
		dub = dub / 100;
		return dub;
	}

}
