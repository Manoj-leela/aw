package sg.activewealth.roboadvisor.banking.model;

import sg.activewealth.roboadvisor.banking.enums.PaymentStatus;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.model.UserPaymentMethod;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//TODO We are not using this entity anymore as per the new design on 23-Feb-18
@Entity
@Table(name = "user_payment")
public class UserPayment extends AbstractModel{

  private static final Integer MAX_RETRY = 3;

  @Column(name = "amount")
  private BigDecimal amount;
  
  @Column(name = "transaction_no")
  private String transactionNo;
  
  @Column(name = "status")
  private PaymentStatus status;

  @Column(name = "last_retry_on")
  private LocalDateTime lastRetryOn;

  @Column(name = "retry_count")
  private Integer retryCount = 0;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;
  
  @OneToOne
  @JoinColumn(name = "payment_method_id")
  private UserPaymentMethod paymentMethod;

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getTransactionNo() {
    return transactionNo;
  }

  public void setTransactionNo(String transactionNo) {
    this.transactionNo = transactionNo;
  }

  public PaymentStatus getStatus() {
    return status;
  }

  public void setStatus(PaymentStatus status) {
    this.status = status;
  }

  public LocalDateTime getLastRetryOn() {
    return lastRetryOn;
  }

  public void setLastRetryOn(LocalDateTime lastRetryOn) {
    this.lastRetryOn = lastRetryOn;
  }

  public Integer getRetryCount() {
    return retryCount;
  }

  public void setRetryCount(Integer retryCount) {
    this.retryCount = retryCount;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public UserPaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(UserPaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  /**
   * This method increments retry count by 1 and mark status to Manual Interaction Required in case it exceeds maximum retry attempt
   */
  public void incrementRetryCount() {
    setRetryCount(getRetryCount()+1);
    setLastRetryOn(LocalDateTime.now());
    if(getRetryCount() >= MAX_RETRY){
      setStatus(PaymentStatus.ManualInteractionRequired);
    }
  }
  
  

}
