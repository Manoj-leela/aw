package sg.activewealth.roboadvisor.banking.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import sg.activewealth.roboadvisor.banking.enums.RedemptionStatus;
import sg.activewealth.roboadvisor.infra.enums.EnumByteUserType;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;

@Entity
@Table(name = "redemption")
@TypeDefs({ @TypeDef(name = "redemptionStatus", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.redemptionStatus") }) })
public class Redemption extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "user_portfolio_id")
    private UserPortfolio userPortfolio;

    private LocalDate redemptionDate;
    private BigDecimal redemptionAmount;
    private RedemptionStatus redemptionStatus;
    private LocalDate brokerBatch;
    private BigDecimal amountRequestedFromBroker;
    private BigDecimal amountReceivedFromBroker;
    private BigDecimal amountReceivedFees;
    private BigDecimal netRedemptionAmount;
    private LocalDate redemptionCompletedEmailSent;
    private String remarks;

    public Redemption() {
    }

    public Redemption(String id) {
        super(id);
    }

    public UserPortfolio getUserPortfolio() {
        return userPortfolio;
    }

    public void setUserPortfolio(UserPortfolio userPortfolio) {
        this.userPortfolio = userPortfolio;
    }

    public LocalDate getRedemptionDate() {
        return redemptionDate;
    }

    public void setRedemptionDate(LocalDate redemptionDate) {
        this.redemptionDate = redemptionDate;
    }

    public BigDecimal getRedemptionAmount() {
        return redemptionAmount;
    }

    public void setRedemptionAmount(BigDecimal redemptionAmount) {
        this.redemptionAmount = redemptionAmount;
    }

    public RedemptionStatus getRedemptionStatus() {
        return redemptionStatus;
    }

    public void setRedemptionStatus(RedemptionStatus redemptionStatus) {
        this.redemptionStatus = redemptionStatus;
    }

    public LocalDate getBrokerBatch() {
        return brokerBatch;
    }

    public void setBrokerBatch(LocalDate brokerBatch) {
        this.brokerBatch = brokerBatch;
    }

    public BigDecimal getAmountRequestedFromBroker() {
        return amountRequestedFromBroker;
    }

    public void setAmountRequestedFromBroker(BigDecimal amountRequestedFromBroker) {
        this.amountRequestedFromBroker = amountRequestedFromBroker;
    }

    public BigDecimal getAmountReceivedFromBroker() {
        return amountReceivedFromBroker;
    }

    public void setAmountReceivedFromBroker(BigDecimal amountReceivedFromBroker) {
        this.amountReceivedFromBroker = amountReceivedFromBroker;
    }

    public BigDecimal getAmountReceivedFees() {
        return amountReceivedFees;
    }

    public void setAmountReceivedFees(BigDecimal amountReceivedFees) {
        this.amountReceivedFees = amountReceivedFees;
    }

    public BigDecimal getNetRedemptionAmount() {
        return netRedemptionAmount;
    }

    public void setNetRedemptionAmount(BigDecimal netRedemptionAmount) {
        this.netRedemptionAmount = netRedemptionAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDate getRedemptionCompletedEmailSent() {
        return redemptionCompletedEmailSent;
    }

    public void setRedemptionCompletedEmailSent(LocalDate redemptionCompletedEmailSent) {
        this.redemptionCompletedEmailSent = redemptionCompletedEmailSent;
    }

}
