package sg.activewealth.roboadvisor.portfolio.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.common.dao.UserDao;
import sg.activewealth.roboadvisor.common.enums.UserProgressStatus;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.FilterDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ObjectNotFoundException;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.portfolio.dao.UserPortfolioDao;
import sg.activewealth.roboadvisor.portfolio.dao.UserPortfolioTransactionDao;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioTransactionStatus;
import sg.activewealth.roboadvisor.portfolio.model.AssetClassAllocation;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.model.PortfolioInstrument;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolioQuestionAndAnswer;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolioTransaction;
import sg.activewealth.roboadvisor.trade.dao.UserTradeDao;
import sg.activewealth.roboadvisor.trade.dao.UserTradeTransactionDao;
import sg.activewealth.roboadvisor.trade.enums.TradeStatus;
import sg.activewealth.roboadvisor.trade.model.UserTrade;
import sg.activewealth.roboadvisor.trade.service.UserTradeService;

@Service
public class UserPortfolioService extends AbstractService<UserPortfolio> {

    public UserPortfolioService() {
        super(UserPortfolio.class);
    }

    public UserPortfolioService(Class<UserPortfolio> modelClass) {
        super(modelClass);
    }

    @Autowired
    public void setDao(UserPortfolioDao dao) {
        super.dao = dao;
    }

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserTradeDao userTradeDao;

    @Autowired
    private UserTradeTransactionDao userTradeTransactionsDao;

    @Autowired
    private UserPortfolioTransactionDao userPortfolioTransactionDao;

    @Autowired
    private UserPortfolioTransactionService userPortfolioTransactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTradeService userTradeService;

    @Autowired
    private PortfolioService portfolioService;

    public UserPortfolio saveWithAudit(final UserPortfolio userPortfolio, final UserPortfolioTransaction userPortfolioTransaction) {
        userPortfolioTransactionService.save(userPortfolioTransaction);
        return super.saveWithoutPrePost(userPortfolio);
    }

    /**
     * Retrieves UserPortfolio By status.
     * 
     * @param userPortfolioStatus
     *            userPortfolioStatus
     * @return List of UserPortfolio having given status.
     */
    public List<UserPortfolio> getPortfoliosByStatus(final UserPortfolioStatus[] userPortfolioStatus) {
        return ((UserPortfolioDao) dao).retrieveByStatus(userPortfolioStatus);
    }

    public List<String> getPortfolioIdsByStatus(final UserPortfolioStatus userPortfolioStatus) {
        return ((UserPortfolioDao) dao).retrieveIdsByStatus(userPortfolioStatus);
    }

    public List<UserPortfolio> retrieveByUserIdAndStatus(final UserPortfolioStatus userPortfolioStatus, String userId) {
        List<UserPortfolio> userPortfolios = ((UserPortfolioDao) dao).retrieveByUserIdAndStatus(userPortfolioStatus, userId);
        userPortfolios.forEach(userPortfolio -> loadInstruments(userPortfolio));
        userPortfolios.forEach(userPortfolio -> loadAssetClassAllocation(userPortfolio));
        return userPortfolios;
    }

    /**
     * Retrieves UserPortfolio By status and batch size
     * 
     * @param userPortfolioStatus
     * @param batchSize
     * @return List of UserPortfolio
     */
    public List<UserPortfolio> getPortfoliosByStatusAndBatchSize(final UserPortfolioStatus userPortfolioStatus, final int batchSize) {
        PagingDto<UserPortfolio> pagingDto = new PagingDto<>();

        final List<FilterDto> filters = new ArrayList<>();
        Object[] filterValue = new Object[1];
        filterValue[0] = userPortfolioStatus;
        filters.add(new FilterDto("executionStatus", FilterDto.Operetor.EQ, filterValue));
        pagingDto.setFilters(filters);
        pagingDto.setResultsPerPage(batchSize);
        pagingDto = retrieve("createdOn desc", pagingDto, true);
        return pagingDto.getResults();
    }

    @Override
    public UserPortfolio preSave(UserPortfolio model) {
        model = super.preSave(model);

        for (UserPortfolioQuestionAndAnswer userPortfolioQuestionAndAnswers : model.getQuestionAndAnswerList()) {
            userPortfolioQuestionAndAnswers.setUserPortfolio(model);
        }
        return model;
    }

    @Override
    public UserPortfolio postSave(UserPortfolio model) {
        model = super.postSave(model);
        if (model.getCreatingNewObject()) {
            setTradesForUserPortfolio(model);
        }
        return model;
    }

    public UserPortfolio setTradesForUserPortfolio(UserPortfolio model) {
        for (final PortfolioInstrument portfolioInstrument : model.getPortfolio().getPortfolioInstruments()) {
            UserTrade userTrade = new UserTrade();
            userTrade.setPortfolioInstrument(portfolioInstrument);
            userTrade.setUserPortfolio(model);
            userTrade.setTradePosition(portfolioInstrument.getTradePosition());
            userTrade.setExecutionStatus(TradeStatus.Draft);
            userTradeDao.save(userTrade);
            // TODO:Set description
            userTradeTransactionsDao.add(TradeStatus.getTransactionStatus(userTrade.getExecutionStatus()), userTrade, "Description");
        }
        userPortfolioTransactionDao.add(UserPortfolioTransactionStatus.UserPortfolioAssigned, model, null);
        return model;
    }

    /**
     * Get users user portfolio
     * 
     * @param user
     * @return
     */
    public PagingDto<UserPortfolio> retriveByUser(User user, PagingDto<UserPortfolio> pagingDto) {
        PagingDto<UserPortfolio> userPortfolios = ((UserPortfolioDao) dao).retrive(user.getId(), pagingDto);
        if (userPortfolios.getResults() != null && userPortfolios.getResults().size() > 0) {
            userPortfolios.getResults().forEach(userPortfolio -> loadInstruments(userPortfolio));
            userPortfolios.getResults().forEach(userPortfolio -> loadAssetClassAllocation(userPortfolio));
        }
        return userPortfolios;
    }

    public List<UserPortfolio> retrieveByUserIdAndPortfolioStatus(String userId, PagingDto<UserPortfolio> pagingDto) {
        List<UserPortfolio> userPortfolios = ((UserPortfolioDao) dao).retriveByUser(userId, pagingDto);
        userPortfolios.forEach(userPortfolio -> loadInstruments(userPortfolio));
        userPortfolios.forEach(userPortfolio -> loadAssetClassAllocation(userPortfolio));
        return userPortfolios;
    }

    @Override
    public UserPortfolio retrieve(final String id, final boolean fullInit) {
        final UserPortfolio userPortfolio = super.retrieve(id, fullInit);
        if (userPortfolio == null) {
            throw new ObjectNotFoundException("error.object.notfound");
        }
        loadInstruments(userPortfolio);
        loadAssetClassAllocation(userPortfolio);
        return userPortfolio;
    }

    public void createTradesForFundedPortfolio(final UserPortfolio userPortfolioDetachedEntity) {
        // Pre-requisite
        if (userPortfolioDetachedEntity.getExecutionStatus() != UserPortfolioStatus.Funded)
            throw new IllegalArgumentException("Not valid userportfolio status =" + userPortfolioDetachedEntity.getExecutionStatus());

        // This is to Fix issue of LazyInitialization because of Detached entity
        final UserPortfolio userPortfolio = retrieve(userPortfolioDetachedEntity.getId(), true);
        final List<UserTrade> userTradeList = userTradeService.getUserTradeByUserPortfolioId(userPortfolio.getId());
        for (UserTrade userTrade : userTradeList) {
            userTrade.setExecutionStatus(TradeStatus.PlaceOrderRequest);
            userTradeDao.save(userTrade);
            userTradeTransactionsDao.add(TradeStatus.getTransactionStatus(userTrade.getExecutionStatus()), userTrade, "Description");
        }
        userPortfolio.setExecutionStatus(UserPortfolioStatus.NotExecuted);
        dao.save(userPortfolio);
        userPortfolioTransactionDao.add(UserPortfolioTransactionStatus.UserPortfolioNotExecuted, userPortfolio, null);
    }

    public void createTradesForCloseRequest(final UserPortfolio userPortfolioDetachedEntity) {
        // Pre-requisite
        if (userPortfolioDetachedEntity.getExecutionStatus() != UserPortfolioStatus.CloseRequested && userPortfolioDetachedEntity.getExecutionStatus() != UserPortfolioStatus.ReadyForRebalance
                && userPortfolioDetachedEntity.getExecutionStatus() != UserPortfolioStatus.AssignedForReplace)
            throw new IllegalArgumentException("Not valid user portfolio status =" + userPortfolioDetachedEntity.getExecutionStatus());

        // This is to Fix issue of LazyInitialization because of Detached entity
        final UserPortfolio userPortfolio = retrieve(userPortfolioDetachedEntity.getId(), true);
        List<UserTrade> userTradesList = userTradeService.getCompletedUserTradeByUserPortfolioId(userPortfolio.getId());
        for (UserTrade userTrade : userTradesList) {
            UserTrade userTradeClose = new UserTrade();
            userTradeClose.setPortfolioInstrument(userTrade.getPortfolioInstrument());
            userTradeClose.setUserPortfolio(userPortfolio);
            userTradeClose.setTradePosition(userTrade.getPortfolioInstrument().getTradePosition());
            userTradeClose.setExecutionStatus(TradeStatus.CloseOrderRequest);
            userTradeClose.setCreatedBy(userService.getSystemUser().getId());
            userTradeClose.setCreatedOn(LocalDateTime.now());
            userTradeClose.setEnteredUnits(userTrade.getEnteredUnits());
            userTradeClose.setEnteredPrice(userTrade.getEnteredPrice());
            userTradeClose.setEnteredFees(userTrade.getEnteredFees());
            userTradeDao.save(userTradeClose);
            userTradeTransactionsDao.add(TradeStatus.getTransactionStatus(userTrade.getExecutionStatus()), userTrade, "Close Order Trades Created");
        }
        userPortfolio.setExecutionStatus(UserPortfolioStatus.ReadyForClose);
        dao.save(userPortfolio);
        userPortfolioTransactionDao.add(UserPortfolioTransactionStatus.UserPortfolioReadyForClose, userPortfolio, null);
    }

    private void loadInstruments(final UserPortfolio userPortfolio) {
        // Load lazy initialization classes
        Portfolio portfolio = userPortfolio.getPortfolio();
        if (portfolio != null) {
            portfolio.getPortfolioInstruments().forEach(PortfolioInstrument::getInstrument);
        }
    }

    private void loadAssetClassAllocation(final UserPortfolio userPortfolio) {
        // Load lazy initialization classes
        Portfolio portfolio = userPortfolio.getPortfolio();
        if (portfolio != null) {
            portfolio.getAssetClassAllocations().forEach(AssetClassAllocation::getDisplayName);
        }
    }

    public void loadQueAns(final UserPortfolio userPortfolio) {
        // Load lazy initialization classes
        userPortfolio.getQuestionAndAnswerList().forEach(UserPortfolioQuestionAndAnswer::getAnswer);
        userPortfolio.getQuestionAndAnswerList().forEach(UserPortfolioQuestionAndAnswer::getQuestion);
    }

    public List<UserPortfolio> retrieveByUserStatusAndEmail(UserPortfolioStatus status, String email) {
        return retrieveByUserStatusAndEmailAndPortfolio(status, email, null);
    }

    public List<UserPortfolio> retrieveByUserStatusAndEmailAndPortfolio(UserPortfolioStatus status, String email, Portfolio portfolio) {
        return ((UserPortfolioDao) dao).retrieve(status, email, portfolio);
    }

    @Override
    public ErrorsDto validateForSave(UserPortfolio model, ErrorsDto errors) throws ValidateException {
        errors = super.validateForSave(model, errors);
        if (model.getUser() == null) {
            errors.add(new ErrorDto("user", "error.required", "User"));
        }

        if (model.getCreatingNewObject()) {
            if (model.getPortfolio() != null && model.getUser() != null && isExists(model)) {
                errors.add(new ErrorDto("portfolio", "error.portfolio.exist", "UserPortfolio"));
            }
        }

        if (model.getPortfolio() == null) {
            errors.add(new ErrorDto("portfolio", "error.required", "Portfolio"));
        }

        if (model.getNetInvestmentAmount() != null) {
            if (BigDecimal.ZERO.compareTo(model.getNetInvestmentAmount()) > 0) {
                errors.add(new ErrorDto("netInvestmentAmount", "error.invalid", "Net Investment Amount"));
            }
            if (model.getNetInvestmentAmount().precision() > 15) {
                errors.add(new ErrorDto("totalUninvestedAmount", "error.invalid", "Net Investment Amount"));
            }
        }

        if (model.getTotalUninvestedAmount() != null) {
            if (BigDecimal.ZERO.compareTo(model.getTotalUninvestedAmount()) > 0) {
                errors.add(new ErrorDto("totalUninvestedAmount", "error.invalid", "Total Uninvested Amount"));
            }
            if (model.getTotalUninvestedAmount().precision() > 15) {
                errors.add(new ErrorDto("totalUninvestedAmount", "error.invalid", "Total Uninvested Amount"));
            }
        }
        return errors;
    }

    /**
     * Check if {@link UserPortfolio} exists or not
     * 
     * @param model
     * @return
     */
    public boolean isExists(UserPortfolio model) {
        return ((UserPortfolioDao) dao).isExists(model.getUser().getId(), model.getPortfolio().getId());
    }

    public boolean isPortfolioInUse(String portfolioId) {
        List<UserPortfolio> list = ((UserPortfolioDao) dao).getUserPortfolios(portfolioId);
        if (list != null && !list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Iterates through userPortfolios and assigns it as per the action
     * 
     * @param userPortfolioIds
     * @param fromPortfolio
     * @param toPortfolio
     * @param userPortfolioStatus
     */
    public List<UserPortfolio> assignPortfolio(String[] userPortfolioIds, Portfolio fromPortfolio, Portfolio toPortfolio, UserPortfolioStatus userPortfolioStatus) {

        List<UserPortfolio> assignedUserPortfolios = new ArrayList<UserPortfolio>();

        PagingDto<UserPortfolio> pagingDto = new PagingDto<>();
        pagingDto.getIds().addAll(Arrays.asList(userPortfolioIds));
        List<UserPortfolio> userPortfolios = retrieve("createdOn", pagingDto).getResults();
        for (UserPortfolio userPortfolio : userPortfolios) {
            try {
                UserPortfolio assignedUserPortfolio = assignSinglePortfolio(userPortfolio, fromPortfolio, toPortfolio, userPortfolioStatus);
                assignedUserPortfolios.add(assignedUserPortfolio);
            } catch (Exception ex) {
                logger.warn("UserPortfolio assingment failed for userportfolioId:" + userPortfolio.getId());
                userPortfolio.setHasAssignFailed(Boolean.TRUE);
            }
        }
        return assignedUserPortfolios;
    }

    /**
     * Assigns individual UserPortfolio
     * 
     * @param userPortfolio
     * @param fromPortfolio
     * @param toPortfolio
     * @param userPortfolioStatus
     */
    public UserPortfolio assignSinglePortfolio(UserPortfolio userPortfolio, Portfolio fromPortfolio, Portfolio toPortfolio, UserPortfolioStatus userPortfolioStatus) {
        if (fromPortfolio != null && toPortfolio != null) {
            userPortfolio.setRebalanceTargetPortfolio(toPortfolio);
        } else if (toPortfolio != null) {
            userPortfolio.setPortfolio(toPortfolio);
        }
        userPortfolio.setExecutionStatus(userPortfolioStatus);
        return save(userPortfolio);
    }

    /**
     * Mark userPortfolioDetached from Assigned to Funded if eligible
     * 
     * @param userPortfolio
     *            userPortfolioDetached domain entity
     */
    public void fundUserPortfolioIfEligible(final UserPortfolio userPortfolio) {
        if (userPortfolio.getExecutionStatus() != UserPortfolioStatus.Assigned)
            throw new IllegalArgumentException("UserPortfolio Status must be Assigned to mark as funded but founded:" + userPortfolio.getExecutionStatus());

        // If user's balance is greater than portfolio's amount then mark as
        // funded and deduct that amount from user's balance.
        final User user = userPortfolio.getUser();
        final BigDecimal userBalance = user.getAccountSummary();
        if (userBalance != null && userPortfolio.getNetInvestmentAmount() != null && userBalance.compareTo(userPortfolio.getNetInvestmentAmount()) >= 0) {
            user.setAccountSummary(userBalance.subtract(userPortfolio.getNetInvestmentAmount()));
            user.setProgressStatus(UserProgressStatus.Home);
            userDao.save(user);
            userPortfolio.setExecutionStatus(UserPortfolioStatus.Funded);
            userPortfolio.setTotalUninvestedAmount(userPortfolio.getNetInvestmentAmount());
            dao.save(userPortfolio);
            userPortfolioTransactionDao.add(UserPortfolioTransactionStatus.UserPortfolioFunded, userPortfolio, null);
        }
    }

    /**
     * Release fund of closed portfolio and add balance to User account summary.
     * 
     * @param userPortfolio
     *            userPortfolio
     */
    public void releaseFundOfPortfolio(final UserPortfolio userPortfolio) {
        final User userToAddBalance = userPortfolio.getUser();
        userToAddBalance.addToAccountSummary(userPortfolio.getNetAssetValue());
        userPortfolio.setExecutionStatus(UserPortfolioStatus.ClosedAndReleased);
        userPortfolio.setTotalUninvestedAmount(BigDecimal.ZERO);
        UserPortfolio reassignedPortfolio = null;
        if (userPortfolio.getRebalanceTargetPortfolio() != null) {
            reassignedPortfolio = copyToReassignUserPortfolio(userPortfolio);
            reassignedPortfolio.setPortfolio(portfolioService.retrieve(reassignedPortfolio.getPortfolio().getId()));
            reassignedPortfolio = dao.save(reassignedPortfolio);
            setTradesForUserPortfolio(reassignedPortfolio);
            // TODO:Discuss , do we need to set to null as mentioned in
            // document?
            userPortfolio.setRebalanceTargetPortfolio(null);
        }
        userDao.save(userToAddBalance);
        dao.save(userPortfolio);
        userPortfolioTransactionDao.add(UserPortfolioTransactionStatus.UserPortfolioFundReleased, userPortfolio, null);
    }

    private UserPortfolio copyToReassignUserPortfolio(final UserPortfolio userPortfolio) {
        // Reassign portfolio
        final UserPortfolio reassignedPortfolio = new UserPortfolio();
        // TODO:What to set in balance in case of reassigning?
        // reassignedPortfolio.setBalance(null);
        reassignedPortfolio.setUser(userPortfolio.getUser());
        reassignedPortfolio.setPortfolio(userPortfolio.getRebalanceTargetPortfolio());
        reassignedPortfolio.setExecutionStatus(UserPortfolioStatus.Assigned);
        reassignedPortfolio.setGoal(userPortfolio.getGoal());
        // reassignedPortfolio.setQuestion1(userPortfolio.getQuestion1());
        // reassignedPortfolio.setQuestion2(userPortfolio.getQuestion2());
        // reassignedPortfolio.setQuestion3(userPortfolio.getQuestion3());
        // reassignedPortfolio.setQuestion4(userPortfolio.getQuestion4());
        reassignedPortfolio.setCreatedBy(userService.getSystemUser().getId());
        reassignedPortfolio.setCreatedOn(LocalDateTime.now());
        return reassignedPortfolio;
    }

    public boolean isPortfolioReadyForRebalance(final UserPortfolio userPortfolio) {
        // TODO:It has been decided to hard code value since we dont have
        // definition of ready for rebalance.
        return false;
    }

    public Map<String, BigDecimal> getPortfolioSummary(String userId) {
        Map<String, Object> userPortfolioDetails = new HashMap<>();
        Map<String, BigDecimal> userPortfolioSummary = new HashMap<>();
        BigDecimal totalEarned = new BigDecimal(0);
        BigDecimal totalReturns = new BigDecimal(0);
        BigDecimal totalBalance = new BigDecimal(0);
        BigDecimal totalInvested = new BigDecimal(0);
        userPortfolioDetails = ((UserPortfolioDao) dao).getPortfolioSummary(userId).get(0);
        totalBalance = (BigDecimal) userPortfolioDetails.get("totalBalance");
        totalInvested = (BigDecimal) userPortfolioDetails.get("totalInvested");
        totalReturns = (BigDecimal) userPortfolioDetails.get("totalReturns");
        totalEarned = (BigDecimal) userPortfolioDetails.get("totalEarned");

        userPortfolioSummary.put("totalBalance", totalBalance);
        userPortfolioSummary.put("totalInvested", totalInvested);
        userPortfolioSummary.put("totalReturns", totalReturns);
        userPortfolioSummary.put("totalEarned", totalEarned);
        return userPortfolioSummary;
    }

    public List<UserPortfolioQuestionAndAnswer> getInvestMentQuestions() {
        List<UserPortfolioQuestionAndAnswer> userProfileQuestions = new ArrayList<>();
        userProfileQuestions.add(new UserPortfolioQuestionAndAnswer(propertiesHelper.userannualincomequestion, null));
        userProfileQuestions.add(new UserPortfolioQuestionAndAnswer(propertiesHelper.investmentObjectiveQuestion, null));
        userProfileQuestions.add(new UserPortfolioQuestionAndAnswer(propertiesHelper.riskReturnExpectationsQuestion, null));
        userProfileQuestions.add(new UserPortfolioQuestionAndAnswer(propertiesHelper.volatilityToleranceQuestion, null));
        userProfileQuestions.add(new UserPortfolioQuestionAndAnswer(propertiesHelper.lossToleranceQuestion, null));
        userProfileQuestions.add(new UserPortfolioQuestionAndAnswer(propertiesHelper.personalityAndBehaviorQuestion, null));
        userProfileQuestions.add(new UserPortfolioQuestionAndAnswer(propertiesHelper.timeHorizonQuestion, null));
        userProfileQuestions.add(new UserPortfolioQuestionAndAnswer(propertiesHelper.liquidityConsiderationQuestion1, null));
        userProfileQuestions.add(new UserPortfolioQuestionAndAnswer(propertiesHelper.liquidityConsiderationQuestion2, null));
        return userProfileQuestions;
    }

    public PagingDto<UserPortfolio> retrieveForListPage(PagingDto<UserPortfolio> pagingDto, UserPortfolioStatus userPortfolioStatus, String quryEmailAddress, String queryPortfolioName) {
        pagingDto = ((UserPortfolioDao) dao).retrieveForListPage(pagingDto, userPortfolioStatus, quryEmailAddress, queryPortfolioName);
        return pagingDto;
    }

    public void recalculatePortfolio() {
        // TODO : Retrieve the Paginated records
        final List<UserPortfolio> userPortfolios = getPortfoliosByStatus(new UserPortfolioStatus[] { UserPortfolioStatus.Executed, UserPortfolioStatus.PartiallyExecuted });
        for (UserPortfolio userPortfolio : userPortfolios) {
            final List<UserTrade> userTradesToUpdate = userTradeService.getUserTradeByUserPortfolioId(userPortfolio.getId());
            BigDecimal totalUnRealisedPnL = BigDecimal.ZERO;
            for (UserTrade userTrade : userTradesToUpdate) {
                BigDecimal unRealisedPnL = BigDecimal.ZERO;
                BigDecimal currentRate = userTrade.getPortfolioInstrument().getInstrument().getCurrentPrice();
                if (TradeStatus.PlaceOrderCompleted.equals(userTrade.getExecutionStatus()) && currentRate != null) {
                    BigDecimal difference = currentRate.subtract(userTrade.getEnteredPrice());
                    if (BigDecimal.ZERO.compareTo(difference) != 0) {
                        BigDecimal tradeValue = difference.multiply(userTrade.getEnteredUnits()).setScale(PropertiesHelper.SCALE, BigDecimal.ROUND_DOWN);
                        unRealisedPnL = tradeValue.subtract(userTrade.getFeesBothTradeLegs());
                        if (logger.isDebugEnabled()) {
                            logger.debug("units are:" + userTrade.getEnteredUnits());
                            logger.debug("Current rate is:" + currentRate + "Entered rate is:" + userTrade.getEnteredPrice());
                            logger.debug("unRealisedPnL Value is:" + unRealisedPnL);
                        }
                    }
                    totalUnRealisedPnL = totalUnRealisedPnL.add(unRealisedPnL);
                }
                logger.debug("totalUnRealisedPnL Value is:" + totalUnRealisedPnL);
            }
            userPortfolio.setUnrealisedPnl(totalUnRealisedPnL);
            BigDecimal netAssetValue = userPortfolio.getNetInvestmentAmount().add(userPortfolio.getUnrealisedPnl());
            userPortfolio.setNetAssetValue(netAssetValue);
            logger.debug("userPortfolio.setNetAssetValue is:" + userPortfolio.getNetAssetValue());
            userPortfolio.setReturns();
            userPortfolio.setUpdatedOn(LocalDateTime.now());
            userPortfolio.setUpdatedBy("SYSTEM");
            final boolean isRebalanceRequired = isPortfolioReadyForRebalance(userPortfolio);
            if (isRebalanceRequired) {
                // change status to Ready for rebalance
                userPortfolio.setExecutionStatus(UserPortfolioStatus.ReadyForRebalance);
                userPortfolioTransactionDao.add(UserPortfolioTransactionStatus.UserPortfolioReadyRebalance, userPortfolio, netAssetValue);
            } else {
                // check whether portfolio is in loss.
                if (userPortfolio.isPortfolioInLoss()) {
                    // check whether loss exceeds threshold.
                    final boolean hasExceededThreshold = userPortfolio.hasLossExceededThreshold(propertiesHelper.portfolioLossPercentageThreshold);
                    if (hasExceededThreshold) {
                        // TODO:Notify admin
                    }
                }
            }
            dao.save(userPortfolio);
            userPortfolioTransactionDao.add(UserPortfolioTransactionStatus.UserPortfolioReturnsUpdated, userPortfolio, netAssetValue);
        }
    }

    public void calculateRealisedPnL(UserPortfolio userPortfolio) {
        final List<UserTrade> userTradesToUpdate = userTradeService.getUserTradeByUserPortfolioId(userPortfolio.getId());
        BigDecimal totalRealisedPnL = BigDecimal.ZERO;
        for (UserTrade userTrade : userTradesToUpdate) {
            BigDecimal closePrice = userTrade.getClosedPrice();
            if (TradeStatus.CloseOrderCompleted.equals(userTrade.getExecutionStatus()) && closePrice != null) {
                BigDecimal difference = closePrice.subtract(userTrade.getEnteredPrice());
                BigDecimal realisedPnL = BigDecimal.ZERO;
                if (BigDecimal.ZERO.compareTo(difference) != 0) {
                    BigDecimal tradeValue = difference.multiply(userTrade.getEnteredUnits()).setScale(PropertiesHelper.SCALE, BigDecimal.ROUND_DOWN);
                    realisedPnL = tradeValue.subtract(userTrade.getFeesBothTradeLegs());
                    if (logger.isDebugEnabled()) {
                        logger.debug("units are:" + userTrade.getEnteredUnits());
                        logger.debug("Close rate is:" + closePrice + "Entered rate is:" + userTrade.getEnteredPrice());
                        logger.debug("unRealisedPnL Value is:" + realisedPnL);
                    }
                }
                totalRealisedPnL = totalRealisedPnL.add(realisedPnL);
            }
        }
        userPortfolio.setRealisedPnl(totalRealisedPnL);
        BigDecimal netAssetValue = userPortfolio.getNetInvestmentAmount().add(userPortfolio.getRealisedPnl());
        userPortfolio.setNetAssetValue(netAssetValue);
        logger.debug("userPortfolio.setNetAssetValue is:" + userPortfolio.getNetAssetValue());
        userPortfolio.setReturns();
        userPortfolio.setUnrealisedPnl(BigDecimal.ZERO);
        userPortfolioTransactionDao.add(UserPortfolioTransactionStatus.UserPortfolioReturnsUpdated, userPortfolio, netAssetValue);
    }

}
