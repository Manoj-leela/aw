package sg.activewealth.roboadvisor.trade.model;

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
import sg.activewealth.roboadvisor.portfolio.model.PortfolioInstrument;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.trade.enums.TradePosition;
import sg.activewealth.roboadvisor.trade.enums.TradeStatus;

@Entity
@Table(name = "user_trade")
@TypeDefs({ @TypeDef(name = "executionStatus", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.TradeStatus") }),
        @TypeDef(name = "tradePosition", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.TradePosition") }) })
public class UserTrade extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "user_portfolio_id")
    private UserPortfolio userPortfolio;

    @ManyToOne
    @JoinColumn(name = "portfolio_instrument_id")
    private PortfolioInstrument portfolioInstrument;
    private BigDecimal allocatedAmount;
    private TradePosition tradePosition;
    private BigDecimal currentPrice;
    private BigDecimal enteredUnits;
    private BigDecimal enteredPrice;
    private BigDecimal enteredAmountBeforeFees;
    private BigDecimal enteredFees;
    private BigDecimal enteredAmount;
    private BigDecimal closedPrice;
    private BigDecimal closedAmountBeforeFees;
    private BigDecimal closedFees;
    private BigDecimal closedAmount;
    private BigDecimal feesBothTradeLegs;
    private TradeStatus executionStatus;
    private Integer retryCount = 0;
    private LocalDateTime lastRetryOn;
    private String brokerOrderId;
    private String roboOrderId;
    private static final int MAX_RETRY = 3;

    public UserTrade() {
    }

    public UserTrade(String id) {
        super(id);
    }

    /**
     * This method increments retry count by 1 and mark status to Manual
     * Interaction Required in case it exceeds maximum retry attempt
     */
    public void incrementRetryCount() {
        setRetryCount(getRetryCount() + 1);
        setLastRetryOn(LocalDateTime.now());

        if (getRetryCount() >= MAX_RETRY) {
            setExecutionStatus(TradeStatus.ManualInteractionRequired);
        }
    }

    public UserPortfolio getUserPortfolio() {
        return userPortfolio;
    }

    public void setUserPortfolio(UserPortfolio userPortfolio) {
        this.userPortfolio = userPortfolio;
    }

    public PortfolioInstrument getPortfolioInstrument() {
        return portfolioInstrument;
    }

    public void setPortfolioInstrument(PortfolioInstrument portfolioInstrument) {
        this.portfolioInstrument = portfolioInstrument;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public TradePosition getTradePosition() {
        return tradePosition;
    }

    public void setTradePosition(TradePosition tradePosition) {
        this.tradePosition = tradePosition;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getEnteredUnits() {
        return enteredUnits;
    }

    public void setEnteredUnits(BigDecimal enteredUnits) {
        this.enteredUnits = enteredUnits;
    }

    public BigDecimal getEnteredPrice() {
        return enteredPrice;
    }

    public void setEnteredPrice(BigDecimal enteredPrice) {
        this.enteredPrice = enteredPrice;
    }

    public BigDecimal getEnteredAmountBeforeFees() {
        return enteredAmountBeforeFees;
    }

    public void setEnteredAmountBeforeFees(BigDecimal enteredAmountBeforeFees) {
        this.enteredAmountBeforeFees = enteredAmountBeforeFees;
    }

    public BigDecimal getEnteredFees() {
        return enteredFees;
    }

    public void setEnteredFees(BigDecimal enteredFees) {
        this.enteredFees = enteredFees;
    }

    public BigDecimal getEnteredAmount() {
        return enteredAmount;
    }

    public void setEnteredAmount(BigDecimal enteredAmount) {
        this.enteredAmount = enteredAmount;
    }

    public BigDecimal getClosedPrice() {
        return closedPrice;
    }

    public void setClosedPrice(BigDecimal closedPrice) {
        this.closedPrice = closedPrice;
    }

    public BigDecimal getClosedAmountBeforeFees() {
        return closedAmountBeforeFees;
    }

    public void setClosedAmountBeforeFees(BigDecimal closedAmountBeforeFees) {
        this.closedAmountBeforeFees = closedAmountBeforeFees;
    }

    public BigDecimal getClosedFees() {
        return closedFees;
    }

    public void setClosedFees(BigDecimal closedFees) {
        this.closedFees = closedFees;
    }

    public BigDecimal getClosedAmount() {
        return closedAmount;
    }

    public void setClosedAmount(BigDecimal closedAmount) {
        this.closedAmount = closedAmount;
    }

    public BigDecimal getFeesBothTradeLegs() {
        return feesBothTradeLegs;
    }

    public void setFeesBothTradeLegs(BigDecimal feesBothTradeLegs) {
        this.feesBothTradeLegs = feesBothTradeLegs;
    }

    public TradeStatus getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(TradeStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public LocalDateTime getLastRetryOn() {
        return lastRetryOn;
    }

    public void setLastRetryOn(LocalDateTime lastRetryOn) {
        this.lastRetryOn = lastRetryOn;
    }

    public String getBrokerOrderId() {
        return brokerOrderId;
    }

    public void setBrokerOrderId(String brokerOrderId) {
        this.brokerOrderId = brokerOrderId;
    }

    public String getRoboOrderId() {
        return roboOrderId;
    }

    public void setRoboOrderId(String roboOrderId) {
        this.roboOrderId = roboOrderId;
    }

}
