package sg.activewealth.roboadvisor.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SourceOfIncome implements ByteEnum {

	Salary(0, "Salary");

	private byte value;
	private String label;

	SourceOfIncome(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}
	
	@JsonCreator
	public static SourceOfIncome get(String label) {
		SourceOfIncome sourceOfIncome = null;
		for (SourceOfIncome sourceOfIncome2 : SourceOfIncome.values()) {
			if (sourceOfIncome2.getLabel().equals(label)) {
				sourceOfIncome = sourceOfIncome2;
				break;
			}
		}
		return sourceOfIncome;
	}

	public static SourceOfIncome get(int value) {
		SourceOfIncome sourceOfIncome = null;
		for (SourceOfIncome sourceOfIncome2 : SourceOfIncome.values()) {
			if (sourceOfIncome2.getValue() == value) {
				sourceOfIncome = sourceOfIncome2;
				break;
			}
		}
		return sourceOfIncome;
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
