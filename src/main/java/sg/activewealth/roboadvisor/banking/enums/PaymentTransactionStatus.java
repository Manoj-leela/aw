package sg.activewealth.roboadvisor.banking.enums;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

public enum PaymentTransactionStatus implements ByteEnum{
	
	PaymentAuthorized(0,"Payment Authorized"), 
	PaymentAuthorizedFailed(1,"Payment Authorized Failed"),
	PaymentScheduled(2,"Payment Scheduled"), 
	PaymentInitiated(3,"Payment Initiated"),
	PaymentCompleted(4,"Payment Completed"), 
	PaymentFailed(5,"Payment Failed"),
	PaymentManualRetry(6,"Payment Manual Retry");

    private byte value;
    private String label;

    private PaymentTransactionStatus(int value,String label) {
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
}

