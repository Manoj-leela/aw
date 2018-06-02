package sg.activewealth.roboadvisor.common.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import sg.activewealth.roboadvisor.common.enums.Currency;
import sg.activewealth.roboadvisor.common.enums.ResidenceCountry;

public class UserUpdateDto {

	private String firstName;

	private String lastName;

	private String emailAddress;

	private String phone;

	private LocalDate dateOfBirth;
	
	private Currency currency;
	
    private ResidenceCountry residenceCountry;
	
	private BigDecimal annualIncome = new BigDecimal(0);
	
	private Boolean agree = Boolean.FALSE;
	
	private Boolean hasAcknowledged = Boolean.FALSE;

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

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
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

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
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

	public Boolean getAgree() {
		return agree;
	}

	public void setAgree(Boolean agree) {
		this.agree = agree;
	}

	public Boolean getHasAcknowledged() {
		return hasAcknowledged;
	}

	public void setHasAcknowledged(Boolean hasAcknowledged) {
		this.hasAcknowledged = hasAcknowledged;
	}
	
}
