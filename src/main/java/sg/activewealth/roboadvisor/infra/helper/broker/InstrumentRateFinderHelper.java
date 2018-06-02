package sg.activewealth.roboadvisor.infra.helper.broker;

import sg.activewealth.roboadvisor.portfolio.model.Instrument;
import sg.activewealth.roboadvisor.trade.enums.BuySell;

import java.math.BigDecimal;

public interface InstrumentRateFinderHelper {
  BigDecimal getCurrentRate(final Instrument instrument,BuySell buySell);
}
