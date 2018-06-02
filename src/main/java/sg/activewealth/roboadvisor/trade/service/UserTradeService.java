package sg.activewealth.roboadvisor.trade.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;import org.springframework.web.client.HttpClientErrorException;

import sg.activewealth.roboadvisor.common.jobs.runner.CloseOrderJobRunner;
import sg.activewealth.roboadvisor.common.jobs.runner.PlaceOrderJobRunner;
import sg.activewealth.roboadvisor.common.service.BrokerTransactionService;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.dealing.model.ExternalFundPrice;
import sg.activewealth.roboadvisor.dealing.service.ExternalFundSubscriptionResoldService;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ServiceException;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.helper.broker.BrokerIntegrationServicesFactory;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.portfolio.enums.InstrumentType;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioTransactionStatus;
import sg.activewealth.roboadvisor.portfolio.model.Instrument;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.InstrumentService;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;
import sg.activewealth.roboadvisor.trade.dao.UserTradeDao;
import sg.activewealth.roboadvisor.trade.dao.UserTradeTransactionDao;
import sg.activewealth.roboadvisor.trade.enums.BuySell;
import sg.activewealth.roboadvisor.trade.enums.TradeStatus;
import sg.activewealth.roboadvisor.trade.model.UserTrade;

@Service
public class UserTradeService extends AbstractService<UserTrade> {

    private Logger logger = Logger.getLogger(UserTradeService.class);

    @Autowired
    private UserTradeTransactionDao userTradeTransactionsDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ExternalFundSubscriptionResoldService externalFundSubscriptionResoldService;

    @Autowired
    private BrokerTransactionService brokerTransactionService;

    @Autowired
    private PlaceOrderJobRunner placeOrderJobRunner;
    
    @Autowired
    private CloseOrderJobRunner closeOrderJobRunner;
    
    @Autowired
    private InstrumentService instrumentService;
    
    @Autowired
    private UserPortfolioService userPortfolioService;

    @Autowired
    protected BrokerIntegrationServicesFactory brokerIntegrationServicesFactory;

    public UserTradeService() {
        super(UserTrade.class);
    }

    public UserTradeService(Class<UserTrade> modelClass) {
        super(modelClass);
    }

    @Autowired
    public void setDao(UserTradeDao dao) {
        super.dao = dao;
    }

    public void markUserTradesToGivenStatus(final List<UserTrade> eligibleUserTrades, final TradeStatus tradeStatusToUpdate) {
        for (final UserTrade userTrade : eligibleUserTrades) {
            userTrade.setExecutionStatus(tradeStatusToUpdate);
            userTrade.setUpdatedOn(LocalDateTime.now());
            userTrade.setUpdatedBy(userService.getSystemUser().getId());
            userTradeTransactionsDao.add(TradeStatus.getTransactionStatus(userTrade.getExecutionStatus()), userTrade, "To be decided");
            this.save(userTrade);
        }
    }

    public UserTrade saveWithAudit(final UserTrade userTrade, final String description) throws ValidateException {
        userTradeTransactionsDao.add(TradeStatus.getTransactionStatus(userTrade.getExecutionStatus()), userTrade, description);
        return super.save(userTrade);
    }

    public boolean isPortfolioInstrumentInUse(String portfolioInstrumentId) {
        List<UserTrade> userTradeList = ((UserTradeDao) dao).getUserTradesByPortfolioInstrument(portfolioInstrumentId);
        if (userTradeList != null && !userTradeList.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<UserTrade> getUserTradeByUserPortfolioId(String userPortfolioId) {
        return ((UserTradeDao) dao).getUserTradeByUserPortfolioId(userPortfolioId);
    }

    public List<UserTrade> getCompletedUserTradeByUserPortfolioId(String userPortfolioId) {
        return ((UserTradeDao) dao).getCompletedUserTradeByUserPortfolioId(userPortfolioId);
    }

    public UserTrade getUserTrade(String userPortfolioId, String portfolioInstrumentId) {
        return ((UserTradeDao) dao).getUserTrade(userPortfolioId, portfolioInstrumentId);
    }

    @Override
    public void delete(UserTrade model) {
        userTradeTransactionsDao.deleteUserTradeTransactions(model.getId());
        dao.delete(model);
    }

    public PagingDto<UserTrade> retrieveForListPage(PagingDto<UserTrade> pagingDto, String queryFirstName, String[] executionStatus, LocalDate createdOn) {
        pagingDto = ((UserTradeDao) dao).retrieveForListPage(pagingDto, queryFirstName, executionStatus, createdOn);
        return pagingDto;
    }

    public UserTrade updateTradeStatusForBrokerError(UserTrade userTrade, String errorMsg) {
        if (TradeStatus.PlaceOrderProgress.equals(userTrade.getExecutionStatus()) || TradeStatus.PlaceOrderError.equals(userTrade.getExecutionStatus())) {
            userTrade.setExecutionStatus(TradeStatus.PlaceOrderError);
        } else if (TradeStatus.CloseOrderInProgress.equals(userTrade.getExecutionStatus()) || TradeStatus.CloseOrderError.equals(userTrade.getExecutionStatus())) {
            userTrade.setExecutionStatus(TradeStatus.CloseOrderError);
        }
        userTrade.incrementRetryCount();
        return saveWithAudit(userTrade, errorMsg);
    }

    public UserTrade retreiveByRoboOrderId(String orderId) {
        return ((UserTradeDao) dao).retrieveByRoboOrderId(orderId);
    }

    public List<UserTrade> retrieveByUser(String userId) {
        return ((UserTradeDao) dao).retrieveByUser(userId);
    }

    public void processPlaceOrderTrade(UserTrade userTrade) {
        userTrade.setUpdatedOn(LocalDateTime.now());
        userTrade.setUpdatedBy(userService.getSystemUser().getId());
        UserPortfolio userPortfolio = userTrade.getUserPortfolio();
        try {
            BigDecimal allocatedAmount = (userPortfolio.getNetInvestmentAmount().multiply(userTrade.getPortfolioInstrument().getWeightage()).divide(new BigDecimal(100))).setScale(3, BigDecimal.ROUND_HALF_UP);
            userTrade.setAllocatedAmount(allocatedAmount);
            calculateUnits(userTrade);
            BigDecimal uninvestedAmount = allocatedAmount.subtract(userTrade.getAllocatedAmount());
            if (userTrade.getEnteredUnits().compareTo(BigDecimal.ZERO) > 0) {
                if (InstrumentType.CPFund.equals(userTrade.getPortfolioInstrument().getInstrument().getInstrumentType())) {
                    ExternalFundPrice externalFundPrice = externalFundSubscriptionResoldService.addExternalFundSubscriptionResold(userTrade, BuySell.Buy);
                    userTrade.setEnteredPrice(externalFundPrice.getSellPrice());
                    userTrade.setExecutionStatus(TradeStatus.PlaceOrderCompleted);
                    // Take reference of UserPortfolio From first UserTrade entry
                    // Note:This logic assumes that we will have batch job of UserTrade having same UserPortfolio
                    placeOrderJobRunner.updateUserPortfolioStatusForPlaceOrderTrades(userPortfolio, TradeStatus.PlaceOrderCompleted, UserPortfolioStatus.Executed, UserPortfolioTransactionStatus.UserPortfolioExecuted, UserPortfolioStatus.PartiallyExecuted,
                            UserPortfolioTransactionStatus.UserPortfolioPartiallyExecuted);
                } else if (InstrumentType.Cash.equals(userTrade.getPortfolioInstrument().getInstrument().getInstrumentType())) {
                    userTrade.setExecutionStatus(TradeStatus.PlaceOrderCompleted);
                    userTrade.setEnteredPrice(BigDecimal.ONE);
                    userTrade.setEnteredAmountBeforeFees(userTrade.getEnteredPrice().multiply(userTrade.getEnteredUnits()));
                    userTrade.setEnteredFees(BigDecimal.ZERO);
                    userTrade.setEnteredAmount(userTrade.getEnteredAmountBeforeFees().subtract(userTrade.getEnteredFees()));
                } else if (InstrumentType.ETF.equals(userTrade.getPortfolioInstrument().getInstrument().getInstrumentType())) {
                	userTrade.setExecutionStatus(TradeStatus.PlaceOrderProgress);
                    brokerIntegrationServicesFactory.getPlaceOrderHelper().placeOrder(userTrade);
                }
                saveWithAudit(userTrade, "Order is Placed");
            } else {
                userTrade.setExecutionStatus(TradeStatus.TotalUnitsIsZeroOrLess);
                save(userTrade);
            }
    		// Change the User Portfolio status as Partially Executed as down the line we will 
    		//be changing this back to Executed when it come back from Callback of IB method
    		if (!UserPortfolioStatus.PartiallyExecuted.equals(userPortfolio.getExecutionStatus())) {
    			userPortfolio.setExecutionStatus(UserPortfolioStatus.PartiallyExecuted);
    		}
    	    BigDecimal totalUninvestedAmount = userPortfolio.getTotalUninvestedAmount().add(uninvestedAmount);
            logger.debug("TotalUninvestedAmount is:" + totalUninvestedAmount);
            userPortfolio.setTotalUninvestedAmount(totalUninvestedAmount);
            userPortfolioService.saveWithoutPrePost(userPortfolio);
        } catch (HttpClientErrorException e) {
            logger.error("An unexpected error Status code: " + e.getStatusCode());
            logger.error("An unexpected error Message: " + e.getResponseBodyAsString());
            logger.error("An unexpected error Header: " + e.getResponseHeaders());
            userTrade.setExecutionStatus(TradeStatus.PlaceOrderError);
            userTrade.incrementRetryCount();
            saveWithAudit(userTrade, "An exception has occured, error message is:" + e.getMessage());
            placeOrderJobRunner.updateUserPortfolioStatusForPlaceOrderTrades(userPortfolio, TradeStatus.PlaceOrderCompleted, UserPortfolioStatus.Executed, UserPortfolioTransactionStatus.UserPortfolioExecuted, UserPortfolioStatus.PartiallyExecuted,
                    UserPortfolioTransactionStatus.UserPortfolioPartiallyExecuted);
        } catch (Exception ex) {
            logger.error("An unexpected error has occured:", ex);
            userTrade.setExecutionStatus(TradeStatus.PlaceOrderError);
            userTrade.incrementRetryCount();
            saveWithAudit(userTrade, "An unexpected exception has occured, error message is:" + ex.getMessage());
            placeOrderJobRunner.updateUserPortfolioStatusForPlaceOrderTrades(userPortfolio, TradeStatus.PlaceOrderCompleted, UserPortfolioStatus.Executed, UserPortfolioTransactionStatus.UserPortfolioExecuted, UserPortfolioStatus.PartiallyExecuted,
                    UserPortfolioTransactionStatus.UserPortfolioPartiallyExecuted);
        }
    }

    
    public void calculateUnits(UserTrade userTrade) {
        Instrument instrument = userTrade.getPortfolioInstrument().getInstrument();
        BigDecimal currentPrice = BigDecimal.ZERO;
        // Do not call IB if it is not activated and also do not call it for the
        // CP fund instruments
        if (propertiesHelper.fireExternalCallToBroker && PropertiesHelper.BROKER_TYPE_IB.equalsIgnoreCase(propertiesHelper.brokerType)) {
            instrument = instrumentService.retrieve(instrument.getId());
            if (instrument.getCurrentPrice() == null) {
                throw new ServiceException("Instrument's current rate is not set it is null");
            }
            currentPrice = instrument.getCurrentPrice();
        } else {
            List<Instrument> instruments = new ArrayList<Instrument>();
            instruments.add(instrument);
            Map<String, BigDecimal> instrumentPriceMap = instrumentService.getCurrentRateOfInstrument(instruments, BuySell.Buy);
            currentPrice = instrumentPriceMap.get(instrument.getId());
        }
        if (BigDecimal.ZERO != currentPrice) {
            BigDecimal units = (userTrade.getAllocatedAmount().divideToIntegralValue(currentPrice));
            userTrade.setEnteredUnits(units);
            userTrade.setAllocatedAmount(currentPrice.multiply(units).setScale(PropertiesHelper.SCALE, BigDecimal.ROUND_DOWN));
        }
    }
    
    public void processCloseOrderTrade(final UserTrade userTrade) {
        userTrade.setUpdatedOn(LocalDateTime.now());
        userTrade.setUpdatedBy(userService.getSystemUser().getId());
        UserPortfolio userPortfolio = userTrade.getUserPortfolio();
        try {
            // Broker integration
            // userTrade.setTradeAmountAndFees();
            if (InstrumentType.CPFund.equals(userTrade.getPortfolioInstrument().getInstrument().getInstrumentType())) {
                ExternalFundPrice externalFundPrice = externalFundSubscriptionResoldService.addExternalFundSubscriptionResold(userTrade, BuySell.Sell);
                userTrade.setClosedPrice(externalFundPrice.getSellPrice());
                userTrade.setExecutionStatus(TradeStatus.CloseOrderCompleted);
                saveWithAudit(userTrade, "Order is Placed");
                closeOrderJobRunner.updateUserPortfolioStatusForPlaceOrderTrades(userPortfolio, TradeStatus.CloseOrderCompleted, UserPortfolioStatus.Closed, UserPortfolioTransactionStatus.UserPortfolioClosed, UserPortfolioStatus.PartiallyClosed,
                        UserPortfolioTransactionStatus.UserPortfolioPartiallyClosed);
            } else if (InstrumentType.Cash.equals(userTrade.getPortfolioInstrument().getInstrument().getInstrumentType())) {
                userTrade.setExecutionStatus(TradeStatus.CloseOrderCompleted);
                userTrade.setClosedPrice(BigDecimal.ONE);
                userTrade.setClosedAmountBeforeFees(userTrade.getClosedPrice().multiply(userTrade.getEnteredUnits()));
                userTrade.setClosedFees(BigDecimal.ZERO);
                userTrade.setClosedAmount(userTrade.getClosedAmountBeforeFees().subtract(userTrade.getClosedFees()));
                save(userTrade);
            } else if (InstrumentType.ETF.equals(userTrade.getPortfolioInstrument().getInstrument().getInstrumentType())){
                userTrade.setExecutionStatus(TradeStatus.CloseOrderInProgress);
                brokerIntegrationServicesFactory.getPlaceOrderHelper().closeOrder(userTrade);
                saveWithAudit(userTrade, "Close Order is Placed");
            }
            // Change the User Portfolio status as Partially Closed as down the line we will
            // be changing this back to Closed when it come back from Callback of IB method
            if (!UserPortfolioStatus.PartiallyClosed.equals(userPortfolio.getExecutionStatus())) {
                    userPortfolio.setExecutionStatus(UserPortfolioStatus.PartiallyClosed);
                    userPortfolioService.saveWithoutPrePost(userPortfolio);
            }
        } catch (HttpClientErrorException e) {
            logger.error("Http client error has occurred and status code: " + e.getStatusCode());
            logger.error("Http client error has occurred and error Message: " + e.getResponseBodyAsString());
            logger.error("Http client error has occurred and Header: " + e.getResponseHeaders());
            userTrade.setExecutionStatus(TradeStatus.CloseOrderError);
            userTrade.incrementRetryCount();
            saveWithAudit(userTrade, "Close Order Error");
        } catch (Exception ex) {
            logger.error("An unexpected error has occured:", ex);
            userTrade.setExecutionStatus(TradeStatus.CloseOrderError);
            userTrade.incrementRetryCount();
            saveWithAudit(userTrade, "Close Order Error");
        }
    }

}
