package sg.activewealth.roboadvisor.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeclarationStatus implements ByteEnum {
	
	NotAccepted(0, "Not Accepted"),Accepted(1, "Accepted");

	private byte value;
	private String label;
	
	private DeclarationStatus(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}
	
	@JsonCreator
	public static DeclarationStatus get(int value) {
		DeclarationStatus declarationStatus = null;
		for (DeclarationStatus declarationStatus2 : DeclarationStatus.values()) {
			if (declarationStatus2.getValue() == value) {
				declarationStatus = declarationStatus2;
				break;
			}
		}
		return declarationStatus;
	}
	
	public static DeclarationStatus get(String label) {
		DeclarationStatus declarationStatus = null;
		for (DeclarationStatus declarationStatus2 : DeclarationStatus.values()) {
			if (declarationStatus2.getLabel().equalsIgnoreCase(label)) {
				declarationStatus = declarationStatus2;
				break;
			}
		}
		return declarationStatus;
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
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(byte value) {
		this.value = value;
	}

}
