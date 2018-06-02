package sg.activewealth.roboadvisor.banking.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

/**
 * The Enum InvestorRemittance.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum InvestorRemittanceStatus implements ByteEnum {

	/** The submitted. */
	Submitted(0, "Submitted"),

	/** The received. */
	Received(1, "Received"),

	/** The issues. */
	Issues(2, "Issues");

	/** The value. */
	private byte value;

	/** The label. */
	private String label;

	/**
	 * Instantiates a new investor remittance.
	 *
	 * @param value
	 *            the value
	 * @param label
	 *            the label
	 */
	private InvestorRemittanceStatus(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}

	/**
	 * Gets the.
	 *
	 * @param value
	 *            the value
	 * @return the investor remittance
	 */
	@JsonCreator
	public static InvestorRemittanceStatus get(int value) {
		InvestorRemittanceStatus investorRemittance = null;
		for (InvestorRemittanceStatus investorRemittance2 : InvestorRemittanceStatus.values()) {
			if (investorRemittance2.getValue() == value) {
				investorRemittance = investorRemittance2;
				break;
			}
		}
		return investorRemittance;
	}

	/**
	 * Gets the.
	 *
	 * @param label
	 *            the label
	 * @return the investor remittance
	 */
	public static InvestorRemittanceStatus get(String label) {
		InvestorRemittanceStatus investorRemittance = null;
		for (InvestorRemittanceStatus investorRemittance2 : InvestorRemittanceStatus.values()) {
			if (investorRemittance2.getLabel().equals(label)) {
				investorRemittance = investorRemittance2;
				break;
			}
		}
		return investorRemittance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sg.activewealth.roboadvisor.infra.enums.ByteEnum#getValue()
	 */
	@Override
	public byte getValue() {
		return value;
	}

	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	public static InvestorRemittanceStatus fromString(String strInvestoreRemittance) {
		InvestorRemittanceStatus investorRemittance = null;
		for (InvestorRemittanceStatus investorRemittance2 : InvestorRemittanceStatus.values()) {
			if (investorRemittance2.name().equalsIgnoreCase(strInvestoreRemittance)) {
				investorRemittance = investorRemittance2;
				break;
			}
		}
		return investorRemittance;
	}
}
