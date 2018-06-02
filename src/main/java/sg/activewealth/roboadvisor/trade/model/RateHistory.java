package sg.activewealth.roboadvisor.trade.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "rate_history")
@IdClass(RateHistoryPk.class)
public class RateHistory {

    private static final long serialVersionUID = 121122211444L;

    @Column(name = "rate_timestamp")
    @Id
    private LocalDateTime rateTimeStamp;

    @Id
    private String instrument;
    private String granularity;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private Integer volume;

    public LocalDateTime getRateTimeStamp() {
        return rateTimeStamp;
    }

    public void setRateTimeStamp(final LocalDateTime rateTimeStamp) {
        this.rateTimeStamp = rateTimeStamp;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(final String instrument) {
        this.instrument = instrument;
    }

    public String getGranularity() {
        return granularity;
    }

    public void setGranularity(final String granularity) {
        this.granularity = granularity;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(final BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(final BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(final BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(final BigDecimal close) {
        this.close = close;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(final Integer volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RateHistory))
            return false;
        final RateHistory that = (RateHistory) o;
        return Objects.equals(getInstrument(), that.getInstrument()) && Objects.equals(getRateTimeStamp(), that.getRateTimeStamp());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getInstrument(), getRateTimeStamp());
    }
}