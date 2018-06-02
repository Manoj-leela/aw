package sg.activewealth.roboadvisor.infra.helper.broker;

import sg.activewealth.roboadvisor.portfolio.model.Instrument;

public class InteractiveBrokerUtil {

	private static InteractiveBrokerUtil me;

	public static InteractiveBrokerUtil getInstance() {
		if (me == null)
			me = new InteractiveBrokerUtil();

		return me;
	}

	public String getExchange() {
		return "SMART";
	}
	
	public String getExchange(Instrument instrument) {
		String exchange = "";
		switch (instrument.getCode()) {
		case "VOO":
			exchange = "ARCA";
			break;
		case "EZU":
			exchange = "BATS";
			break;
		case "EWJ":
			exchange = "ARCA";
			break;
		case "AAXJ":
			exchange = "NYSE";
			break;	
		case "IXC":
			exchange = "ARCA";
			break;
		case "EMB":
			exchange = "NYSE";
			break;
		case "RING":
			exchange = "NYSE";
			break;	
		default:
			exchange = "NYSE";
			break;
		}
		return exchange;
	}

	public String getCurreny(Instrument instrument) {
		return "USD";
	}

}
