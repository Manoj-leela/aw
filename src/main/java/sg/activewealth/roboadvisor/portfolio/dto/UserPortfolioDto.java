package sg.activewealth.roboadvisor.portfolio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolioQuestionAndAnswer;

public class UserPortfolioDto {

	public UserPortfolioDto() {
		super();
	}

	private String goal;

	private LocalDate targetDateOfWithdrawal;

	private List<UserPortfolioQuestionAndAnswer> questionAndAnswerList = new ArrayList<>();

	private String portfolioType;

	private BigDecimal totalBalance;

	private BigDecimal totalReturns;
	
	private BigDecimal totalInvested = new BigDecimal(0);
	
	private BigDecimal earned = new BigDecimal(0);
	
	private String userPortfolioStatus;

	private String id;

	private Portfolio portfolio;

	private String timeHorizon;

	private List<BigDecimal> predictedAmount;

	private BigDecimal predictedGrowth = new BigDecimal(0);
	
	private String fundingStatus;
	
	private LocalDate dealingDate;
	

	public UserPortfolioDto(BigDecimal totalInvested, BigDecimal totalBalance, BigDecimal totalReturns, String id,
			Portfolio portfolio, String goal, String fundingStatus) {
		super();
		this.totalBalance = totalBalance;
		this.totalReturns = totalReturns;
		this.id = id;
		this.portfolio = portfolio;
		this.portfolioType = portfolio.getRiskProfile().getKey();
		this.goal = goal;
		this.totalInvested = totalInvested;
		this.fundingStatus = fundingStatus;
	}

	public UserPortfolioDto(BigDecimal totalInvested, BigDecimal totalBalance, BigDecimal totalReturns, String id,
			Portfolio portfolio,String userPortfolioStatus) {
		super();
		this.totalInvested = totalInvested;
		this.totalBalance = totalBalance;
		this.totalReturns = totalReturns;
		this.id = id;
		this.portfolio = portfolio;
		this.portfolioType = portfolio.getRiskProfile().getKey();
		this.userPortfolioStatus = userPortfolioStatus;
	}
	
	public UserPortfolioDto(BigDecimal totalBalance, BigDecimal totalReturns,BigDecimal totalInvested, String id,
			Portfolio portfolio, String goal, String userPortfolioStatus,String fundingStatus,LocalDate dealingDate) {
		super();
		this.totalBalance = totalBalance;
		this.totalReturns = totalReturns;
		this.totalInvested = totalInvested;
		this.earned = totalBalance != null ? totalBalance.subtract(totalInvested) : new BigDecimal(0);
		this.id = id;
		this.portfolio = portfolio;
		this.portfolioType = portfolio.getRiskProfile().getKey();
		this.goal = goal;
		this.userPortfolioStatus = userPortfolioStatus;
		this.fundingStatus = fundingStatus;
		this.dealingDate = dealingDate;
	}			

	public List<UserPortfolioQuestionAndAnswer> getQuestionAndAnswerList() {
		return questionAndAnswerList;
	}

	public void setQuestionAndAnswerList(List<UserPortfolioQuestionAndAnswer> questionAndAnswerList) {
		this.questionAndAnswerList = questionAndAnswerList;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public LocalDate getTargetDateOfWithdrawal() {
		return targetDateOfWithdrawal;
	}

	public void setTargetDateOfWithdrawal(LocalDate targetDateOfWithdrawal) {
		this.targetDateOfWithdrawal = targetDateOfWithdrawal;
	}

	public BigDecimal getTotalBalance() {
		return totalBalance;
	}

	public BigDecimal getTotalReturns() {
		return totalReturns;
	}

	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
	}

	public void setTotalReturns(BigDecimal totalReturns) {
		this.totalReturns = totalReturns;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public String getPortfolioType() {
		return portfolioType;
	}

	public void setPortfolioType(String portfolioType) {
		this.portfolioType = portfolioType;
	}

	public List<BigDecimal> getPredictedAmount() {
		return predictedAmount;
	}

	public void setPredictedAmount(List<BigDecimal> predictedAmount) {
		this.predictedAmount = predictedAmount;
	}

	public BigDecimal getPredictedGrowth() {
		return predictedGrowth;
	}

	public void setPredictedGrowth(BigDecimal predictedGrowth) {
		this.predictedGrowth = predictedGrowth;
	}

	public String getTimeHorizon() {
		return timeHorizon;
	}

	public void setTimeHorizon(String timeHorizon) {
		this.timeHorizon = timeHorizon;
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

	public void setEarned(BigDecimal earned) {
		this.earned = earned;
	}

	public String getUserPortfolioStatus() {
		return userPortfolioStatus;
	}

	public void setUserPortfolioStatus(String userPortfolioStatus) {
		this.userPortfolioStatus = userPortfolioStatus;
	}

	public String getFundingStatus() {
		return fundingStatus;
	}

	public void setFundingStatus(String fundingStatus) {
		this.fundingStatus = fundingStatus;
	}

	public LocalDate getDealingDate() {
		return dealingDate;
	}

	public void setDealingDate(LocalDate dealingDate) {
		this.dealingDate = dealingDate;
	}
	
}
