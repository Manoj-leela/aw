package sg.activewealth.roboadvisor.infra.helper.broker;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.trade.enums.TradeStatus;
import sg.activewealth.roboadvisor.trade.model.UserTrade;

@Component
public class MockPlaceOrderHelper implements PlaceOrderHelper{
  @Override
  public void placeOrder(final UserTrade userTrade) {
    double random =  Math.random();
//    if(random < 0.5){
//      throw new RuntimeException("Broker Integration error");
//    }
    userTrade.setEnteredPrice(new BigDecimal(random).setScale(3, BigDecimal.ROUND_HALF_UP));
    userTrade.setExecutionStatus(TradeStatus.PlaceOrderCompleted);
  }

  @Override
  public void closeOrder(final UserTrade userTrade) {
    double random =  Math.random();
//    if(random < 0.5){
//      throw new RuntimeException("Broker Integration error");
//    }
    userTrade.setEnteredPrice(new BigDecimal(random).setScale(3, BigDecimal.ROUND_HALF_UP));
  }
}
