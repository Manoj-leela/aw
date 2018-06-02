package sg.activewealth.roboadvisor.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)

public enum Currency implements ByteEnum {

	USD(0, "US$"), SGD(1, "S$"), MYR(2, "MYR");

	private byte value;
	private String label;

	Currency(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}

	@JsonCreator
	public static Currency get(String currentCurrencyValue) {
		Currency currency = null;
		for (Currency currentCurrency : Currency.values()) {
			if (currentCurrency.label.equals(currentCurrencyValue)) {
				currency = currentCurrency;
				break;
			}
		}
		return currency;
	}

	public static Currency get(Integer value) {
		Currency currency = null;
		for (Currency currency2 : Currency.values()) {
			if (currency2.value == value) {
				currency = currency2;
				break;
			}
		}
		return currency;
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
