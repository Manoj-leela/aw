package sg.activewealth.roboadvisor.dealing.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity
@Table(name = "external_fund_price")
public class ExternalFundPrice extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "external_fund_id")
    private ExternalFund externalFund;

    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
    private Boolean dealingPrice = Boolean.FALSE;
    private LocalDate priceDate;

    public ExternalFundPrice() {
        super();
    }

    public ExternalFund getExternalFund() {
        return externalFund;
    }

    public void setExternalFund(ExternalFund externalFund) {
        this.externalFund = externalFund;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Boolean getDealingPrice() {
        return dealingPrice;
    }

    public void setDealingPrice(Boolean dealingPrice) {
        this.dealingPrice = dealingPrice;
    }

    public LocalDate getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(LocalDate priceDate) {
        this.priceDate = priceDate;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ExternalFundPrice [externalFund=");
        builder.append(externalFund);
        builder.append(", buyPrice=");
        builder.append(buyPrice);
        builder.append(", sellPrice=");
        builder.append(sellPrice);
        builder.append(", dealingPrice=");
        builder.append(dealingPrice);
        builder.append(", priceDate=");
        builder.append(priceDate);
        builder.append("]");
        return builder.toString();
    }

}
