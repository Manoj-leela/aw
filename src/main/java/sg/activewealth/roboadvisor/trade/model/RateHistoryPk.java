package sg.activewealth.roboadvisor.trade.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class RateHistoryPk implements Serializable {

  private String instrument;

  private LocalDateTime rateTimeStamp;

  public String getInstrument() {
    return instrument;
  }

  public void setInstrument(final String instrument) {
    this.instrument = instrument;
  }

  public LocalDateTime getRateTimeStamp() {
    return rateTimeStamp;
  }

  public void setRateTimeStamp(final LocalDateTime rateTimeStamp) {
    this.rateTimeStamp = rateTimeStamp;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof RateHistoryPk)) return false;
    final RateHistoryPk that = (RateHistoryPk) o;
    return Objects.equals(getInstrument(), that.getInstrument()) &&
            Objects.equals(getRateTimeStamp(), that.getRateTimeStamp());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getInstrument(), getRateTimeStamp());
  }
}
