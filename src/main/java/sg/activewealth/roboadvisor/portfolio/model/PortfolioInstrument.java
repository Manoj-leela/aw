package sg.activewealth.roboadvisor.portfolio.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.trade.enums.TradePosition;

@Entity
@Table(name = "portfolio_instrument")
public class PortfolioInstrument extends AbstractModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    @JsonIgnore
    public Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

    private BigDecimal weightage;
    private TradePosition tradePosition;

    @Transient
    @JsonIgnore
    private boolean inUse = false;

    @Transient
    private String assetLabel;

    public PortfolioInstrument() {
    }

    public PortfolioInstrument(String id) {
        super(id);
    }

    public PortfolioInstrument(Instrument instrument, BigDecimal weightage) {
        this.weightage = weightage;
        this.instrument = instrument;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    /**
     * @return the instrument
     */
    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * @param instrument
     *            the instrument to set
     */
    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    /**
     * @return the weightage
     */
    public BigDecimal getWeightage() {
        return weightage;
    }

    /**
     * @param weightage
     *            the weightage to set
     */
    public void setWeightage(BigDecimal weightage) {
        this.weightage = weightage;
    }

    public TradePosition getTradePosition() {
        return tradePosition;
    }

    public void setTradePosition(TradePosition tradePosition) {
        this.tradePosition = tradePosition;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public String getAssetLabel() {
        return assetLabel;
    }

    public void setAssetLabel(String assetLabel) {
        this.assetLabel = assetLabel;
    }
}
