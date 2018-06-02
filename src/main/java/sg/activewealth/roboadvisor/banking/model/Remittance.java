package sg.activewealth.roboadvisor.banking.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.web.multipart.MultipartFile;

import sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus;
import sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus;
import sg.activewealth.roboadvisor.infra.enums.EnumByteUserType;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;

@Entity
@Table(name = "remittance")
@TypeDefs({ @TypeDef(name = "brokerFundingStatus", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.BrokerFunding") }),
        @TypeDef(name = "investorRemittanceStatus", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.InvestorRemittance") }) })
public class Remittance extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "user_portfolio_id")
    private UserPortfolio userPortfolio;

    private String referenceNo;
    private String remittanceSlipFileName;
    private InvestorRemittanceStatus investorRemittanceStatus;
    private BigDecimal investorRemittanceRemittedAmount;
    private BigDecimal investorRemittanceReceivedAmount;
    private BigDecimal investorRemittanceFees;
    private BrokerFundingStatus brokerFundingStatus;
    private LocalDate brokerBatch;
    private BigDecimal brokerFundingReceivedAmount;
    private BigDecimal brokerFundingFees;
    private BigDecimal netInvestmentAmount;
    private LocalDateTime investorRemittanceIssueEmailSent;
    private LocalDateTime reconciliationEmailSent;
    private LocalDateTime reconciliationAwaitEmailSent;
    private LocalDateTime brokerFundedEmailSent;
    private String remarks;

    @Transient
    private MultipartFile attachment;

    public Remittance() {
    }

    public Remittance(String id) {
        super(id);
    }

    public UserPortfolio getUserPortfolio() {
        return userPortfolio;
    }

    public void setUserPortfolio(UserPortfolio userPortfolio) {
        this.userPortfolio = userPortfolio;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getRemittanceSlipFileName() {
        return remittanceSlipFileName;
    }

    public void setRemittanceSlipFileName(String remittanceSlipFileName) {
        this.remittanceSlipFileName = remittanceSlipFileName;
    }

    public InvestorRemittanceStatus getInvestorRemittanceStatus() {
        return investorRemittanceStatus;
    }

    public void setInvestorRemittanceStatus(InvestorRemittanceStatus investorRemittanceStatus) {
        this.investorRemittanceStatus = investorRemittanceStatus;
    }

    public BigDecimal getInvestorRemittanceRemittedAmount() {
        return investorRemittanceRemittedAmount;
    }

    public void setInvestorRemittanceRemittedAmount(BigDecimal investorRemittanceRemittedAmount) {
        this.investorRemittanceRemittedAmount = investorRemittanceRemittedAmount;
    }

    public BigDecimal getInvestorRemittanceReceivedAmount() {
        return investorRemittanceReceivedAmount;
    }

    public void setInvestorRemittanceReceivedAmount(BigDecimal investorRemittanceReceivedAmount) {
        this.investorRemittanceReceivedAmount = investorRemittanceReceivedAmount;
    }

    public BigDecimal getInvestorRemittanceFees() {
        return investorRemittanceFees;
    }

    public void setInvestorRemittanceFees(BigDecimal investorRemittanceFees) {
        this.investorRemittanceFees = investorRemittanceFees;
    }

    public BrokerFundingStatus getBrokerFundingStatus() {
        return brokerFundingStatus;
    }

    public void setBrokerFundingStatus(BrokerFundingStatus brokerFundingStatus) {
        this.brokerFundingStatus = brokerFundingStatus;
    }

    public LocalDate getBrokerBatch() {
        return brokerBatch;
    }

    public void setBrokerBatch(LocalDate brokerBatch) {
        this.brokerBatch = brokerBatch;
    }

    public BigDecimal getBrokerFundingReceivedAmount() {
        return brokerFundingReceivedAmount;
    }

    public void setBrokerFundingReceivedAmount(BigDecimal brokerFundingReceivedAmount) {
        this.brokerFundingReceivedAmount = brokerFundingReceivedAmount;
    }

    public BigDecimal getBrokerFundingFees() {
        return brokerFundingFees;
    }

    public void setBrokerFundingFees(BigDecimal brokerFundingFees) {
        this.brokerFundingFees = brokerFundingFees;
    }

    public BigDecimal getNetInvestmentAmount() {
        return netInvestmentAmount;
    }

    public void setNetInvestmentAmount(BigDecimal netInvestmentAmount) {
        this.netInvestmentAmount = netInvestmentAmount;
    }

    public LocalDateTime getInvestorRemittanceIssueEmailSent() {
        return investorRemittanceIssueEmailSent;
    }

    public void setInvestorRemittanceIssueEmailSent(LocalDateTime investorRemittanceIssueEmailSent) {
        this.investorRemittanceIssueEmailSent = investorRemittanceIssueEmailSent;
    }

    public LocalDateTime getReconciliationEmailSent() {
        return reconciliationEmailSent;
    }

    public void setReconciliationEmailSent(LocalDateTime reconciliationEmailSent) {
        this.reconciliationEmailSent = reconciliationEmailSent;
    }

    public LocalDateTime getReconciliationAwaitEmailSent() {
        return reconciliationAwaitEmailSent;
    }

    public void setReconciliationAwaitEmailSent(LocalDateTime reconciliationAwaitEmailSent) {
        this.reconciliationAwaitEmailSent = reconciliationAwaitEmailSent;
    }

    public LocalDateTime getBrokerFundedEmailSent() {
        return brokerFundedEmailSent;
    }

    public void setBrokerFundedEmailSent(LocalDateTime brokerFundedEmailSent) {
        this.brokerFundedEmailSent = brokerFundedEmailSent;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public MultipartFile getAttachment() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }

}
