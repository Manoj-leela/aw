package sg.activewealth.roboadvisor.dealing.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.dealing.enums.Fund;
import sg.activewealth.roboadvisor.infra.enums.EnumByteUserType;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity
@Table(name = "cp_fund_account")
@TypeDefs({ @TypeDef(name = "cpFund", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.Fund") }) })
public class CPFundAccount extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Fund cpFund;
    private LocalDateTime subscribedOn;
    private BigDecimal subscriptionAmount;
    private LocalDateTime redeemedOn;
    private BigDecimal redemptionAmount;

    public CPFundAccount() {
    }

    public CPFundAccount(String id) {
        super(id);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Fund getCpFund() {
        return cpFund;
    }

    public void setCpFund(Fund cpFund) {
        this.cpFund = cpFund;
    }

    public LocalDateTime getSubscribedOn() {
        return subscribedOn;
    }

    public void setSubscribedOn(LocalDateTime subscribedOn) {
        this.subscribedOn = subscribedOn;
    }

    public BigDecimal getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(BigDecimal subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public LocalDateTime getRedeemedOn() {
        return redeemedOn;
    }

    public void setRedeemedOn(LocalDateTime redeemedOn) {
        this.redeemedOn = redeemedOn;
    }

    public BigDecimal getRedemptionAmount() {
        return redemptionAmount;
    }

    public void setRedemptionAmount(BigDecimal redemptionAmount) {
        this.redemptionAmount = redemptionAmount;
    }

}
