package sg.activewealth.roboadvisor.infra.helper.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;

@Component
public class BrokerIntegrationServicesFactory {

  @Autowired
  private PropertiesHelper propertiesHelper;

  @Autowired
  private MockPlaceOrderHelper mockPlaceOrderHelper;

  @Autowired
  private PlaceOrderHelperImpl placeOrderHelperImpl;

  @Autowired
  private MockInstrumentRateFinderHelper mockInstrumentRateFinderHelper;

  @Autowired
  private InstrumentRateFinderHelperImpl instrumentRateFinderHelperImpl;

  @Autowired
  private IBPlaceOrderHelper ibPlaceOrderHelper;

  @Autowired
  private IBInstrumentRateFinderHelper ibInstrumentRateFinderHelper;
  
  public PlaceOrderHelper getPlaceOrderHelper(){
//	  return propertiesHelper.fireExternalCallToBroker ? placeOrderHelperImpl:mockPlaceOrderHelper;
    return propertiesHelper.fireExternalCallToBroker ? ibPlaceOrderHelper:mockPlaceOrderHelper;
  }
  public InstrumentRateFinderHelper getInstrumentRateFinderHelper(){
   return propertiesHelper.fireExternalCallToBroker ? ibInstrumentRateFinderHelper:mockInstrumentRateFinderHelper;
	//return ibInstrumentRateFinderHelper;
  }
}
