package sg.activewealth.roboadvisor.portfolio.dto;

import java.math.BigDecimal;
import java.util.List;

public class UserPortfolioListDto {

	private BigDecimal totalBalance;
	
	private BigDecimal totalReturns;
	
	private BigDecimal totalInvested;
	
	private BigDecimal earned;
	
	private List<UserPortfolioDto> userPortfolios;

	public UserPortfolioListDto() {
		super();
	}
	
	public UserPortfolioListDto(BigDecimal totalBalance, BigDecimal totalReturns, BigDecimal totalInvestments,
			BigDecimal earned, List<UserPortfolioDto> userPortfolios) {
		super();
		this.totalBalance = totalBalance;
		this.totalReturns = totalReturns;
		this.totalInvested = totalInvestments;
		this.earned = earned;
		this.userPortfolios = userPortfolios;
	}

	public UserPortfolioListDto(List<UserPortfolioDto> userPortfolios) {
		super();
		this.userPortfolios = userPortfolios;
	}

	public BigDecimal getTotalBalance() {
		return totalBalance;
	}

	public BigDecimal getTotalReturns() {
		return totalReturns;
	}

	public BigDecimal getTotalInvested() {
		return totalInvested;
	}

	public void setTotalInvested(BigDecimal totalInvested) {
		this.totalInvested = totalInvested;
	}

	public BigDecimal getEarned() {
		return earned;
	}

	public List<UserPortfolioDto> getUserPortfolios() {
		return userPortfolios;
	}

	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
	}

	public void setTotalReturns(BigDecimal totalReturns) {
		this.totalReturns = totalReturns;
	}

	public void setEarned(BigDecimal earned) {
		this.earned = earned;
	}

	public void setUserPortfolios(List<UserPortfolioDto> userPortfolios) {
		this.userPortfolios = userPortfolios;
	}
}
