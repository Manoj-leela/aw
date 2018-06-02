package sg.activewealth.roboadvisor.infra.helper.broker;

import sg.activewealth.roboadvisor.trade.model.UserTrade;

public interface PlaceOrderHelper {
  void placeOrder(final UserTrade userTrade);
  void closeOrder(final UserTrade userTrade);
}
