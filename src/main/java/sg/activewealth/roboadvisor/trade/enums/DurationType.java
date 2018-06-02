package sg.activewealth.roboadvisor.trade.enums;

public enum DurationType {
	AtTheClose("AtTheClose"),
	AtTheOpening("AtTheOpening"),	
	DayOrder("DayOrder"),	
	FillOrKill("FillOrKill"),	
	GoodForPeriod("GoodForPeriod"),	
	GoodTillCancel("GoodTillCancel"),	
	GoodTillDate("GoodTillDate"),	
	ImmediateOrCancel("ImmediateOrCancel");

	private String label;

	DurationType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
