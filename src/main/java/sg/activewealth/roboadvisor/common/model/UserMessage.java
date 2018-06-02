package sg.activewealth.roboadvisor.common.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity
public class UserMessage extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "sender_user_id", nullable = false)
    private User senderUser;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    private User receiverUser;

    private String message;
    private Boolean delivered;
    private Boolean read;

    public User getSenderUser() {
        return senderUser;
    }

    /**
     * @param senderUser
     *            the sender to set
     */
    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    /**
     * @return the receiver
     */
    public User getReceiverUser() {
        return receiverUser;
    }

    /**
     * @param receiverUser
     *            the receiver to set
     */
    public void setReceiverUser(User receiverUser) {
        this.receiverUser = receiverUser;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the delivered
     */
    public Boolean isDelivered() {
        return delivered;
    }

    /**
     * @param delivered
     *            the delivered to set
     */
    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    /**
     * @return the read
     */
    public Boolean isRead() {
        return read;
    }

    /**
     * @param read
     *            the read to set
     */
    public void setRead(Boolean read) {
        this.read = read;
    }

}
