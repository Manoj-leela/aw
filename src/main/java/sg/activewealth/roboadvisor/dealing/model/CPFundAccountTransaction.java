package sg.activewealth.roboadvisor.dealing.model;

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
import sg.activewealth.roboadvisor.dealing.enums.TransactionType;
import sg.activewealth.roboadvisor.infra.enums.EnumByteUserType;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity
@Table(name = "cp_fund_account_transacton")
@TypeDefs({ @TypeDef(name = "fundTransactionType", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.TransactionType") }),
        @TypeDef(name = "cpFund", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.Fund") }) })
public class CPFundAccountTransaction extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Fund cpFund;
    private TransactionType fundTransactionType;
    private LocalDateTime transactionOn;

    // TODO ADD transaction bank reference
    private Double transactionAmountUSD;
    private Double transactionFeesAmountUSD;

    public CPFundAccountTransaction() {
    }

    public CPFundAccountTransaction(String id) {
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

    public TransactionType getFundTransactionType() {
        return fundTransactionType;
    }

    public void setFundTransactionType(TransactionType fundTransactionType) {
        this.fundTransactionType = fundTransactionType;
    }

    public LocalDateTime getTransactionOn() {
        return transactionOn;
    }

    public void setTransactionOn(LocalDateTime transactionOn) {
        this.transactionOn = transactionOn;
    }

    public Double getTransactionAmountUSD() {
        return transactionAmountUSD;
    }

    public void setTransactionAmountUSD(Double transactionAmountUSD) {
        this.transactionAmountUSD = transactionAmountUSD;
    }

    public Double getTransactionFeesAmountUSD() {
        return transactionFeesAmountUSD;
    }

    public void setTransactionFeesAmountUSD(Double transactionFeesAmountUSD) {
        this.transactionFeesAmountUSD = transactionFeesAmountUSD;
    }

}
