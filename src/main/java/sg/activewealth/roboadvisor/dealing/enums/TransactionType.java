package sg.activewealth.roboadvisor.dealing.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TransactionType implements ByteEnum {

	Subscription(0,"Subscription"),Redemption(1,"Redemption");

	private byte value;
	private String label;

	TransactionType(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}

	@JsonCreator
	public static TransactionType get(String value) {
		TransactionType fund = null;
		for (TransactionType fund2 : TransactionType.values()) {
			if (fund2.label.equals(value)) {
				fund = fund2;
				break;
			}
		}
		return fund;
	}

	public static TransactionType get(Integer value) {
		TransactionType fund = null;
		for (TransactionType fund2 : TransactionType.values()) {
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
