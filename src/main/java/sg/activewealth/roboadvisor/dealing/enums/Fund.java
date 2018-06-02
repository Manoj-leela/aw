package sg.activewealth.roboadvisor.dealing.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Fund implements ByteEnum {

	GAF(0,"GAF"),CPIP(1,"CPIP");

	private byte value;
	private String label;

	Fund(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}

	@JsonCreator
	public static Fund get(String value) {
		Fund fund = null;
		for (Fund fund2 : Fund.values()) {
			if (fund2.label.equals(value)) {
				fund = fund2;
				break;
			}
		}
		return fund;
	}

	public static Fund get(Integer value) {
		Fund fund = null;
		for (Fund fund2 : Fund.values()) {
			if (fund2.value == value) {
				fund = fund2;
				break;
			}
		}
		return fund;
	}

	public byte getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
