package sg.activewealth.roboadvisor.trade.enums;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

public enum UserTradeTransactionStatus implements ByteEnum {
	
    TradeCreated(0,"Trade Created"),
    TradePlaceOrderInitiated(1,"Trade Place Order Initiated"),
    TradePlaceOrderCompleted(2,"Trade Place Order Completed"),
    TradePlaceOrderFailed(3,"Trade Place Order Failed"),
    TradeCloseOrderInitiated(4,"Trade Close Place Initiated"),
    TradeCloseOrderCompleted(5,"Trade Close Order Completed"),
    TradeCloseOrderFailed(6,"Trade Close Order Failed"),
    TradeCloseOrderReschedule(7,"Trade Close Order Reschedule"),
    TradeManualRetry(8,"Trade Manual Retry"),
    TradeRateUpdated(9,"Trade Rate Updated"),
	TradeClosed(10,"Trade Closed"),
	TradeDrafted(11,"Trade Drafted");
	

    private byte value;
    private String label;

    private UserTradeTransactionStatus(int value,String label) {
        this.value = (byte) value;
        this.label = label;
    }
    
    public static UserTradeTransactionStatus get(String statusValue) {
    	UserTradeTransactionStatus status = null;
		for (UserTradeTransactionStatus statusValues : UserTradeTransactionStatus.values()) {
			if (statusValues.label.equals(statusValue)) {
				status = statusValues;
				break;
			}
		}
		return status;
	}

    @Override
    public byte getValue() {
        return this.value;
    }
    public String getLabel() {
    	return label;
    }
}

