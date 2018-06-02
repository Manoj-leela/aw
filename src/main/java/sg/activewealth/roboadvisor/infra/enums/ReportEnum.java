package sg.activewealth.roboadvisor.infra.enums;

public enum ReportEnum implements StringEnum {

	Agent("Agent","agent/generateReport"), 
	ExternalFund("External Fund","externalFund/generateReport"),
	ExternalFundPrice("External Fund Price","externalFundPrice/generateReport"),
	ExternalFundSubscription("External Fund Subscription","externalFundSubscription/generateReport"),
	ExternalFundSubscriptionResold("External Fund Subscription Resold","externalFundSubscriptionResold/generateReport"),
	Instrument("Instrument","instrument/generateReport"),
	Portfolio("Portfolio","portfolio/generateReport"),
	Redemption("Redemption","redemption/generateReport"),
	Remittance("Remittance","remittance/generateReport"),
	User("User","user/generateReport"),
	UserPortfolio("User Portfolio","userPortfolio/generateReport"),
	UserPortfolioQuesAndAns("User Portfolio Ques And Ans","userPortfolioQuestionAndAnswer/generateReport");

	private String label;
	private String value;

	private ReportEnum(String label ,String value) {
		this.label = label;
		this.value = value;
	}
	
	public String getLabel() {
		return this.label;
	}

	@Override
	public String getValue() {
		return this.value;
	}

}
