package sg.activewealth.roboadvisor.banking.dto;

import sg.activewealth.roboadvisor.infra.dto.AbstractDto;

public class BankDetailsDto extends AbstractDto {

	private String name;
	private String address;
	private String aba;
	private String chips;
	private String swift;
	private String accountName;
	private String accountNumber;
	private String reference;
	private Boolean accreditedInvestor = Boolean.FALSE;
	private Boolean pep = Boolean.FALSE;
	private Boolean crc = Boolean.FALSE;
	private Boolean taxcrime = Boolean.FALSE;
	private String sourceOfIncome;
	private String charges;
	private String swiftSecondaryAddress;
	private Boolean usCitizen = Boolean.FALSE;
	
	public BankDetailsDto() {
		super();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the aba
	 */
	public String getAba() {
		return aba;
	}

	/**
	 * @param aba
	 *            the aba to set
	 */
	public void setAba(String aba) {
		this.aba = aba;
	}

	/**
	 * @return the chips
	 */
	public String getChips() {
		return chips;
	}

	/**
	 * @param chips
	 *            the chips to set
	 */
	public void setChips(String chips) {
		this.chips = chips;
	}

	/**
	 * @return the swift
	 */
	public String getSwift() {
		return swift;
	}

	/**
	 * @param swift
	 *            the swift to set
	 */
	public void setSwift(String swift) {
		this.swift = swift;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName
	 *            the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *            the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the accreditedInvestor
	 */
	public Boolean getAccreditedInvestor() {
		return accreditedInvestor;
	}

	/**
	 * @param accreditedInvestor
	 *            the accreditedInvestor to set
	 */
	public void setAccreditedInvestor(Boolean accreditedInvestor) {
		this.accreditedInvestor = accreditedInvestor;
	}

	/**
	 * @return the pep
	 */
	public Boolean getPep() {
		return pep;
	}

	/**
	 * @param pep
	 *            the pep to set
	 */
	public void setPep(Boolean pep) {
		this.pep = pep;
	}

	/**
	 * @return the crc
	 */
	public Boolean getCrc() {
		return crc;
	}

	/**
	 * @param crc
	 *            the crc to set
	 */
	public void setCrc(Boolean crc) {
		this.crc = crc;
	}

	/**
	 * @return the taxcrime
	 */
	public Boolean getTaxcrime() {
		return taxcrime;
	}

	/**
	 * @param taxcrime
	 *            the taxcrime to set
	 */
	public void setTaxcrime(Boolean taxcrime) {
		this.taxcrime = taxcrime;
	}

	/**
	 * @return the sourceOfIncome
	 */
	public String getSourceOfIncome() {
		return sourceOfIncome;
	}

	/**
	 * @param sourceOfIncome the sourceOfIncome to set
	 */
	public void setSourceOfIncome(String sourceOfIncome) {
		this.sourceOfIncome = sourceOfIncome;
	}


	/**
	 * @return the charges
	 */
	public String getCharges() {
		return charges;
	}

	/**
	 * @param charges the charges to set
	 */
	public void setCharges(String charges) {
		this.charges = charges;
	}

	/**
	 * @return the swiftSecondaryAddress
	 */
	public String getSwiftSecondaryAddress() {
		return swiftSecondaryAddress;
	}

	/**
	 * @param swiftSecondaryAddress the swiftSecondaryAddress to set
	 */
	public void setSwiftSecondaryAddress(String swiftSecondaryAddress) {
		this.swiftSecondaryAddress = swiftSecondaryAddress;
	}

	/**
	 * @return the usCitizen
	 */
	public Boolean getUsCitizen() {
		return usCitizen;
	}

	/**
	 * @param usCitizen the usCitizen to set
	 */
	public void setUsCitizen(Boolean usCitizen) {
		this.usCitizen = usCitizen;
	}

	
}
