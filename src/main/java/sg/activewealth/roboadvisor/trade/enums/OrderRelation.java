package sg.activewealth.roboadvisor.trade.enums;

public enum OrderRelation {
	IFDONEMASTER("IfDoneMaster"),
	IFDONESLAVE("IfDoneSlave"),
	IFDONESLAVEOCO("IfDoneSlaveOco"),
	OCO("Oco"),
	STANDALONE("StandAlone");

	private String label;

	OrderRelation(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
