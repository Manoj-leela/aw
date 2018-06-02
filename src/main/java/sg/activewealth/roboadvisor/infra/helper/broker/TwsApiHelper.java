package sg.activewealth.roboadvisor.infra.helper.broker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.ib.client.Contract;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.client.TickAttr;
import com.ib.client.TickType;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiConnection.ILogger;
import com.ib.controller.ApiController.IConnectionHandler;
import com.ib.controller.Bar;

import sg.activewealth.roboadvisor.common.jobs.runner.CloseOrderJobRunner;
import sg.activewealth.roboadvisor.common.jobs.runner.PlaceOrderJobRunner;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.helper.broker.CustomApiController.ICustomHistoricalDataHandler;
import sg.activewealth.roboadvisor.infra.helper.broker.CustomApiController.ICustomOrderHandler;
import sg.activewealth.roboadvisor.infra.helper.broker.CustomApiController.ICustomTopMktDataHandler;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioTransactionStatus;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.InstrumentService;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;
import sg.activewealth.roboadvisor.trade.enums.TradeStatus;
import sg.activewealth.roboadvisor.trade.model.UserTrade;
import sg.activewealth.roboadvisor.trade.service.IBReqTrackerService;
import sg.activewealth.roboadvisor.trade.service.UserTradeService;

public class TwsApiHelper implements IConnectionHandler, ICustomOrderHandler, ICustomTopMktDataHandler, ICustomHistoricalDataHandler {

    private Logger logger = Logger.getLogger(TwsApiHelper.class);
    

    private CustomApiController api = null;
    private String host;
    private int port;
    private int clientId;

    private static ApplicationContext appCtx = null;

    IBReqTrackerService ibReqTrackerService = appCtx.getBean(IBReqTrackerService.class);
    InstrumentService instrumentService = appCtx.getBean(InstrumentService.class);
    UserPortfolioService userPortfolioService = appCtx.getBean(UserPortfolioService.class);
    PlaceOrderJobRunner placeOrderJobRunner = appCtx.getBean(PlaceOrderJobRunner.class);
    CloseOrderJobRunner closeOrderJobRunner = appCtx.getBean(CloseOrderJobRunner.class);
    UserTradeService userTradeService = appCtx.getBean(UserTradeService.class);

    Map<Integer, Integer> orderExecTracker = new HashMap<Integer, Integer>();
    
    final List<Integer> ignoreErrors = new ArrayList<Integer>();

    public TwsApiHelper(String host, int port, int clientId) {
        super();
        api = new CustomApiController(this, new TwsApiHelper.TwsLogger("in"), new TwsLogger("out"));
        this.host = host;
        this.port = port;
        this.clientId = clientId;
        ignoreErrors.add(399);
        ignoreErrors.add(404);
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        appCtx = applicationContext;
    }

    public void reqMktData(int reqId, Contract contract) {
        api.reqTopMktData(reqId, contract, "", true, false, this);
    }

    /**
     * Submits the Order to the IB TWS or IB Gateway interface
     * 
     * @param cc
     * @param order
     */
    public void submitOrder(Contract cc, com.ib.client.Order order) {
        if (logger.isDebugEnabled()) {
            logger.debug("submitting order for " + cc.symbol());
            logger.debug("Contract Definition is:" + cc.toString());
            logger.debug("Order definition is:" + order.toString());
            logger.debug("ClientId is:" + clientId);
        }
        order.clientId(clientId);
        api.placeOrModifyOrder(cc, order, this);
    }

    public boolean connect() {
        logger.info("connect called with host:" + host + " and port:" + port + " and clietId is:" + clientId);
        boolean result = true;
        if (!api.client().isConnected()) {
            api.connect(host, port, clientId, "");
        }
        logger.info("Connect call result is:" + result);
        return result;
    }

    public boolean disconnect() {
        logger.info("Disconnect called for the TWS");
        boolean result = true;
        if (api.client().isConnected()) {
            api.disconnect();
        }
        logger.info("Disconnect call result is:" + result);
        return result;
    }

    private class TwsLogger implements ILogger {
        @Override
        public void log(String valueOf) {
            logger.info("[" + type + "]: " + valueOf);
        }

        public TwsLogger(String tt) {
            type = tt;
        }

        private String type;
    }

    @Override
    public void handle(int arg0, String arg1) {

    }

    @Override
    public void orderState(OrderState orderState) {

    }

    @Override
    public void orderStatus(OrderStatus arg0, double arg1, double arg2, double arg3, int arg4, int arg5, double arg6, int arg7, String arg8, double arg9) {
    }

    @Override
    public void accountList(List<String> arg0) {

    }

    @Override
    public void connected() {
    }

    @Override
    public void disconnected() {
    }

    @Override
    public void error(Exception e) {
        logger.error("error: " + e.getMessage());
    }

    @Override
    public void message(int reqId, int errorCode, String errorMsg) {
        if (reqId == -1) {
            logger.info(String.format("Info--> Message Code is =" + errorCode + " Message is =" + errorMsg));
        } else {
            logger.error(String.format("Error ==> Message: id =" + reqId + " errorCode =" + errorCode + " errorMsg = " + errorMsg));
            UserTrade userTrade = userTradeService.retreiveByRoboOrderId(String.valueOf(reqId));
            if (userTrade != null) {
            	if(!ignoreErrors.contains(errorCode)) {
            		logger.debug("Update User trade status accordingly");
                    userTradeService.updateTradeStatusForBrokerError(userTrade, "errorCode =" + errorCode + " errorMsg = " + errorMsg);
                    return;
            	}
            }
        }
    }

    @Override
    public void show(String string) {
        logger.info("show: " + string);
    }

    @Override
    public void tickPrice(TickType tickType, double price, TickAttr attribs) {
        if (TickType.ASK.equals(tickType)) {
            System.out.println("Ask Price is:" + price);
        }
    }

    @Override
    public void tickSize(TickType tickType, int size) {

    }

    @Override
    public void tickString(TickType tickType, String value) {

    }

    @Override
    public void tickSnapshotEnd() {

    }

    @Override
    public void marketDataType(int marketDataType) {

    }

    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {

    }

    @Override
    public void tickPrice(int reqId, TickType tickType, double price, TickAttr attribs) {
//        if (TickType.ASK.equals(tickType)) {
//            Map<String, BigDecimal> currentRate = new HashMap<String, BigDecimal>();
//
//            IBReqTracker ibReqTracker = ibReqTrackerService.retrieveByReqId(reqId);
//            Instrument instrument = instrumentService.retrieveByInstrumentCode(ibReqTracker.getInstrumentId());
//
//            BigDecimal currentRateInDecimal = BigDecimal.valueOf(price);
//            currentRate.put(instrument.getId(), currentRateInDecimal);
//            if (currentRateInDecimal.compareTo(BigDecimal.ZERO) > 0) {
//                instrument.setCurrentRate(currentRateInDecimal);
//                instrumentService.saveWithoutPrePost(instrument);
//            }
//            userPortfolioService.recalculatePortfolio();
//        }
    }

    @Override
    public void orderStatus(int orderId, OrderStatus status, double filled, double remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {

        if (logger.isDebugEnabled()) {
            logger.debug("Robo Order Id:" + orderId + " and status is:" + status.toString());
            logger.debug("Filled value is:" + filled);
            logger.debug("Remaining value is:" + remaining);
            logger.debug("avgFillPrice is:" + avgFillPrice);
            logger.debug("permId is:" + permId);
            logger.debug("parentId is:" + parentId);
            logger.debug("lastFillPrice is:" + lastFillPrice);
            logger.debug("clientId is:" + clientId);
            logger.debug("whyHeld is:" + whyHeld);
            logger.debug("mktCapPrice is:" + mktCapPrice);
        }

        if (OrderStatus.Filled.equals(status)) {

            // This map is used to prevent multiple calls as sometimes IB sends
            // multiple callbacks for the
            // filled status
            orderExecTracker.put(orderId, 1);

            // Get the UserTrade based on orderId
            UserTrade userTrade = userTradeService.retreiveByRoboOrderId(String.valueOf(orderId));
            userTrade.setBrokerOrderId(String.valueOf(permId));
            // If TradeStatus is Place Order In progress or Place Order
            // Error
            // then set the status complete
            // and update the required fields for the User Portfolio
            if ((TradeStatus.PlaceOrderProgress.equals(userTrade.getExecutionStatus()) || TradeStatus.PlaceOrderError.equals(userTrade.getExecutionStatus())) && orderExecTracker.get(orderId) != null && orderExecTracker.get(orderId) == 1) {

                orderExecTracker.put(orderId, 0);

                UserPortfolio userPortfolio = userTrade.getUserPortfolio();

                logger.debug("^^^^ Response received for Placed Order with Id :" + orderId + " ^^^^^");

                // Set the status as completed
                userTrade.setExecutionStatus(TradeStatus.PlaceOrderCompleted);
                
                // Use avgFillPrice
                if (avgFillPrice > 0) {
                    userTrade.setEnteredPrice(BigDecimal.valueOf(avgFillPrice));
                    userTrade.setEnteredAmountBeforeFees(userTrade.getEnteredPrice().multiply(userTrade.getEnteredUnits()));
                    userTrade.setEnteredFees(userTrade.getPortfolioInstrument().getInstrument().getFeesPerTradeLeg().
                            divide(new BigDecimal(100)).multiply(userTrade.getEnteredAmountBeforeFees()).
                            setScale(PropertiesHelper.SCALE, BigDecimal.ROUND_HALF_DOWN));
                    userTrade.setEnteredAmount(userTrade.getEnteredAmountBeforeFees().subtract(userTrade.getEnteredFees()));
                    userTrade.setFeesBothTradeLegs(userTrade.getEnteredFees());
                }

                // Save the User Trade
                userTradeService.saveWithAudit(userTrade, "Placed order");

                // Update User Portfolio Status asynchronously
                placeOrderJobRunner.updateUserPortfolioStatusForPlaceOrderTradesAsync(userPortfolio, TradeStatus.PlaceOrderCompleted, UserPortfolioStatus.Executed, UserPortfolioTransactionStatus.UserPortfolioExecuted,
                        UserPortfolioStatus.PartiallyExecuted, UserPortfolioTransactionStatus.UserPortfolioPartiallyExecuted);

                orderExecTracker.remove(orderId);

                // If Trade status is Close Order In Progress or Close Order
                // Error then set the status complete
                // and update the required fields for the User Portfolio
            } else if (TradeStatus.CloseOrderInProgress.equals(userTrade.getExecutionStatus()) || TradeStatus.CloseOrderError.equals(userTrade.getExecutionStatus())) {

                // Get User Portfolio
                UserPortfolio userPortfolio = userTrade.getUserPortfolio();
                logger.debug("Response received for Closed Order with Id :" + orderId);

                // Set the Order status
                userTrade.setExecutionStatus(TradeStatus.CloseOrderCompleted);
                
                // Set close price as avgFillPrice
                userTrade.setClosedPrice(BigDecimal.valueOf(avgFillPrice));
                
//                BigDecimal currentRate = userTrade.getPortfolioInstrument().getInstrument().getCurrentRate();
                // Use avgFillPrice
                if (avgFillPrice > 0) {
                    userTrade.setClosedPrice(BigDecimal.valueOf(avgFillPrice));
                    userTrade.setClosedAmountBeforeFees(userTrade.getClosedPrice().multiply(userTrade.getEnteredUnits()));
                    userTrade.setClosedFees(userTrade.getPortfolioInstrument().getInstrument().getFeesPerTradeLeg().
                            divide(new BigDecimal(100)).multiply(userTrade.getClosedAmountBeforeFees()).
                            setScale(PropertiesHelper.SCALE, BigDecimal.ROUND_HALF_DOWN));
                    userTrade.setClosedAmount(userTrade.getClosedAmountBeforeFees().subtract(userTrade.getEnteredFees()));
                    BigDecimal feesBothTradeLegs = userTrade.getClosedFees().add(userTrade.getEnteredFees());
                    userTrade.setFeesBothTradeLegs(feesBothTradeLegs);
                }

                // Save User trade
                userTradeService.saveWithAudit(userTrade, "Closed Order");

                closeOrderJobRunner.updateUserPortfolioStatusForPlaceOrderTradesAsync(userPortfolio, TradeStatus.CloseOrderCompleted, UserPortfolioStatus.Closed, UserPortfolioTransactionStatus.UserPortfolioClosed, UserPortfolioStatus.PartiallyClosed,
                        UserPortfolioTransactionStatus.UserPortfolioPartiallyClosed);
            }
        } else if (OrderStatus.Cancelled.equals(status) || OrderStatus.Inactive.equals(status) || OrderStatus.Unknown.equals(status)) {

            UserTrade userTrade = userTradeService.retreiveByRoboOrderId(String.valueOf(orderId));
            userTrade.setBrokerOrderId(String.valueOf(permId));

            if (TradeStatus.PlaceOrderProgress.equals(userTrade.getExecutionStatus()) || TradeStatus.PlaceOrderError.equals(userTrade.getExecutionStatus())) {
                userTrade.setExecutionStatus(TradeStatus.PlaceOrderError);
                //Add the allocated amount to totalUninvestedAmount when there is error. Need to confirm with Jon
//                UserPortfolio userPortfolio = userTrade.getUserPortfolio();
//                BigDecimal totalUninvestedAmount = userPortfolio.getTotalUninvestedAmount().add(userTrade.getAllocatedAmount());
//                userPortfolio.setTotalUninvestedAmount(totalUninvestedAmount);
//                userPortfolioService.saveWithoutPrePost(userPortfolio);
            } else if (TradeStatus.CloseOrderInProgress.equals(userTrade.getExecutionStatus()) || TradeStatus.CloseOrderError.equals(userTrade.getExecutionStatus())) {
                userTrade.setExecutionStatus(TradeStatus.CloseOrderError);
            }
            userTradeService.saveWithAudit(userTrade, "Order status is:" + status);

        } else {
            // Updates the Broker's order id for the other statuses
            UserTrade userTrade = userTradeService.retreiveByRoboOrderId(String.valueOf(orderId));
            if (userTrade.getBrokerOrderId() == null) {
                userTrade.setBrokerOrderId(String.valueOf(permId));
                userTradeService.saveWithoutPrePost(userTrade);
            }
        }
    }

    public void getHistoricalData(int reqId, Contract contract, String endDateTime, String duration, DurationUnit durationUnit, BarSize barSize, WhatToShow whatToShow, boolean rthOnly, boolean keepUpToDate) {
        api.reqHistoricalData(reqId, contract, endDateTime, duration, durationUnit, barSize, whatToShow, rthOnly, keepUpToDate, this);
    }

    @Override
    public void historicalData(Bar bar) {
        System.out.println("Bar high is:" + bar.high());
        System.out.println("Bar Low is:" + bar.low());
        System.out.println("Bar Formatted Time is:" + bar.formattedTime());
        System.out.println("Bar Open is:" + bar.open());
        System.out.println("Bar Close is:" + bar.close());
        System.out.println("Bar volume is:" + bar.volume());
    }

    @Override
    public void historicalDataEnd() {
        System.out.println("Historical Data Ended");
    }

    @Override
    public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) {
        System.out.println("Historical Data Ended reqId:" + reqId + " startDateStr is:" + startDateStr + " and endDateSt:" + endDateStr);
    }

}
