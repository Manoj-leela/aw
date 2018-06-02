package sg.activewealth.roboadvisor.portfolio.model;

import java.math.BigDecimal;
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
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioTransactionStatus;

@Entity
@Table(name = "user_portfolio_transaction")
@TypeDefs({ @TypeDef(name = "status", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.UserPortfolioTransactionStatus") }) })
public class UserPortfolioTransaction extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "user_portfolio_id")
    private UserPortfolio userPortfolio;

    private BigDecimal amount;
    private UserPortfolioTransactionStatus status;
    private LocalDateTime lastRetryOn;

    public UserPortfolio getUserPortfolio() {
        return userPortfolio;
    }

    public void setUserPortfolio(final UserPortfolio userPortfolio) {
        this.userPortfolio = userPortfolio;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public UserPortfolioTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(final UserPortfolioTransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastRetryOn() {
        return lastRetryOn;
    }

    public void setLastRetryOn(final LocalDateTime lastRetryOn) {
        this.lastRetryOn = lastRetryOn;
    }
}
