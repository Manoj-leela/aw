package sg.activewealth.roboadvisor.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum KycStatus implements ByteEnum {

	NotSubmitted(0, "Not Submitted"), Submitted(1, "Submitted"), AwaitingApproval(2,
			"Awaiting Approval"), SubmissionIssues(3,
					"Submission Issues"), Completed(4, "Completed"), Rejected(5, "Rejected");

	private byte value;
	private String label;

	private KycStatus(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}

	@JsonCreator
	public static KycStatus get(int value) {
		KycStatus kycStatus = null;
		for (KycStatus kycStatus2 : KycStatus.values()) {
			if (kycStatus2.getValue() == value) {
				kycStatus = kycStatus2;
				break;
			}
		}
		return kycStatus;
	}
	
	public static KycStatus get(String label) {
		KycStatus kycStatus = null;
		for (KycStatus kycStatus2 : KycStatus.values()) {
			if (kycStatus2.getLabel().equalsIgnoreCase(label)) {
				kycStatus = kycStatus2;
				break;
			}
		}
		return kycStatus;
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
