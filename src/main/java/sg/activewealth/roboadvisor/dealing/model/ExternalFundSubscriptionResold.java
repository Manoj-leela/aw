package sg.activewealth.roboadvisor.dealing.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity
@Table(name = "external_fund_subscription_resold")
public class ExternalFundSubscriptionResold extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "external_fund_subscription_id")
    private ExternalFundSubscription externalFundSubscription;

    @ManyToOne
    @JoinColumn(name = "external_fund_price")
    private ExternalFundPrice externalFundPrice;

    private LocalDateTime transactionDate;
    private BigDecimal totalSubscriptionAmount;
    private BigDecimal netInvestAmount;
    private BigDecimal shares;

    public ExternalFundSubscriptionResold() {

    }

    public ExternalFundSubscription getExternalFundSubscription() {
        return externalFundSubscription;
    }

    public void setExternalFundSubscription(ExternalFundSubscription externalFundSubscription) {
        this.externalFundSubscription = externalFundSubscription;
    }

    public ExternalFundPrice getExternalFundPrice() {
        return externalFundPrice;
    }

    public void setExternalFundPrice(ExternalFundPrice externalFundPrice) {
        this.externalFundPrice = externalFundPrice;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getTotalSubscriptionAmount() {
        return totalSubscriptionAmount;
    }

    public void setTotalSubscriptionAmount(BigDecimal totalSubscriptionAmount) {
        this.totalSubscriptionAmount = totalSubscriptionAmount;
    }

    public BigDecimal getNetInvestAmount() {
        return netInvestAmount;
    }

    public void setNetInvestAmount(BigDecimal netInvestAmount) {
        this.netInvestAmount = netInvestAmount;
    }

    public BigDecimal getShares() {
        return shares;
    }

    public void setShares(BigDecimal shares) {
        this.shares = shares;
    }

}
