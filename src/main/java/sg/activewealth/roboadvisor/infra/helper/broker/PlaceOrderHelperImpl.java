package sg.activewealth.roboadvisor.infra.helper.broker;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.web.rest.AbstractRestClient;
import sg.activewealth.roboadvisor.portfolio.service.InstrumentService;
import sg.activewealth.roboadvisor.trade.enums.BuySell;
import sg.activewealth.roboadvisor.trade.enums.DurationType;
import sg.activewealth.roboadvisor.trade.enums.OrderRelation;
import sg.activewealth.roboadvisor.trade.enums.OrderType;
import sg.activewealth.roboadvisor.trade.model.UserTrade;

@Component
public class PlaceOrderHelperImpl extends AbstractRestClient implements PlaceOrderHelper {

	@Autowired
	private PropertiesHelper propertiesHelper;
	
	@Autowired
	private InstrumentService instrumentService;

	@Override
	public void placeOrder(final UserTrade userTrade) {
		createTrade(BuySell.Buy,userTrade);
	}

	@Override
	public void closeOrder(final UserTrade userTrade) {
		createTrade(BuySell.Sell,userTrade);
	}

	/**
	 * Creates the trade.
	 *
	 * @param instrumentId
	 *            the instrument id
	 * @param assetType
	 *            the asset type
	 * @param buySell
	 *            the buy or sell
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createTrade(BuySell buySell, UserTrade userTrade) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> orderDurationMap = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		//TODO For SAXO integration we do not have implemented close order correctly, instead of taking current prize and
		// selling the Units we need to fire close order call to the broker
		// Below logic would change when we do the broker integration
		BigDecimal currentPrice = instrumentService.getCurrentRateOfInstrument(userTrade.getPortfolioInstrument().getInstrument(),buySell)
				.setScale(3, BigDecimal.ROUND_HALF_UP);
		BigDecimal boughtAmount = BigDecimal.ZERO;
		if(BuySell.Sell.equals(buySell)){
			boughtAmount = currentPrice.multiply(userTrade.getEnteredUnits());
		} else {
			boughtAmount = currentPrice.multiply(userTrade.getEnteredUnits().setScale(2, BigDecimal.ROUND_DOWN)).setScale(3,BigDecimal.ROUND_DOWN);
		}
		userTrade.setAllocatedAmount(boughtAmount);
		userTrade.setEnteredPrice(currentPrice);
		map.put("Uic", userTrade.getPortfolioInstrument().getInstrument().getSaxoInstrumentId());
		map.put("BuySell", buySell.getLabel());
		map.put("AssetType", userTrade.getPortfolioInstrument().getInstrument().getSaxoAssetType());
		map.put("Amount", boughtAmount);
		map.put("OrderType", OrderType.MARKET.getLabel());
		map.put("OrderRelation", OrderRelation.STANDALONE.getLabel());
		orderDurationMap.put("DurationType", DurationType.DayOrder.getLabel());
		map.put("OrderDuration", orderDurationMap);
		map.put("AccountKey", userTrade.getUserPortfolio().getUser().getBrokerSaxoApiKey());

		// Set the API Secret of the user
		Map<String, String> httpHeaders = new HashMap<String, String>();
		httpHeaders.put("Authorization", "Bearer " + userTrade.getUserPortfolio().getUser().getBrokerSaxoApiSecret());
		setHttpHeaders(httpHeaders);

		ResponseEntity<Map> tradeCreateResult = exchange(propertiesHelper.saxoApiCreateTradeUrl, HttpMethod.POST, map,
				Map.class);
		Map<String, Object> responseMap = objectMapper.convertValue(tradeCreateResult.getBody().get("MasterOrder"),
				Map.class);
		String orderId = responseMap.get("OrderId").toString();
		userTrade.setBrokerOrderId(orderId);
	}

}
