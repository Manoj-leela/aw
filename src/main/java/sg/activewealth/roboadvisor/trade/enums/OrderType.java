package sg.activewealth.roboadvisor.trade.enums;

public enum OrderType {
	ALGORITHMIC("Algorithmic"),	
	GUARANTEEDSTOP("GuaranteedStop"),	
	LIMIT("Limit"),	
	MARKET("Market"),	
	STOPIFBID("StopIfBid"),	
	STOPIFOFFERED("StopIfOffered"),	
	STOPIFTRADED("StopIfTraded"),	
	STOPLIMIT("StopLimit"),	
	SWITCH("Switch"),	
	TRAILINGSTOP("TrailingStop"),	
	TRAILINGSTOPIFBID("TrailingStopIfBid"),	
	TRAILINGSTOPIFOFFERED("TrailingStopIfOffered"),	
	TRAILINGSTOPIFTRADED("TrailingStopIfTraded"),	
	TRASPASO("Traspaso");

	private String label;

	OrderType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
