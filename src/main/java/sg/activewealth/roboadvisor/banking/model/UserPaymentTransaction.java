package sg.activewealth.roboadvisor.banking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sg.activewealth.roboadvisor.banking.enums.PaymentTransactionStatus;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;

//TODO We are not using this entity any more as per the new design of screens on 23-Feb-18
@Entity
@Table(name = "user_payment_transaction")
public class UserPaymentTransaction extends AbstractModel {

	@Column(name = "status")
	private PaymentTransactionStatus status;

	@ManyToOne
	@JoinColumn(name = "user_payment_id", nullable = false)
	private UserPayment userPayment;
	
    @Column(name = "description")
    private String description;

    /**
	 * @return the status
	 */
	public PaymentTransactionStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(PaymentTransactionStatus status) {
		this.status = status;
	}

	public UserPayment getUserPayment() {
		return userPayment;
	}

	public void setUserPayment(UserPayment userPayment) {
		this.userPayment = userPayment;
	}
	
	public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

}
