package sg.activewealth.roboadvisor.trade.dto;

import java.math.BigDecimal;

public class TradeDetailsDto {

	private String instrumentName;
	
	private BigDecimal currentValue;
	
	private BigDecimal numberOfShares;
	
	private String intrumentDescription;
	
	private String instrumentCode;

	public String getInstrumentName() {
		return instrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}

	public BigDecimal getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}

	public BigDecimal getNumberOfShares() {
		return numberOfShares;
	}

	public void setNumberOfShares(BigDecimal numberOfShares) {
		this.numberOfShares = numberOfShares;
	}

	public String getIntrumentDescription() {
		return intrumentDescription;
	}

	public void setIntrumentDescription(String intrumentDescription) {
		this.intrumentDescription = intrumentDescription;
	}

	public String getInstrumentCode() {
		return instrumentCode;
	}

	public void setInstrumentCode(String instrumentCode) {
		this.instrumentCode = instrumentCode;
	}
}
