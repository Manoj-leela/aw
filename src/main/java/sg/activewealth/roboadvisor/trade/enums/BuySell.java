package sg.activewealth.roboadvisor.trade.enums;

public enum BuySell{
	Buy("Buy"), Sell("Sell");

	private String label;

	BuySell(String label) {
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
}
