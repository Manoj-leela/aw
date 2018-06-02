package sg.activewealth.roboadvisor.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;

//TODO We are not using this entity anymore as per the new design on 23-Feb-18
@Entity
@Table(name = "user_payment_method")
public class UserPaymentMethod extends AbstractModel {

	@Column(name = "payment_method")
	private String paymentMethod;
	
	@Column(name = "card_number")
	private String cardNumber;

	@Column(name = "source_id")
	private String sourceId;

	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;

	/**
	 * @return the paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param paymentMethod
	 *            the paymentMethod to set
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

}
