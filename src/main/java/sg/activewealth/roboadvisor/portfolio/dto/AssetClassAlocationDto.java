package sg.activewealth.roboadvisor.portfolio.dto;

import java.math.BigDecimal;

import sg.activewealth.roboadvisor.infra.dto.AbstractDto;

public class AssetClassAlocationDto extends AbstractDto {

	private String instrumentId;
	private String displayName;

	private BigDecimal totalWeightage;

	private String color;

	public String getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the totalWeightage
	 */
	public BigDecimal getTotalWeightage() {
		return totalWeightage;
	}

	/**
	 * @param totalWeightage the totalWeightage to set
	 */
	public void setTotalWeightage(BigDecimal totalWeightage) {
		this.totalWeightage = totalWeightage;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	
}
