package sg.activewealth.roboadvisor.banking.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RedemptionStatus implements ByteEnum {
	
	RequestedByInvestor(0, "Requested By Investor"), 
	SentToBroker(1, "Sent To Broker"), 
	ReceivedFromBroker(2,"Received From Broker"),  
	Completed(3, "Completed"),
	Issues(4, "Issues");

	private byte value;
	private String label;
	
	private RedemptionStatus(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}
	
	@JsonCreator
	public static RedemptionStatus get(int value) {
		RedemptionStatus redemptionStatus = null;
		for (RedemptionStatus redemptionStatus2 : RedemptionStatus.values()) {
			if (redemptionStatus2.getValue() == value) {
				redemptionStatus = redemptionStatus2;
				break;
			}
		}
		return redemptionStatus;
	}
	
	public static RedemptionStatus get(String label) {
		RedemptionStatus redemptionStatus = null;
		for (RedemptionStatus redemptionStatus2 : RedemptionStatus.values()) {
			if (redemptionStatus2.getLabel().equals(label)) {
				redemptionStatus = redemptionStatus2;
				break;
			}
		}
		return redemptionStatus;
	}
	
	public static RedemptionStatus fromString(String strRedemptionStatus) {
		RedemptionStatus redemptionStatus = null;
		for (RedemptionStatus redemptionStatus2 : RedemptionStatus.values()) {
			if (redemptionStatus2.name().equalsIgnoreCase(strRedemptionStatus)) {
				redemptionStatus = redemptionStatus2;
				break;
			}
		}
		return redemptionStatus;
	}
	
	@Override
	public byte getValue() {
		return value;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

}
