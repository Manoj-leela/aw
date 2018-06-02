package sg.activewealth.roboadvisor.banking.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FundingStatus implements ByteEnum {

	NotSubmitted(0, "Not Submitted", "Not Submitted Description"), Submitted(1, "Submitted","Submitted Description"), 
	AwaitingReconciliation(2, "Awaiting Reconciliation","Awaiting Reconciliation Description"), 
	DidNotReceive(3, "Did not Receive", "Did Not Receive Description"), Funded(4, "Funded","Funded Description"), 
	Completed(5, "Completed", "Completed Description");

	private byte value;
	private String label;
	private String description;

	FundingStatus(int value, String label, String description) {
		this.value = (byte) value;
		this.label = label;
		this.description = description;
		
	}

	@JsonCreator
	public static FundingStatus getStatusByLabel(String label) {
		FundingStatus fundingStatus = null;
		for (FundingStatus fundingStatus2 : FundingStatus.values()) {
			if (fundingStatus2.getLabel().equalsIgnoreCase(label)) {
				fundingStatus = fundingStatus2;
				break;
			}
		}
		return fundingStatus;
	}

	public static FundingStatus get(int value) {
		FundingStatus fundingStatus = null;
		for (FundingStatus fundingStatus2 : FundingStatus.values()) {
			if (fundingStatus2.getValue() == value) {
				fundingStatus = fundingStatus2;
				break;
			}
		}
		return fundingStatus;
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

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(byte value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
