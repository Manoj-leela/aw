package sg.activewealth.roboadvisor.portfolio.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.infra.enums.EnumByteUserType;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.portfolio.enums.Goal;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioFundingStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.trade.model.UserTrade;

@Entity
@Table(name = "user_portfolio")
@TypeDefs({ @TypeDef(name = "executionStatus", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.UserPortfolioStatus") }),
        @TypeDef(name = "portfolioFundingStatus", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.portfolio.enums.PortfolioFundingStatus") }) })
public class UserPortfolio extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @OneToMany(mappedBy = "userPortfolio", cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JsonManagedReference
    private List<UserPortfolioQuestionAndAnswer> questionAndAnswerList = new ArrayList<>();
    
    private Goal goal;
    private PortfolioFundingStatus portfolioFundingStatus = PortfolioFundingStatus.NotFundedYet;
    private UserPortfolioStatus executionStatus;
    private BigDecimal netInvestmentAmount;
    private BigDecimal totalUninvestedAmount;
    private BigDecimal realisedPnl;
    private BigDecimal unrealisedPnl;
    private BigDecimal netAssetValue;
    private BigDecimal returns;
    private LocalDate rebalanceStatusOn;

    @ManyToOne
    @JoinColumn(name = "rebalance_target_portfolio_id")
    private Portfolio rebalanceTargetPortfolio;

    @Transient
    private String[] userIds;

    @Transient
    private boolean hasAssignFailed = Boolean.FALSE;

    @Transient
    private List<UserTrade> userTradeList = new ArrayList<>();

    @Transient
    private String remittanceSlipFileName;

    public UserPortfolio() {
    }

    public UserPortfolio(String id) {
        super(id);
    }

    public UserPortfolio(User user, Portfolio portfolio) {
        this.user = user;
        this.portfolio = portfolio;
    }

    /**
     * Returns true if portfolio is making loss.
     * 
     * @return
     */
    public boolean isPortfolioInLoss() {
        if (netAssetValue != null && netInvestmentAmount != null) {
            final BigDecimal profit = netAssetValue.subtract(netInvestmentAmount);
            return profit.compareTo(BigDecimal.ZERO) < 0;
        }
        return false;
    }

    /**
     * Returns true if portfolio loss has exceeded threshold percentage.
     * 
     * @param percentageThreshold
     *            percentageThreshold
     * @return
     */
    public boolean hasLossExceededThreshold(final int percentageThreshold) {
        final BigDecimal thresholdValue = getTotalUninvestedAmount().multiply(new BigDecimal((float) (100 - percentageThreshold) / 100)).setScale(2, RoundingMode.HALF_UP);
        return thresholdValue.compareTo(getReturns()) > 0;
    }

    public void setReturns() {
        this.returns = getNetAssetValue().divide(getNetInvestmentAmount(), PropertiesHelper.SCALE, BigDecimal.ROUND_DOWN).subtract(BigDecimal.ONE).multiply(new BigDecimal(100)).setScale(PropertiesHelper.SCALE, BigDecimal.ROUND_DOWN);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public List<UserPortfolioQuestionAndAnswer> getQuestionAndAnswerList() {
        return questionAndAnswerList;
    }

    public void setQuestionAndAnswerList(List<UserPortfolioQuestionAndAnswer> questionAndAnswerList) {
        this.questionAndAnswerList = questionAndAnswerList;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public PortfolioFundingStatus getPortfolioFundingStatus() {
        return portfolioFundingStatus;
    }

    public void setPortfolioFundingStatus(PortfolioFundingStatus portfolioFundingStatus) {
        this.portfolioFundingStatus = portfolioFundingStatus;
    }

    public UserPortfolioStatus getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(UserPortfolioStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

    public BigDecimal getNetInvestmentAmount() {
        return netInvestmentAmount;
    }

    public void setNetInvestmentAmount(BigDecimal netInvestmentAmount) {
        this.netInvestmentAmount = netInvestmentAmount;
    }

    public BigDecimal getTotalUninvestedAmount() {
        return totalUninvestedAmount;
    }

    public void setTotalUninvestedAmount(BigDecimal totalUninvestedAmount) {
        this.totalUninvestedAmount = totalUninvestedAmount;
    }

    public BigDecimal getRealisedPnl() {
        return realisedPnl;
    }

    public void setRealisedPnl(BigDecimal realisedPnl) {
        this.realisedPnl = realisedPnl;
    }

    public BigDecimal getUnrealisedPnl() {
        return unrealisedPnl;
    }

    public void setUnrealisedPnl(BigDecimal unrealisedPnl) {
        this.unrealisedPnl = unrealisedPnl;
    }

    public BigDecimal getNetAssetValue() {
        return netAssetValue;
    }

    public void setNetAssetValue(BigDecimal netAssetValue) {
        this.netAssetValue = netAssetValue;
    }

    public BigDecimal getReturns() {
        return returns;
    }

    public void setReturns(BigDecimal returns) {
        this.returns = returns;
    }

    public LocalDate getRebalanceStatusOn() {
        return rebalanceStatusOn;
    }

    public void setRebalanceStatusOn(LocalDate rebalanceStatusOn) {
        this.rebalanceStatusOn = rebalanceStatusOn;
    }

    public Portfolio getRebalanceTargetPortfolio() {
        return rebalanceTargetPortfolio;
    }

    public void setRebalanceTargetPortfolio(Portfolio rebalanceTargetPortfolio) {
        this.rebalanceTargetPortfolio = rebalanceTargetPortfolio;
    }

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public boolean isHasAssignFailed() {
        return hasAssignFailed;
    }

    public void setHasAssignFailed(boolean hasAssignFailed) {
        this.hasAssignFailed = hasAssignFailed;
    }

    public List<UserTrade> getUserTradeList() {
        return userTradeList;
    }

    public void setUserTradeList(List<UserTrade> userTradeList) {
        this.userTradeList = userTradeList;
    }

    public String getRemittanceSlipFileName() {
        return remittanceSlipFileName;
    }

    public void setRemittanceSlipFileName(String remittanceSlipFileName) {
        this.remittanceSlipFileName = remittanceSlipFileName;
    }

}
