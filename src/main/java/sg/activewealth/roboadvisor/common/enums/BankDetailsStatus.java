package sg.activewealth.roboadvisor.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BankDetailsStatus implements ByteEnum {

	NotSubmitted(0, "Not Submitted"), Submitted(1, "Submitted"), AwaitingApproval(2,
			"Awaiting Approval"), SubmissionIssues(3,
					"Submission Issues"), Completed(4, "Completed"), Rejected(5, "Rejected");

	private byte value;
	private String label;

	private BankDetailsStatus(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}

	@JsonCreator
	public static BankDetailsStatus get(int value) {
		BankDetailsStatus bankDetailsStatus = null;
		for (BankDetailsStatus bankDetailsStatus2 : BankDetailsStatus.values()) {
			if (bankDetailsStatus2.getValue() == value) {
				bankDetailsStatus = bankDetailsStatus2;
				break;
			}
		}
		return bankDetailsStatus;
	}
	
	public static BankDetailsStatus get(String label) {
		BankDetailsStatus bankDetailsStatus = null;
		for (BankDetailsStatus bankDetailsStatus2 : BankDetailsStatus.values()) {
			if (bankDetailsStatus2.getLabel().equalsIgnoreCase(label)) {
				bankDetailsStatus = bankDetailsStatus2;
				break;
			}
		}
		return bankDetailsStatus;
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

}
