package sg.activewealth.roboadvisor.trade.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import sg.activewealth.roboadvisor.infra.enums.EnumByteUserType;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.trade.enums.UserTradeTransactionStatus;

@Entity
@Table(name = "user_trade_transaction")
@TypeDefs({ @TypeDef(name = "status", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.UserTradeTransactionStatus") }) })
public class UserTradeTransaction extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "user_trade_id")
    private UserTrade userTrade;
    private String description;
    private LocalDateTime initiateDate;
    private LocalDateTime lastRetryOn;
    private UserTradeTransactionStatus status;

    public UserTrade getUserTrade() {
        return userTrade;
    }

    public void setUserTrade(UserTrade userTradeId) {
        this.userTrade = userTradeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getInitiateDate() {
        return initiateDate;
    }

    public void setInitiateDate(LocalDateTime initiateDate) {
        this.initiateDate = initiateDate;
    }

    public LocalDateTime getLastRetryOn() {
        return lastRetryOn;
    }

    public void setLastRetryOn(LocalDateTime lastRetryOn) {
        this.lastRetryOn = lastRetryOn;
    }

    public UserTradeTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(UserTradeTransactionStatus status) {
        this.status = status;
    }

}
