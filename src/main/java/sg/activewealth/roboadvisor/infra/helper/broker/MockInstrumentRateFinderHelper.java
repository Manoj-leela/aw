package sg.activewealth.roboadvisor.infra.helper.broker;

import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.portfolio.model.Instrument;
import sg.activewealth.roboadvisor.trade.enums.BuySell;

import java.math.BigDecimal;

@Component
public class MockInstrumentRateFinderHelper implements InstrumentRateFinderHelper{
  @Override
  public BigDecimal getCurrentRate(final Instrument instrument,BuySell buySell) {
    return new BigDecimal(Math.random()*100);
  }
}
