package sg.activewealth.roboadvisor.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AgentOTPStatus implements ByteEnum {

	NotYetSent(0, "Not Yet Sent"), SentToAgent(1, "Sent to Agent"), Completed(2, "Completed"), OtpVerificationFailed(3,
			"OTP Verification Failed"),  NotCompleted (4, "Not Completed");

	private byte value;
	private String label;

	AgentOTPStatus(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}

	public static AgentOTPStatus get(String label) {
		AgentOTPStatus agentOTPStatus = null;
		for (AgentOTPStatus agentOTPStatus2 : AgentOTPStatus.values()) {
			if (agentOTPStatus2.getLabel().equals(label)) {
				agentOTPStatus = agentOTPStatus2;
				break;
			}
		}
		return agentOTPStatus;
	}

	@JsonCreator
	public static AgentOTPStatus get(int value) {
		AgentOTPStatus agentOTPStatus = null;
		for (AgentOTPStatus agentOTPStatus2 : AgentOTPStatus.values()) {
			if (agentOTPStatus2.getValue() == value) {
				agentOTPStatus = agentOTPStatus2;
				break;
			}
		}
		return agentOTPStatus;
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
