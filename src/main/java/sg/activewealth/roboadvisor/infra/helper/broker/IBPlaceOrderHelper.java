package sg.activewealth.roboadvisor.infra.helper.broker;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.Types.SecType;

import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.trade.enums.BuySell;
import sg.activewealth.roboadvisor.trade.model.UserTrade;
import sg.activewealth.roboadvisor.trade.service.CustomSequenceService;

@Component
public class IBPlaceOrderHelper implements PlaceOrderHelper {

    private Logger logger = Logger.getLogger(IBPlaceOrderHelper.class);

    private static final String ORDER_TYPE_MKT = "MKT";
    private static final String SEQ_DOMAIN_NAME = "orderId";

    @Autowired
    private PropertiesHelper propertiesHelper;

    @Autowired
    private CustomSequenceService customSequenceService;

    @Override
    public void placeOrder(UserTrade userTrade) {
        TwsApiHelper twsSgaweApiHelper = TwsApiFactory.getSGAWEInstance(propertiesHelper.ibSgaweInstanceHostName, propertiesHelper.ibSgaweInstancePort, 22);
        Order order = createOrder(userTrade, BuySell.Buy);
        int nextReqId = customSequenceService.retrieveNextSeqByDomain(SEQ_DOMAIN_NAME);
        order.orderId(nextReqId);
        logger.info("Order Id for tracking place order is:" + nextReqId);
        userTrade.setRoboOrderId(String.valueOf(nextReqId));
        twsSgaweApiHelper.submitOrder(createContract(userTrade), order);
    }

    private Order createOrder(UserTrade userTrade, BuySell buySell) {
        Order order = new Order();
        order.action(buySell.getLabel().toUpperCase());
        order.orderType(ORDER_TYPE_MKT);
        order.totalQuantity(userTrade.getEnteredUnits().intValue());
        // order.outsideRth(true);
        return order;
    }

    private Contract createContract(UserTrade userTrade) {
        Contract contract = new Contract();
        contract.symbol(userTrade.getPortfolioInstrument().getInstrument().getCode());
        contract.secType(SecType.STK);
        contract.primaryExch(InteractiveBrokerUtil.getInstance().getExchange(userTrade.getPortfolioInstrument().getInstrument()));
        contract.exchange(InteractiveBrokerUtil.getInstance().getExchange());
        contract.currency(InteractiveBrokerUtil.getInstance().getCurreny(userTrade.getPortfolioInstrument().getInstrument()));
        return contract;
    }

    @Override
    public void closeOrder(UserTrade userTrade) {
        TwsApiHelper twsSgaweApiHelper = TwsApiFactory.getSGAWEInstance(propertiesHelper.ibSgaweInstanceHostName, propertiesHelper.ibSgaweInstancePort, 22);
        Order order = createOrder(userTrade, BuySell.Sell);
        int nextReqId = customSequenceService.retrieveNextSeqByDomain(SEQ_DOMAIN_NAME);
        order.orderId(nextReqId);
        userTrade.setRoboOrderId(String.valueOf(nextReqId));
        logger.info("Order Id for tracking Close order is:" + nextReqId);
        twsSgaweApiHelper.submitOrder(createContract(userTrade), order);
    }

}
