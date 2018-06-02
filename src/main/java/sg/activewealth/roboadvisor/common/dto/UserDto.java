package sg.activewealth.roboadvisor.common.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import sg.activewealth.roboadvisor.common.enums.AccountStatus;
import sg.activewealth.roboadvisor.common.enums.Currency;
import sg.activewealth.roboadvisor.common.enums.ResidenceCountry;
import sg.activewealth.roboadvisor.common.enums.UserProgressStatus;
import sg.activewealth.roboadvisor.common.model.UserPaymentMethod;
import sg.activewealth.roboadvisor.infra.dto.AbstractDto;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioAssignmentCategory;

public class UserDto extends AbstractDto {

	private String firstName;

	private String lastName;

	private String emailAddress;

	private String phone;

	private UserProgressStatus progressStatus = UserProgressStatus.RiskProfiling;

	private BigDecimal accountSummary;

	private LocalDate dateOfBirth;

	private UserPaymentMethod userPaymentMethod;
	
	private String token;
	
	private String id;

	private AccountStatus accountStatus;
	
	private Currency currency = Currency.SGD;
	
	private UserStatusDto userStatusDto;
	
    private ResidenceCountry residenceCountry;
	
	private BigDecimal annualIncome = new BigDecimal(0);
	
	private PortfolioAssignmentCategory portfolioAssignmentCategory;
	
	private Boolean hasAcknowledged;
	
	private Boolean agree = Boolean.FALSE;
	
	public UserDto() {
		super();
	}
	
	public UserDto(String id,String firstName, String lastName, String emailAddress, String phone,
			UserProgressStatus progressStatus, BigDecimal accountSummary, LocalDate dateOfBirth,
			UserPaymentMethod userPaymentMethod,Currency currency,ResidenceCountry residenceCountry,BigDecimal annualIncome, AccountStatus accountStatus) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phone = phone;
		this.progressStatus = progressStatus;
		this.accountSummary = accountSummary;
		this.dateOfBirth = dateOfBirth;
		this.userPaymentMethod = userPaymentMethod;
		this.currency = currency;
		this.accountStatus = accountStatus;
		this.residenceCountry = residenceCountry;
		this.annualIncome = annualIncome;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPhone() {
		return phone;
	}

	public UserProgressStatus getProgressStatus() {
		return progressStatus;
	}

	public BigDecimal getAccountSummary() {
		return accountSummary;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public UserPaymentMethod getUserPaymentMethod() {
		return userPaymentMethod;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setProgressStatus(UserProgressStatus progressStatus) {
		this.progressStatus = progressStatus;
	}

	public void setAccountSummary(BigDecimal accountSummary) {
		this.accountSummary = accountSummary;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setUserPaymentMethod(UserPaymentMethod userPaymentMethod) {
		this.userPaymentMethod = userPaymentMethod;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public UserStatusDto getUserStatusDto() {
		return userStatusDto;
	}

	public void setUserStatusDto(UserStatusDto userStatusDto) {
		this.userStatusDto = userStatusDto;
	}

	public ResidenceCountry getResidenceCountry() {
		return residenceCountry;
	}

	public void setResidenceCountry(ResidenceCountry residenceCountry) {
		this.residenceCountry = residenceCountry;
	}

	public BigDecimal getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(BigDecimal annualIncome) {
		this.annualIncome = annualIncome;
	}

	public PortfolioAssignmentCategory getPortfolioAssignmentCategory() {
		return portfolioAssignmentCategory;
	}

	public void setPortfolioAssignmentCategory(PortfolioAssignmentCategory portfolioAssignmentCategory) {
		this.portfolioAssignmentCategory = portfolioAssignmentCategory;
	}

	public Boolean getHasAcknowledged() {
		return hasAcknowledged;
	}

	public void setHasAcknowledged(Boolean hasAcknowledged) {
		this.hasAcknowledged = hasAcknowledged;
	}

	public Boolean getAgree() {
		return agree;
	}

	public void setAgree(Boolean agree) {
		this.agree = agree;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
}
