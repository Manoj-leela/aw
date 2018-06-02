package sg.activewealth.roboadvisor.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import sg.activewealth.roboadvisor.infra.dto.AbstractDto;

public class RedemptionDto extends AbstractDto {

	private BigDecimal redemptionAmount;
	
	private LocalDate redemptionDate;
	
	public RedemptionDto() {
		super();
	}

	/**
	 * @return the redemptionAmount
	 */
	public BigDecimal getRedemptionAmount() {
		return redemptionAmount;
	}

	/**
	 * @param redemptionAmount the redemptionAmount to set
	 */
	public void setRedemptionAmount(BigDecimal redemptionAmount) {
		this.redemptionAmount = redemptionAmount;
	}

	/**
	 * @return the redemptionDate
	 */
	public LocalDate getRedemptionDate() {
		return redemptionDate;
	}

	/**
	 * @param redemptionDate the redemptionDate to set
	 */
	public void setRedemptionDate(LocalDate redemptionDate) {
		this.redemptionDate = redemptionDate;
	}
	
}
