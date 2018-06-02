package sg.activewealth.roboadvisor.banking.enums;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

public enum PaymentStatus implements ByteEnum {
	PaymentAuthorizationPending(0,"Pending Payment Authorization"),
	PAYMENT_AUTHORIZED_FAILED(1,"Payment Authorization Failed"),
	ReadyForPayment(2,"Ready For Payment"),
	PaymentInProgress(3,"Payment In progress"), 
	PaymentCompleted(4,"Payment Completed"), 
	PaymentError(5,"Payment Error"),
	ManualInteractionRequired(6,"Manual Interaction Required");
	
    private byte value;
    private String label;

    private PaymentStatus(int value,String label) {
    	 this.value = (byte) value;
        this.label = label;
    }

    public byte getValue() {
        return this.value;
    }
    public String getLabel() {
    	return label;
    }

  public static PaymentTransactionStatus getPaymentTransactionStatus(final PaymentStatus paymentStatus){
    if(paymentStatus == PaymentStatus.PaymentError)
      return PaymentTransactionStatus.PaymentFailed;
    else if (paymentStatus == PaymentStatus.PaymentCompleted)
      return PaymentTransactionStatus.PaymentCompleted;
    else if(paymentStatus == PaymentStatus.PaymentInProgress)
      return PaymentTransactionStatus.PaymentInitiated;
    else if(paymentStatus == PaymentStatus.ReadyForPayment)
    	return PaymentTransactionStatus.PaymentScheduled;
    else if(paymentStatus == PaymentStatus.ManualInteractionRequired)
    	return PaymentTransactionStatus.PaymentManualRetry;
    else
      throw new IllegalStateException("No mapping found for paymentStatus = "+paymentStatus);
  }
}

