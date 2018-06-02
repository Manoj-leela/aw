package sg.activewealth.roboadvisor.portfolio.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum InstrumentType implements ByteEnum {
	/*Currencies(0, "Currencies", "#06750a"), Commodities(1, "Commodities", "#111685"), Equities(2, "Equities",
			"#ff7e08"), FixedIncome(3, "Fixed Income", "#ffe600"), Cash(4, "Cash", "#ff0000"), ETF(5, "ETF", "#8f00ff"), CPFund(5, "CP Fund", "#08c93e");*/
	Currency(0, "Currency"), Commodity(1, "Commodity"), Stock(2, "Stock"), Bond(3, "Bond"), Cash(4, "Cash"), ETF(5, "ETF"), CPFund(6, "CP Fund"),
	ExternalFund(7,"External Fund");


	private byte value;

	private String label;


	private InstrumentType(int value, String label) {
		this.label = label;
		this.value = (byte) value;
	}

	public static InstrumentType get(String currentInstrumentTypeValue) {
		InstrumentType instrumentType = null;
		for (InstrumentType currentInstrumentType : sg.activewealth.roboadvisor.portfolio.enums.InstrumentType.values()) {
			if (currentInstrumentType.label.equals(currentInstrumentTypeValue)) {
				instrumentType = currentInstrumentType;
				break;
			}
		}
		return instrumentType;
	}

	@JsonCreator
	public static InstrumentType get(Integer value) {
		InstrumentType instrumentType = null;
		for (InstrumentType instrumentType2 : sg.activewealth.roboadvisor.portfolio.enums.InstrumentType.values()) {
			if (instrumentType2.value == value) {
				instrumentType = instrumentType2;
				break;
			}
		}
		return instrumentType;
	}

	public byte getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

}
