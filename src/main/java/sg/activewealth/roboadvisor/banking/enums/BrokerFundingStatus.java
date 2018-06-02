package sg.activewealth.roboadvisor.banking.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

/**
 * The Enum BrokerFunding.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BrokerFundingStatus implements ByteEnum {

	/** The unprocessed. */
	Unprocessed(0, "Unprocessed"),

	/** The processed. */
	Processed(1, "Processed"),

	/** The completed. */
	Completed(2, "Completed"),

	/** The issues. */
	Issues(3, "Issues");

	/** The value. */
	private byte value;

	/** The label. */
	private String label;

	/**
	 * Instantiates a new broker funding.
	 *
	 * @param value
	 *            the value
	 * @param label
	 *            the label
	 */
	private BrokerFundingStatus(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}

	/**
	 * Gets the.
	 *
	 * @param value
	 *            the value
	 * @return the broker funding
	 */
	@JsonCreator
	public static BrokerFundingStatus get(int value) {
		BrokerFundingStatus brokerFunding = null;
		for (BrokerFundingStatus brokerFunding2 : BrokerFundingStatus.values()) {
			if (brokerFunding2.getValue() == value) {
				brokerFunding = brokerFunding2;
				break;
			}
		}
		return brokerFunding;
	}

	/**
	 * Gets the.
	 *
	 * @param label
	 *            the label
	 * @return the broker funding
	 */
	public static BrokerFundingStatus get(String label) {
		BrokerFundingStatus brokerFunding = null;
		for (BrokerFundingStatus brokerFunding2 : BrokerFundingStatus.values()) {
			if (brokerFunding2.getLabel().equalsIgnoreCase(label)) {
				brokerFunding = brokerFunding2;
				break;
			}
		}
		return brokerFunding;
	}
	
	
	public static BrokerFundingStatus fromString(String strBrokerFunding) {
		BrokerFundingStatus brokerFunding = null;
		for (BrokerFundingStatus brokerFunding2 : BrokerFundingStatus.values()) {
			if (brokerFunding2.name().equalsIgnoreCase(strBrokerFunding)) {
				brokerFunding = brokerFunding2;
				break;
			}
		}
		return brokerFunding;
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

	

}
