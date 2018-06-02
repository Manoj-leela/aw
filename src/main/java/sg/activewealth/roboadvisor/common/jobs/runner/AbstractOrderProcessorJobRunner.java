package sg.activewealth.roboadvisor.common.jobs.runner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.banking.service.RedemptionService;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.dto.FilterDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.helper.broker.BrokerIntegrationServicesFactory;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioTransactionStatus;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolioTransaction;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;
import sg.activewealth.roboadvisor.trade.enums.TradeStatus;
import sg.activewealth.roboadvisor.trade.model.UserTrade;
import sg.activewealth.roboadvisor.trade.service.UserTradeService;

@Component
public abstract class AbstractOrderProcessorJobRunner {

    private Logger logger = Logger.getLogger(AbstractOrderProcessorJobRunner.class);

    @Autowired
    protected UserTradeService userTradeService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserPortfolioService userPortfolioService;

    @Autowired
    protected PropertiesHelper propertiesHelper;
    
    @Autowired
    protected BrokerIntegrationServicesFactory brokerIntegrationServicesFactory;
    
    @Autowired
    RedemptionService redemptionServie;

    public void updateUserPortfolioStatusForPlaceOrderTrades(final UserPortfolio userPortfolio, final TradeStatus successfulTradeStatus, final UserPortfolioStatus successfulPortfolioStatus, final UserPortfolioTransactionStatus successfulTradeTransactionStatus,
            final UserPortfolioStatus partialSuccessfulPortfolioStatus, final UserPortfolioTransactionStatus partialSuccessTradeTransactionStatus) {
        PagingDto<UserTrade> userTradePagingDto = new PagingDto<>();
        final List<FilterDto> filterDtoList = new ArrayList<>();

        final Object[] userPortfolios = new Object[1];
        userPortfolios[0] = userPortfolio.getId();
        filterDtoList.add(new FilterDto("userPortfolio.id", FilterDto.Operetor.EQ, userPortfolios));
        if (successfulPortfolioStatus.equals(UserPortfolioStatus.Closed)) {
            filterDtoList.add(new FilterDto("status", FilterDto.Operetor.IN, getClosedTradeStatusToProcess()));
        }
        if (successfulPortfolioStatus.equals(UserPortfolioStatus.Executed)) {
            filterDtoList.add(new FilterDto("status", FilterDto.Operetor.IN, getPlaceOrderTradeStatusToProcess()));
        }
        userTradePagingDto.setFilters(filterDtoList);
        final List<UserTrade> userTradesForProvidePortfolio = userTradeService.retrieve(null, userTradePagingDto).getResults();
        final boolean allTradesSuccessful = userTradesForProvidePortfolio.stream().allMatch(userTrade -> userTrade.getExecutionStatus().equals(successfulTradeStatus));

        // Audit to transaction table
        final UserPortfolioTransaction userPortfolioTransaction = new UserPortfolioTransaction();
        userPortfolioTransaction.setLastRetryOn(LocalDateTime.now());
        userPortfolioTransaction.setCreatedOn(LocalDateTime.now());
        userPortfolioTransaction.setCreatedBy(userService.getSystemUser().getId());
        userPortfolioTransaction.setUserPortfolio(userPortfolio);

        if (allTradesSuccessful) {
            userPortfolio.setExecutionStatus(successfulPortfolioStatus);
            userPortfolio.setUpdatedOn(LocalDateTime.now());
            userPortfolioTransaction.setStatus(successfulTradeTransactionStatus);
        } else {
            userPortfolio.setExecutionStatus(partialSuccessfulPortfolioStatus);
            userPortfolio.setUpdatedOn(LocalDateTime.now());
            userPortfolioTransaction.setStatus(partialSuccessTradeTransactionStatus);
        }

        userPortfolioService.saveWithAudit(userPortfolio, userPortfolioTransaction);
    }

    protected List<UserTrade> getTradesOfFirstUserPortfolioFromStatus(final TradeStatus tradeStatus) {
        final PagingDto<UserTrade> userTradePagingDto = new PagingDto<>();
        final List<FilterDto> filterDtoList = new ArrayList<>();

        final Object[] tradeStatusValue = new Object[1];
        tradeStatusValue[0] = tradeStatus;
        filterDtoList.add(new FilterDto("executionStatus", FilterDto.Operetor.EQ, tradeStatusValue));
        userTradePagingDto.setFilters(filterDtoList);
        // We need first entry of trade and then we will fetch list of
        // User_Trade from
        // UserPortfolio of first result of UserTrade
        userTradePagingDto.setResultsPerPage(1);
        final List<UserTrade> userTrades = userTradeService.retrieve("createdOn", userTradePagingDto).getResults();
        if (!userTrades.isEmpty()) {
            return getUserTradesFromUserPortfolio(userTrades.get(0).getUserPortfolio(), tradeStatus);
        }
        return Collections.emptyList();
    }

    private List<UserTrade> getUserTradesFromUserPortfolio(final UserPortfolio userPortfolio, final TradeStatus tradeStatus) {
        final PagingDto<UserTrade> userTradePagingDto = new PagingDto<>();
        final List<FilterDto> filterDtoList = new ArrayList<>();

        final Object[] userPortfolioId = new Object[1];
        userPortfolioId[0] = userPortfolio.getId();

        final Object[] tradeStatusValue = new Object[1];
        tradeStatusValue[0] = tradeStatus;

        filterDtoList.add(new FilterDto("userPortfolio.id", FilterDto.Operetor.EQ, userPortfolioId));
        filterDtoList.add(new FilterDto("status", FilterDto.Operetor.EQ, tradeStatusValue));
        userTradePagingDto.setFilters(filterDtoList);
        return userTradeService.retrieve("createdOn", userTradePagingDto).getResults();
    }

    private TradeStatus[] getClosedTradeStatusToProcess() {
        TradeStatus[] tradeStatuses = { TradeStatus.CloseOrderRequest, TradeStatus.CloseOrderInProgress, TradeStatus.CloseOrderError, TradeStatus.CloseOrderCompleted, TradeStatus.ManualInteractionRequired };
        return tradeStatuses;
    }

    private TradeStatus[] getPlaceOrderTradeStatusToProcess() {
        TradeStatus[] tradeStatuses = { TradeStatus.PlaceOrderRequest, TradeStatus.PlaceOrderProgress, TradeStatus.PlaceOrderError, TradeStatus.PlaceOrderCompleted, TradeStatus.ManualInteractionRequired };
        return tradeStatuses;
    }

    @Async
    public void updateUserPortfolioStatusForPlaceOrderTradesAsync(final UserPortfolio userPortfolio, final TradeStatus successfulTradeStatus, final UserPortfolioStatus successfulPortfolioStatus,
            final UserPortfolioTransactionStatus successfulTradeTransactionStatus, final UserPortfolioStatus partialSuccessfulPortfolioStatus, final UserPortfolioTransactionStatus partialSuccessTradeTransactionStatus) {

        PagingDto<UserTrade> userTradePagingDto = new PagingDto<>();
        final List<FilterDto> filterDtoList = new ArrayList<>();

        final Object[] userPortfolios = new Object[1];
        userPortfolios[0] = userPortfolio.getId();
        filterDtoList.add(new FilterDto("userPortfolio.id", FilterDto.Operetor.EQ, userPortfolios));
        if (successfulPortfolioStatus.equals(UserPortfolioStatus.Closed)) {
            filterDtoList.add(new FilterDto("status", FilterDto.Operetor.IN, getClosedTradeStatusToProcess()));
        }
        if (successfulPortfolioStatus.equals(UserPortfolioStatus.Executed)) {
            filterDtoList.add(new FilterDto("status", FilterDto.Operetor.IN, getPlaceOrderTradeStatusToProcess()));
        }
        userTradePagingDto.setFilters(filterDtoList);
        final List<UserTrade> userTradesForProvidePortfolio = userTradeService.retrieve(null, userTradePagingDto).getResults();
        final boolean allTradesSuccessful = userTradesForProvidePortfolio.stream().allMatch(userTrade -> userTrade.getExecutionStatus().equals(successfulTradeStatus));

        logger.debug("All trade successful value is:" + allTradesSuccessful);

        if (allTradesSuccessful) {
            logger.debug("Since all trades are successful so adding audit entry");
            // Audit to transaction table
            final UserPortfolioTransaction userPortfolioTransaction = new UserPortfolioTransaction();
            userPortfolioTransaction.setLastRetryOn(LocalDateTime.now());
            userPortfolioTransaction.setCreatedOn(LocalDateTime.now());
            userPortfolioTransaction.setCreatedBy(userService.getSystemUser().getId());
            userPortfolioTransaction.setUserPortfolio(userPortfolio);
            
            if (UserPortfolioStatus.Closed.equals(successfulPortfolioStatus)) {
                userPortfolioService.calculateRealisedPnL(userPortfolio);
            }

            userPortfolio.setExecutionStatus(successfulPortfolioStatus);
            userPortfolio.setUpdatedOn(LocalDateTime.now());
            userPortfolioTransaction.setStatus(successfulTradeTransactionStatus);
            userPortfolioService.saveWithAudit(userPortfolio, userPortfolioTransaction);
        }
        
    }

}
