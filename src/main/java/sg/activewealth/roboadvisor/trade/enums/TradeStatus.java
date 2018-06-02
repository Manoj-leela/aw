package sg.activewealth.roboadvisor.trade.enums;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

public enum TradeStatus implements ByteEnum {
  PlaceOrderRequest(0,"Place Order Request"),
  PlaceOrderProgress(1,"Place Order Inprogress"),
  PlaceOrderCompleted(2,"Place Order Completed"),
  PlaceOrderError(3,"Place Order Error"),
  CloseOrderRequest(4,"Close Order Request"),
  CloseOrderInProgress(5,"Close Order Inprogress"),
  CloseOrderCompleted(6,"Close Order Completed"),
  CloseOrderError(7,"Close Order Error"),
  ManualInteractionRequired(8,"Manual Interaction Required"),
  Draft(9,"Draft"),
  TotalUnitsIsZeroOrLess(10,"TOTAL UNITS IS ZERO OR LESS");
    
    private byte value;
    private String label;

    TradeStatus(int value,String label) {
        this.value = (byte) value;
        this.label = label;
    }

    @Override
    public byte getValue() {
        return this.value;
    }
    public String getLabel() {
    	return label;
    }
    
    public static TradeStatus get(String statusValue) {
    	TradeStatus status = null;
		for (TradeStatus statusValues : TradeStatus.values()) {
			if (statusValues.label.equals(statusValue)) {
				status = statusValues;
				break;
			}
		}
		return status;
	}

    public static UserTradeTransactionStatus getTransactionStatus(final TradeStatus tradeStatus){
      if(tradeStatus == TradeStatus.PlaceOrderCompleted)
        return UserTradeTransactionStatus.TradePlaceOrderCompleted;
      else if (tradeStatus == TradeStatus.PlaceOrderError)
        return UserTradeTransactionStatus.TradePlaceOrderFailed;
      else if(tradeStatus == TradeStatus.PlaceOrderProgress)
        return UserTradeTransactionStatus.TradePlaceOrderInitiated;
      else if(tradeStatus == TradeStatus.CloseOrderInProgress)
        return UserTradeTransactionStatus.TradeCloseOrderInitiated;
      else if(tradeStatus == TradeStatus.CloseOrderError)
        return UserTradeTransactionStatus.TradeCloseOrderFailed;
      else if(tradeStatus == TradeStatus.CloseOrderCompleted)
        return UserTradeTransactionStatus.TradeCloseOrderCompleted;
      else if(tradeStatus == TradeStatus.PlaceOrderRequest)
        return UserTradeTransactionStatus.TradeCreated;

      //TODO:Need to re-confirm this
      else if(tradeStatus == TradeStatus.CloseOrderRequest)
        return UserTradeTransactionStatus.TradeClosed;
      else if(tradeStatus == TradeStatus.Draft)
          return UserTradeTransactionStatus.TradeDrafted;
      else if(tradeStatus == TradeStatus.ManualInteractionRequired)
    	  return UserTradeTransactionStatus.TradeManualRetry;
      else
        throw new IllegalStateException("No mapping found for trade status = "+tradeStatus);
    }
}