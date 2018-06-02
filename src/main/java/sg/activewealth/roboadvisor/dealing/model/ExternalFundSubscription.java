package sg.activewealth.roboadvisor.dealing.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity
@Table(name = "external_fund_subscription")
public class ExternalFundSubscription extends AbstractModel {

    @JoinColumn(name = "external_fund_id")
    @ManyToOne
    private ExternalFund externalFund;

    @JoinColumn(name = "external_fund_price")
    @ManyToOne
    private ExternalFundPrice externalFundPrice;
    private BigDecimal totalSubscriptionAmount;
    private BigDecimal netInvestAmount;
    private BigDecimal shares;
    private BigDecimal balanceShares;

    public ExternalFundSubscription() {
        super();
    }

    public ExternalFundPrice getExternalFundPrice() {
        return externalFundPrice;
    }

    public void setExternalFundPrice(ExternalFundPrice externalFundPrice) {
        this.externalFundPrice = externalFundPrice;
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

    public BigDecimal getBalanceShares() {
        return balanceShares;
    }

    public void setBalanceShares(BigDecimal balanceShares) {
        this.balanceShares = balanceShares;
    }

    public ExternalFund getExternalFund() {
        return externalFund;
    }

    public void setExternalFund(ExternalFund externalFund) {
        this.externalFund = externalFund;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ExternalFundSubscription [fund=");
        builder.append(externalFund);
        builder.append(", fundPrice=");
        builder.append(externalFundPrice);
        builder.append(", totalSubscriptionAmount=");
        builder.append(totalSubscriptionAmount);
        builder.append(", netInvestAmount=");
        builder.append(netInvestAmount);
        builder.append(", shares=");
        builder.append(shares);
        builder.append(", balanceShares=");
        builder.append(balanceShares);
        builder.append("]");
        return builder.toString();
    }

}
