package sg.activewealth.roboadvisor.common.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sg.activewealth.roboadvisor.common.enums.AccountStatus;
import sg.activewealth.roboadvisor.common.enums.AgentOTPStatus;
import sg.activewealth.roboadvisor.common.enums.BankDetailsStatus;
import sg.activewealth.roboadvisor.common.enums.KycStatus;
import sg.activewealth.roboadvisor.common.enums.ResidenceCountry;
import sg.activewealth.roboadvisor.common.enums.UserProgressStatus;
import sg.activewealth.roboadvisor.infra.enums.EnumByteUserType;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioAssignmentCategory;

@Entity
@Table(name = "user")
@TypeDefs({ @TypeDef(name = "progressStatus", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.UserProgressStatus") }),
        @TypeDef(name = "accountStatus", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.AccountStatus") }),
        @TypeDef(name = "agentOTPStatus", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.AgentOTPStatus") }),
        @TypeDef(name = "kycStatus", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.KycStatus") }),
        @TypeDef(name = "bankDetailsStatus", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.BankDetailsStatus") }),
        @TypeDef(name = "residenceCountry", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.ResidenceCountry") }),
        @TypeDef(name = "portfolioCategory", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.PortfolioAssignmentCategory") }) })
public class User extends AbstractModel {

    private String firstName;
    private String lastName;
    private String email;
    private String socialId;
    private String mobileNumber;
    private Boolean mobileVerified = false;
    private LocalDateTime mobileVerifiedTimestamp;
    private ResidenceCountry residenceCountry;

    // TODO currency column
    private BigDecimal annualIncome;
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;
    private String agentOtp;
    @Column(name="agent_otp_status")
    private AgentOTPStatus agentOTPStatus;
    private String password;

    @JsonIgnore
    private String hashSalt;
    private String lastLoggedInIpAddress;
    @Column(name="isadmin")
    private Boolean isAdmin = Boolean.FALSE;
    private BigDecimal accountSummary;
    private String brokerSaxoApiKey;
    private String brokerSaxoApiSecret;
    private UserProgressStatus progressStatus;

    // TODO # enum: Unapproved, Approved, Rejected, Locked. For now Unapproved
    // will lock the user. Later on, we will shift this to Locked, and default
    // the status to Unapproved (instead of Approved).
    private AccountStatus accountStatus;
    private KycStatus kycStatus;
    private String kycRemarks;
    private String kyc1FileName;
    private String kyc3FileName;
    private String kyc2FileName;
    private BankDetailsStatus bankDetailsStatus;
    private String bankDetailsRemarks;
    private String bankDetailsBankName;
    private String bankDetailsBankAddress;
    private String bankDetailsAba;
    private String bankDetailsChips;
    private String bankDetailsSwiftNumber;
    private String bankDetailsAccountName;
    private String bankDetailsAccountNumber;
    private String bankDetailsReference;
    private Boolean declarationsAi;
    private Boolean declarationsPep;
    private Boolean declarationsCrc;
    private Boolean declarationsTaxCrime;
    private String declarationsSourceOfIncome;
    private Boolean declarationsUsCitizen;
    private String declarationsSignatureFileName;
    private Boolean agreementUserAgreement;
    private Boolean agreementUserAgreementAcknowledged;
    private PortfolioAssignmentCategory portfolioCategory;
    private LocalDateTime signupEmailSent;
    private LocalDateTime kycUploadEmailSent;
    private LocalDateTime bankDetailsDeclarationsEmailSent;
    private LocalDateTime kycIssueEmailSent;
    private LocalDateTime kycCompletedEmailSent;
    private LocalDateTime bankDetailsDeclarationsStatusCompletedEmailSent;

    @Transient
    @JsonIgnore
    private String repassword;

    @Transient
    @JsonIgnore
    private Boolean needToRehashPassword = false;

    @Transient
    @JsonIgnore
    private Boolean needToSendMail = true;

    @Transient
    private List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<DeviceInfo> deviceInfoList;

    // ## Getter and setter methods ## //

    public User() {
    }

    public User(String id) {
        super(id);
    }

    public BigDecimal addToAccountSummary(final BigDecimal amount) {
        this.setAccountSummary(this.getAccountSummary() == null ? amount : this.getAccountSummary().add(amount));
        return this.getAccountSummary();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(Boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public LocalDateTime getMobileVerifiedTimestamp() {
        return mobileVerifiedTimestamp;
    }

    public void setMobileVerifiedTimestamp(LocalDateTime verifiedTimestamp) {
        this.mobileVerifiedTimestamp = verifiedTimestamp;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getAgentOtp() {
        return agentOtp;
    }

    public void setAgentOtp(String agentOtp) {
        this.agentOtp = agentOtp;
    }

	public AgentOTPStatus getAgentOTPStatus() {
        return agentOTPStatus;
    }

    public void setAgentOTPStatus(AgentOTPStatus agentOTPStatus) {
        this.agentOTPStatus = agentOTPStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashSalt() {
        return hashSalt;
    }

    public void setHashSalt(String hashSalt) {
        this.hashSalt = hashSalt;
    }

    public String getLastLoggedInIpAddress() {
        return lastLoggedInIpAddress;
    }

    public void setLastLoggedInIpAddress(String lastLoggedInIpAddress) {
        this.lastLoggedInIpAddress = lastLoggedInIpAddress;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public BigDecimal getAccountSummary() {
        return accountSummary;
    }

    public void setAccountSummary(BigDecimal accountSummary) {
        if (accountSummary == null) {
            this.accountSummary = new BigDecimal(0);
        } else {
            this.accountSummary = accountSummary;
        }
    }

    public String getBrokerSaxoApiKey() {
        return brokerSaxoApiKey;
    }

    public void setBrokerSaxoApiKey(String brokerSaxoApiKey) {
        this.brokerSaxoApiKey = brokerSaxoApiKey;
    }

    public String getBrokerSaxoApiSecret() {
        return brokerSaxoApiSecret;
    }

    public void setBrokerSaxoApiSecret(String brokerSaxoApiSecret) {
        this.brokerSaxoApiSecret = brokerSaxoApiSecret;
    }

    public UserProgressStatus getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(UserProgressStatus progressStatus) {
        this.progressStatus = progressStatus;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public KycStatus getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(KycStatus kycStatus) {
        this.kycStatus = kycStatus;
    }

    public String getKycRemarks() {
        return kycRemarks;
    }

    public void setKycRemarks(final String kycRemarks) {
        this.kycRemarks = kycRemarks;
    }

    public String getKyc1FileName() {
        return kyc1FileName;
    }

    public void setKyc1FileName(String kyc1FileName) {
        this.kyc1FileName = kyc1FileName;
    }

    public String getKyc3FileName() {
        return kyc3FileName;
    }

    public void setKyc3FileName(String kyc3FileName) {
        this.kyc3FileName = kyc3FileName;
    }

    public String getKyc2FileName() {
        return kyc2FileName;
    }

    public void setKyc2FileName(String kyc2FileName) {
        this.kyc2FileName = kyc2FileName;
    }

    public BankDetailsStatus getBankDetailsStatus() {
        return bankDetailsStatus;
    }

    public void setBankDetailsStatus(BankDetailsStatus bankDetailsStatus) {
        this.bankDetailsStatus = bankDetailsStatus;
    }

    public String getBankDetailsRemarks() {
        return bankDetailsRemarks;
    }

    public void setBankDetailsRemarks(final String bankDetailsRemarks) {
        this.bankDetailsRemarks = bankDetailsRemarks;
    }

    public String getBankDetailsBankName() {
        return bankDetailsBankName;
    }

    public void setBankDetailsBankName(String bankName) {
        this.bankDetailsBankName = bankName;
    }

    public String getBankDetailsBankAddress() {
        return bankDetailsBankAddress;
    }

    public void setBankDetailsBankAddress(String bankDetailAddress) {
        this.bankDetailsBankAddress = bankDetailAddress;
    }

    public String getBankDetailsAba() {
        return bankDetailsAba;
    }

    public void setBankDetailsAba(String bankDetailsAba) {
        this.bankDetailsAba = bankDetailsAba;
    }

    public String getBankDetailsChips() {
        return bankDetailsChips;
    }

    public void setBankDetailsChips(String bankDetailsChips) {
        this.bankDetailsChips = bankDetailsChips;
    }

    public String getBankDetailsSwiftNumber() {
        return bankDetailsSwiftNumber;
    }

    public void setBankDetailsSwiftNumber(String bankDetailsSwiftNumber) {
        this.bankDetailsSwiftNumber = bankDetailsSwiftNumber;
    }

    public String getBankDetailsAccountName() {
        return bankDetailsAccountName;
    }

    public void setBankDetailsAccountName(String bankDetailsAccountName) {
        this.bankDetailsAccountName = bankDetailsAccountName;
    }

    public String getBankDetailsAccountNumber() {
        return bankDetailsAccountNumber;
    }

    public void setBankDetailsAccountNumber(String bankDetailsAccountNumber) {
        this.bankDetailsAccountNumber = bankDetailsAccountNumber;
    }

    public String getBankDetailsReference() {
        return bankDetailsReference;
    }

    public void setBankDetailsReference(String bankDetailsReference) {
        this.bankDetailsReference = bankDetailsReference;
    }

    public Boolean getDeclarationsAi() {
        return declarationsAi;
    }

    public void setDeclarationsAi(Boolean declarationsAi) {
        this.declarationsAi = declarationsAi;
    }

    public Boolean getDeclarationsPep() {
        return declarationsPep;
    }

    public void setDeclarationsPep(Boolean declarationsPep) {
        this.declarationsPep = declarationsPep;
    }

    public Boolean getDeclarationsCrc() {
        return declarationsCrc;
    }

    public void setDeclarationsCrc(Boolean declarationsCrc) {
        this.declarationsCrc = declarationsCrc;
    }

    public Boolean getDeclarationsTaxCrime() {
        return declarationsTaxCrime;
    }

    public void setDeclarationsTaxCrime(Boolean declarationsTaxCrime) {
        this.declarationsTaxCrime = declarationsTaxCrime;
    }

    public String getDeclarationsSourceOfIncome() {
        return declarationsSourceOfIncome;
    }

    public void setDeclarationsSourceOfIncome(String declarationsSourceOfIncome) {
        this.declarationsSourceOfIncome = declarationsSourceOfIncome;
    }

    public Boolean getDeclarationsUsCitizen() {
        return declarationsUsCitizen;
    }

    public void setDeclarationsUsCitizen(Boolean declarationsUsCitizen) {
        this.declarationsUsCitizen = declarationsUsCitizen;
    }

    public String getDeclarationsSignatureFileName() {
        return declarationsSignatureFileName;
    }

    public void setDeclarationsSignatureFileName(String declarationsSignatureFileName) {
        this.declarationsSignatureFileName = declarationsSignatureFileName;
    }

    public Boolean getAgreementUserAgreement() {
        return agreementUserAgreement;
    }

    public void setAgreementUserAgreement(Boolean agreementUserAgreement) {
        this.agreementUserAgreement = agreementUserAgreement;
    }

    public Boolean getAgreementUserAgreementAcknowledged() {
        return agreementUserAgreementAcknowledged;
    }

    public void setAgreementUserAgreementAcknowledged(Boolean agreementUserAgreementAcknowledged) {
        this.agreementUserAgreementAcknowledged = agreementUserAgreementAcknowledged;
    }

    public PortfolioAssignmentCategory getPortfolioCategory() {
        return portfolioCategory;
    }

    public void setPortfolioCategory(PortfolioAssignmentCategory portfolioCategory) {
        this.portfolioCategory = portfolioCategory;
    }

    public LocalDateTime getSignupEmailSent() {
        return signupEmailSent;
    }

    public void setSignupEmailSent(final LocalDateTime signupEmailSent) {
        this.signupEmailSent = signupEmailSent;
    }

    public LocalDateTime getKycUploadEmailSent() {
        return kycUploadEmailSent;
    }

    public void setKycUploadEmailSent(final LocalDateTime kycUploadEmailSent) {
        this.kycUploadEmailSent = kycUploadEmailSent;
    }

    public LocalDateTime getBankDetailsDeclarationsEmailSent() {
        return bankDetailsDeclarationsEmailSent;
    }

    public void setBankDetailsDeclarationsEmailSent(final LocalDateTime bankDetailsDeclarationsEmailSent) {
        this.bankDetailsDeclarationsEmailSent = bankDetailsDeclarationsEmailSent;
    }

    public LocalDateTime getKycIssueEmailSent() {
        return kycIssueEmailSent;
    }

    public void setKycIssueEmailSent(final LocalDateTime kycIssueEmailSent) {
        this.kycIssueEmailSent = kycIssueEmailSent;
    }

    public LocalDateTime getKycCompletedEmailSent() {
        return kycCompletedEmailSent;
    }

    public void setKycCompletedEmailSent(final LocalDateTime kycCompletedEmailSent) {
        this.kycCompletedEmailSent = kycCompletedEmailSent;
    }

    public LocalDateTime getBankDetailsDeclarationsStatusCompletedEmailSent() {
        return bankDetailsDeclarationsStatusCompletedEmailSent;
    }

    public void setBankDetailsDeclarationsStatusCompletedEmailSent(final LocalDateTime bankDetailsDeclarationsStatusCompletedEmailSent) {
        this.bankDetailsDeclarationsStatusCompletedEmailSent = bankDetailsDeclarationsStatusCompletedEmailSent;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public Boolean getNeedToRehashPassword() {
        return needToRehashPassword;
    }

    public void setNeedToRehashPassword(Boolean needToRehashPassword) {
        this.needToRehashPassword = needToRehashPassword;
    }

    public Boolean getNeedToSendMail() {
        return needToSendMail;
    }

    public void setNeedToSendMail(Boolean needToSendMail) {
        this.needToSendMail = needToSendMail;
    }

    public List<MultipartFile> getMultipartFiles() {
        return multipartFiles;
    }

    public void setMultipartFiles(List<MultipartFile> multipartFiles) {
        this.multipartFiles = multipartFiles;
    }

    public List<DeviceInfo> getDeviceInfoList() {
        return deviceInfoList;
    }

    public void setDeviceInfoList(List<DeviceInfo> deviceInfoList) {
        this.deviceInfoList = deviceInfoList;
    }

    // Do not delete, Implemented for the backward compatibility for mobile.

    @Transient
    private String emailAddress;

    @Transient
    private String phone;
    @Transient
    private Boolean isVerified;
    @Transient
    private LocalDateTime verifiedTimestamp;
    @Transient
    private Boolean admin;
    @Transient
    private String bankName;
    @Transient
    private String bankAddress;
    @Transient
    private String bankAba;
    @Transient
    private String bankChips;
    @Transient
    private String bankSwiftNumber;
    @Transient
    private String bankAccountName;
    @Transient
    private String bankAccountNumber;
    @Transient
    private String bankReference;
    @Transient
    private Boolean bankAccreditedInvestor;
    @Transient
    private Boolean bankPep;
    @Transient
    private Boolean bankCrc;
    @Transient
    private Boolean bankTaxCrime;
    @Transient
    private String sourceOfIncome;
    @Transient
    private Boolean agreeTermsAndConditions;
    @Transient
    private String bankSignatureFileName;
    @Transient
    private Boolean acknowledgeRecommedation;
    @Transient
    private Boolean usCitizen;
    @Transient
    private PortfolioAssignmentCategory portfolioAssignmentCategory;

    public String getEmailAddress() {
        return getEmail();
    }

    public void setEmailAddress(String emailAddress) {
        setEmail(emailAddress);
    }

    public String getPhone() {
        return getMobileNumber();
    }

    public void setPhone(String phone) {
        setMobileNumber(phone);
    }

    public Boolean getIsVerified() {
        return getMobileVerified();
    }

    public void setIsVerified(Boolean isVerified) {
        setMobileVerified(isVerified);
    }

    public LocalDateTime getVerifiedTimestamp() {
        return getMobileVerifiedTimestamp();
    }

    public void setVerifiedTimestamp(LocalDateTime verifiedTimestamp) {
        setMobileVerifiedTimestamp(verifiedTimestamp);
    }

    public Boolean getAdmin() {
        return getIsAdmin();
    }

    public void setAdmin(Boolean admin) {
        setIsAdmin(admin);
    }

    public String getBankName() {
        return getBankDetailsBankName();
    }

    public void setBankName(String bankName) {
        setBankDetailsBankName(bankName);
    }

    public String getBankAddress() {
        return getBankDetailsBankAddress();
    }

    public void setBankAddress(String bankAddress) {
        setBankDetailsBankAddress(bankAddress);
    }

    public String getBankAba() {
        return getBankDetailsAba();
    }

    public void setBankAba(String bankAba) {
        setBankDetailsAba(bankAba);
    }

    public String getBankChips() {
        return getBankDetailsChips();
    }

    public void setBankChips(String bankChips) {
        setBankDetailsChips(bankChips);
    }

    public String getBankSwiftNumber() {
        return getBankDetailsSwiftNumber();
    }

    public void setBankSwiftNumber(String bankSwiftNumber) {
        setBankDetailsSwiftNumber(bankSwiftNumber);
    }

    public String getBankAccountName() {
        return getBankDetailsAccountName();
    }

    public void setBankAccountName(String bankAccountName) {
        setBankDetailsAccountName(bankAccountName);
    }

    public String getBankAccountNumber() {
        return getBankDetailsAccountNumber();
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        setBankDetailsAccountNumber(bankAccountNumber);
    }

    public String getBankReference() {
        return getBankDetailsReference();
    }

    public void setBankReference(String bankReference) {
        setBankDetailsReference(bankReference);
    }

    public Boolean getBankAccreditedInvestor() {
        return getDeclarationsAi();
    }

    public void setBankAccreditedInvestor(Boolean bankAccreditedInvestor) {
        setDeclarationsAi(bankAccreditedInvestor);
    }

    public Boolean getBankPep() {
        return getDeclarationsPep();
    }

    public void setBankPep(Boolean bankPep) {
        setDeclarationsPep(bankPep);
    }

    public Boolean getBankCrc() {
        return getDeclarationsCrc();
    }

    public void setBankCrc(Boolean bankCrc) {
        setDeclarationsCrc(bankCrc);
    }

    public Boolean getBankTaxCrime() {
        return getDeclarationsTaxCrime();
    }

    public void setBankTaxCrime(Boolean bankTaxCrime) {
        setDeclarationsTaxCrime(bankTaxCrime);
    }

    public String getSourceOfIncome() {
        return getDeclarationsSourceOfIncome();

    }

    public void setSourceOfIncome(String sourceOfIncome) {
        setDeclarationsSourceOfIncome(sourceOfIncome);
    }

    public Boolean getAgreeTermsAndConditions() {
        return getAgreementUserAgreement();
    }

    public void setAgreeTermsAndConditions(Boolean agreeTermsAndConditions) {
        setAgreementUserAgreement(agreeTermsAndConditions);
    }

    public String getBankSignatureFileName() {
        return getDeclarationsSignatureFileName();
    }

    public void setBankSignatureFileName(String bankSignatureFileName) {
        setDeclarationsSignatureFileName(bankSignatureFileName);
    }

    public Boolean getAcknowledgeRecommedation() {
        return getAgreementUserAgreementAcknowledged();
    }

    public void setAcknowledgeRecommedation(Boolean acknowledgeRecommedation) {
        setAgreementUserAgreementAcknowledged(acknowledgeRecommedation);
    }

    public Boolean getUsCitizen() {
        return getDeclarationsUsCitizen();
    }

    public void setUsCitizen(Boolean usCitizen) {
        setDeclarationsUsCitizen(usCitizen);
    }

    public PortfolioAssignmentCategory getPortfolioAssignmentCategory() {
        return getPortfolioCategory();
    }

    public void setPortfolioAssignmentCategory(PortfolioAssignmentCategory portfolioAssignmentCategory) {
        setPortfolioCategory(portfolioAssignmentCategory);
    }

}
