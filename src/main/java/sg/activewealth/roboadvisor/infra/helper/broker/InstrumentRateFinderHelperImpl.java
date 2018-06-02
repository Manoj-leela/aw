package sg.activewealth.roboadvisor.infra.helper.broker;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.web.rest.AbstractRestClient;
import sg.activewealth.roboadvisor.portfolio.model.Instrument;
import sg.activewealth.roboadvisor.trade.enums.BuySell;

@Component
public class InstrumentRateFinderHelperImpl extends AbstractRestClient implements InstrumentRateFinderHelper {
	
	@Autowired
	private PropertiesHelper propertiesHelper;
	@Autowired
	private UserService userService;
	
	@Override
	public BigDecimal getCurrentRate(final Instrument instrument,BuySell buySell) {
		if (instrument.getSaxoInstrumentId() != null && instrument.getCode() != null) {
			String currentPrice = getInstrumentPrice(instrument.getSaxoInstrumentId(), instrument.getSaxoAssetType(), buySell);
			return new BigDecimal(currentPrice);
		} else {
			return new BigDecimal(0.0);
		}
	}
	
	/**
	 * Gets the instrument price.
	 *
	 * @param instrumentId the instrument id
	 * @param assetType the asset type
	 * @return the instrument price
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getInstrumentPrice(String instrumentId, String assetType, BuySell buySell) {
		String uri = propertiesHelper.saxoApiUpdateRateUrl + "?Uics=" + instrumentId + "&FieldGroups=Quote&AssetType="
				+ assetType;
		ObjectMapper objectMapper = new ObjectMapper();
		String instrumentPrice = null;
		Map<String, String> httpHeaders = new HashMap<String, String>();
		User user = userService.retrieveByEmailAddress(propertiesHelper.systemUserEmailId);
		httpHeaders.put("Authorization", "Bearer " + user.getBrokerSaxoApiSecret());
		setHttpHeaders(httpHeaders);
		ResponseEntity<Map> response = exchange(uri, HttpMethod.GET, null, Map.class);
		Object responseObject = response.getBody().get("Data");
		List<Map<String,Object>> listOfMap = objectMapper.convertValue(responseObject, List.class);
		if(!listOfMap.isEmpty() && listOfMap.size() > 0){
			Map<String, Object> priceMap = objectMapper.convertValue(listOfMap.get(0).get("Quote"), Map.class);
			if(BuySell.Buy.equals(buySell)){
				instrumentPrice = priceMap.get("Ask").toString();
			} else {
				instrumentPrice = priceMap.get("Bid").toString();
			}
		}
		return instrumentPrice;
	}

}
