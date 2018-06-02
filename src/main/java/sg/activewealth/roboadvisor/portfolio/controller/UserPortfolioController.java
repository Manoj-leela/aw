package sg.activewealth.roboadvisor.portfolio.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import sg.activewealth.roboadvisor.common.enums.UserProgressStatus;
import sg.activewealth.roboadvisor.common.jobs.runner.PortfolioFundReleaseJobRunner;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.FormOptionDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.enums.UserOperationContextResultType;
import sg.activewealth.roboadvisor.infra.exception.ObjectNotFoundException;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.utils.StringUtils;
import sg.activewealth.roboadvisor.infra.utils.SystemUtils;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.dto.UserPortfolioDto;
import sg.activewealth.roboadvisor.portfolio.enums.Goal;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioFundingStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserRiskProfile;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolioQuestionAndAnswer;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolioTransaction;
import sg.activewealth.roboadvisor.portfolio.service.PortfolioService;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioTransactionService;
import sg.activewealth.roboadvisor.trade.model.UserTrade;
import sg.activewealth.roboadvisor.trade.service.UserTradeService;

@Controller
@RequestMapping("/admin/userPortfolio")
public class UserPortfolioController extends CRUDController<UserPortfolio, UserPortfolioService> {

    @Autowired
    public void setService(UserPortfolioService service) {
        this.service = service;
    }

    @Autowired
    public UserPortfolioController(UserPortfolioService service) {
        super(UserPortfolio.class, service);
    }

    public UserPortfolioController(Class<UserPortfolio> userPortfolio, UserPortfolioService service) {
        super(userPortfolio, service);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserTradeService userTradeService;

    @Autowired
    private UserPortfolioTransactionService userPortfolioTransactionService;

    public UserPortfolioController() {
        super();
    }

    @Autowired
    private UserPortfolioService userPortfolioService;

    @Autowired
    private PortfolioFundReleaseJobRunner portfolioFundReleaseJobRunner;

    @Autowired
    private PropertiesHelper propertiesHelper;

    @Override
    public Object[] preCreateUpdateGet(UserPortfolio model, HttpServletRequest request) {
        super.preCreateUpdateGet(model, request);
        PagingDto<User> pagingDto = new PagingDto<>();
        pagingDto.setResultsPerPage(null);
        List<User> allUserList = userService.retrieve("createdOn", pagingDto).getResults();
        // TODO this is for assign portfolio
        String portfolioId = request.getParameter("id");
        Portfolio portfolio = portfolioService.retrieve(portfolioId);

        List<FormOptionDto> userPortfolioStatusList = new ArrayList<FormOptionDto>();
        for (UserPortfolioStatus userPortfolioStatus : UserPortfolioStatus.values()) {
            userPortfolioStatusList.add(new FormOptionDto(userPortfolioStatus.name(), userPortfolioStatus.getLabel()));
        }

        List<FormOptionDto> goalList = new ArrayList<FormOptionDto>();
        for (Goal goal : Goal.values()) {
            goalList.add(new FormOptionDto(goal.name(), goal.getLabel()));
        }

        PagingDto<Portfolio> portfolioPagingDto = new PagingDto<Portfolio>();
        portfolioPagingDto.setResultsPerPage(null);
        List<Portfolio> portfolioList = portfolioService.retrieveActivePortfolio();

        return new Object[] { "model", model, "userList", allUserList, "portfolio", portfolio, "userPortfolioStatusList", userPortfolioStatusList, "portfolioList", portfolioList, "fundingStatusList", PortfolioFundingStatus.values(), "goalList", goalList };
    }

    @Override
    public UserPortfolio preCreateUpdatePost(UserPortfolio model, HttpServletRequest request) {
        Portfolio portfolio = portfolioService.retrieve(model.getPortfolio().getId());
        User user = userService.retrieve(model.getUser().getId());
        model.setUser(user);
        model.setPortfolio(portfolio);
        if (model.getExecutionStatus() == null) {
            model.setExecutionStatus(UserPortfolioStatus.Assigned);
        }
        return model;
    }

    @RequestMapping(value = { "/execution" }, method = RequestMethod.GET)
    public Object getUserPortfoliosByStatus(PagingDto<UserPortfolio> pagingDto, HttpServletRequest request) {

        String statusParam = request.getParameter("status");

        UserPortfolioStatus userPortfolioStatus = null;
        if (statusParam != null && UserPortfolioStatus.fromString(statusParam) != null) {
            userPortfolioStatus = UserPortfolioStatus.fromString(statusParam);
        }

        final PagingDto<UserPortfolio> userPortfolios = userPortfolioService.retrieveForListPage(pagingDto, userPortfolioStatus, null, null);

        if (CollectionUtils.isNotEmpty(userPortfolios.getResults())) {
            for (UserPortfolio userPortfolio : userPortfolios.getResults()) {
                final List<UserTrade> userTrades = userTradeService.getUserTradeByUserPortfolioId(userPortfolio.getId());
                userPortfolio.setUserTradeList(userTrades);
            }
        }
        Object[] ret = new Object[] { "userPortfolios", userPortfolios, "status", userPortfolioStatus };
        return modelAndView(getFullJspPath("user_portfolio_action_list"), ret);
    }

    @RequestMapping(value = { "/get/{userId}" }, method = RequestMethod.GET)
    public Object getUserPorfolioForUser(@PathVariable(value = "userId") String userId, PagingDto<UserPortfolio> pagingDto) {
        User user = userService.retrieve(userId);
        if (user == null) {
            throw new ObjectNotFoundException("error.object.notfound");
        }
        PagingDto<UserPortfolio> userPortfolios = userPortfolioService.retriveByUser(user, pagingDto);
        List<UserTrade> userTrades = new ArrayList<>();
        if (userPortfolios.getResults() != null && userPortfolios.getResults().size() > 0) {
            for (UserPortfolio userPortfolio : userPortfolios.getResults()) {
                userTrades = userTradeService.getUserTradeByUserPortfolioId(userPortfolio.getId());
                userPortfolio.setUserTradeList(userTrades);
            }
        }
        Object[] ret = new Object[] { "userPortfolios", userPortfolios, "user", user };
        return modelAndView(getFullJspPath("user_portfolio_list"), ret);
    }

    @SuppressWarnings("unused")
    @RequestMapping(value = { "/assign" }, method = RequestMethod.POST)
    public Object assignUserPortfolioPost(HttpServletRequest request) {

        String assignType = request.getParameter("assignType");
        String newPortfolioId = request.getParameter("newPortfolioId");
        String oldPortfolioId = request.getParameter("portfolioId");
        String[] userPortfolioIds = request.getParameterValues("userPortfolio");

        if (userPortfolioIds == null) {
            userOperationContextService.set(UserOperationContextResultType.Failure, "message.portfolio.user.empty");
            return "redirect:" + request.getHeader("Referer");
        }

        Portfolio fromPortfolio = portfolioService.retrieve(oldPortfolioId);
        Portfolio toPortfolio = portfolioService.retrieve(newPortfolioId);

        List<UserPortfolio> assignedUserPortfolios = new ArrayList<UserPortfolio>();

        String message = null;
        switch (assignType) {
        case "Assign":
            assignedUserPortfolios = service.assignPortfolio(userPortfolioIds, null, toPortfolio, UserPortfolioStatus.Assigned);
            message = "message.portfolio.assign";
            break;
        case "AssignLater":
            assignedUserPortfolios = service.assignPortfolio(userPortfolioIds, fromPortfolio, toPortfolio, UserPortfolioStatus.AssignedForLater);
            message = "message.portfolio.assignLater";
            break;
        case "AssignReplace":
            assignedUserPortfolios = service.assignPortfolio(userPortfolioIds, fromPortfolio, toPortfolio, UserPortfolioStatus.AssignedForReplace);
            message = "message.portfolio.assignReplace";
            break;
        case "Close":
            assignedUserPortfolios = service.assignPortfolio(userPortfolioIds, null, null, UserPortfolioStatus.CloseRequested);
            message = "message.portfolio.close";
            break;
        default:
            message = "";
            break;
        }

        String failedUserPortfolioAssignments = "";
        for (UserPortfolio userPortfolio : assignedUserPortfolios) {
            if (userPortfolio.isHasAssignFailed()) {
                failedUserPortfolioAssignments += "[PortfolioName:" + userPortfolio.getPortfolio().getName() + " and UserName:" + userPortfolio.getUser().getEmail() + "]" + ",";
            }
        }
        String derivedMessage = getContextMessage(message, new Object[] { failedUserPortfolioAssignments });

        userOperationContextService.set(UserOperationContextResultType.Success, message);
        return "redirect:/r/admin/portfolio/list";
    }

    @RequestMapping(value = { "/assign" }, method = RequestMethod.GET)
    public Object assignGet(@RequestParam("newPortfolioId") String newPortfolioId) {
        List<Portfolio> portfolioList = new ArrayList<>();
        if (!ValidationUtils.getInstance().isEmptyString(newPortfolioId)) {
            portfolioList = portfolioService.retrieveAllPortfoliosExceptPassedPorfolio(newPortfolioId, false);
        }
        Portfolio newPortfolio = portfolioService.retrieve(newPortfolioId);
        return modelAndView(getFullJspPath("assignment"), "newPortfolio", newPortfolio, "portfolioList", portfolioList);
    }

    @RequestMapping(value = { "/assign/query" }, method = RequestMethod.GET)
    @ResponseBody
    public Object assignQuery(@RequestParam(value = "profile", required = false) String riskProfile, @RequestParam(value = "email", required = false) String emailAddress,
            @RequestParam(value = "assign", required = false, defaultValue = "Assign") String assignAction, @RequestParam(value = "op", required = false) String op, @RequestParam(value = "portfolioId", required = false) String portfolioId,
            HttpServletRequest request) {

        Portfolio oldPortfolio = null;
        List<Portfolio> portfolioList = new ArrayList<>();
        if (!ValidationUtils.getInstance().isEmptyString(portfolioId)) {
            portfolioList = portfolioService.retrieveAllPortfoliosExceptPassedPorfolio(portfolioId, false);
        }

        List<UserPortfolio> userPortfolios;
        if (StringUtils.getInstance().hasValue(portfolioId)) {
            oldPortfolio = portfolioService.retrieve(portfolioId);
        }
        userPortfolios = service.retrieveByUserStatusAndEmailAndPortfolio(UserPortfolioStatus.Executed, emailAddress, oldPortfolio);

        // Thai: fix lazy initialisation exception
        Map<String, String> userPortfolioMap = null;
        List<Map<String, String>> userPortfolioList = new ArrayList<>();
        if (userPortfolios != null) {

            for (UserPortfolio userPortfolio : userPortfolios) {
                userPortfolioMap = new HashMap<>();
                userPortfolioMap.put("id", userPortfolio.getId());
                userPortfolioMap.put("portfolioName", userPortfolio.getPortfolio().getName());
                userPortfolioMap.put("userFirstName", userPortfolio.getUser().getFirstName());
                userPortfolioMap.put("userLastName", userPortfolio.getUser().getLastName());
                userPortfolioMap.put("userEmailAddress", userPortfolio.getUser().getEmail());
                userPortfolioMap.put("portfolioStatus", userPortfolio.getExecutionStatus().getLabel());

                userPortfolioList.add(userPortfolioMap);
            }
        }

        return new UserPortfolioDataDTO(portfolioList, assignAction, userPortfolioList, emailAddress, portfolioId);
    }

    public class UserPortfolioDataDTO {
        private List<FormOptionDto> riskProfileList;
        private List<Portfolio> portfolioList;
        private String assignType;
        private List<Map<String, String>> userPortfolios;
        private String email;
        private String portfolioId;

        public UserPortfolioDataDTO(List<Portfolio> portfolioList, String assignType, List<Map<String, String>> userPortfolios, String email, String portfolioId) {
            this.portfolioList = portfolioList;
            this.assignType = assignType;
            this.userPortfolios = userPortfolios;
            this.email = email;
            this.portfolioId = portfolioId;
        }

        public List<FormOptionDto> getRiskProfileList() {
            return riskProfileList;
        }

        public void setRiskProfileList(List<FormOptionDto> riskProfileList) {
            this.riskProfileList = riskProfileList;
        }

        public List<Portfolio> getPortfolioList() {
            return portfolioList;
        }

        public void setPortfolioList(List<Portfolio> portfolioList) {
            this.portfolioList = portfolioList;
        }

        public String getAssignType() {
            return assignType;
        }

        public void setAssignType(String assignType) {
            this.assignType = assignType;
        }

        public List<Map<String, String>> getUserPortfolios() {
            return userPortfolios;
        }

        public void setUserPortfolios(List<Map<String, String>> userPortfolios) {
            this.userPortfolios = userPortfolios;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPortfolioId() {
            return portfolioId;
        }

        public void setPortfolioId(String portfolioId) {
            this.portfolioId = portfolioId;
        }
    }

    // This trigger is no more used. As per new flow when remittance is completed(BrokerFunding.COMPLETED),
    // the user portfolio would be funded.
    @RequestMapping(value = { "/fundPortfolio" }, method = RequestMethod.GET)
    public Object fundPortfolioTrigger() {
        List<UserPortfolio> userPortfolios = userPortfolioService.getPortfoliosByStatusAndBatchSize(UserPortfolioStatus.Assigned, propertiesHelper.fundPortfolioBatchSize);
        for (final UserPortfolio userPortfolio : userPortfolios) {
            try {
                userPortfolioService.fundUserPortfolioIfEligible(userPortfolio);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return "redirect:/r/admin/triggers/list";
    }

    @RequestMapping(value = { "/portfolioFundReleaseJob" }, method = RequestMethod.GET)
    public Object realseFundPortfolioTrigger() {
        portfolioFundReleaseJobRunner.releaseFund(propertiesHelper.portfolioFundReleaseBatchSize);
        return "redirect:/r/admin/triggers/list";
    }

    @RequestMapping(value = "/answers/{userId}", method = RequestMethod.GET)
    public Object investmentAnswers(@PathVariable(value = "userId") String userId) {
        User user = userService.retrieve(userId);
        if (user == null) {
            throw new ObjectNotFoundException("error.object.notfound");
        }
        List<UserPortfolioQuestionAndAnswer> investmentQuestions = service.getInvestMentQuestions();
        UserPortfolioDto userPortfolioDto = new UserPortfolioDto();
        userPortfolioDto.setQuestionAndAnswerList(investmentQuestions);
        Object[] ret = new Object[] { "userPortfolioDto", userPortfolioDto, "user", user };
        return modelAndView(getFullJspPath("InvestmentAnswer"), ret);
    }

    @RequestMapping(value = "/{userId}/answers", method = RequestMethod.POST)
    public Object createUnderstandingProfile(@PathVariable String userId, @ModelAttribute UserPortfolioDto userPortfolioDto, Errors springErrors, HttpServletRequest request) throws ValidateException, IllegalAccessException, InvocationTargetException {
        User user = userService.retrieve(userId, false);
        UserRiskProfile userRiskProfile = SystemUtils.getInstance().getRandomRiskProfile();
        Portfolio portfolio = portfolioService.retriveByRiskProfile(userRiskProfile, false).get(0);
        UserPortfolio userPortfolio = new UserPortfolio();
        BeanUtils.copyProperties(userPortfolio, userPortfolioDto);
        userPortfolio.setCreatedOn(LocalDateTime.now());
        userPortfolio.setUser(user);
        userPortfolio.setCreatedBy(userSessionService.get().getUserId());
        userPortfolio.setExecutionStatus(UserPortfolioStatus.Assigned);
        userPortfolio.setPortfolio(portfolio);
        ErrorsDto errors = null;
        try {
            userPortfolioService.save(userPortfolio);
            userPortfolioService.setTradesForUserPortfolio(userPortfolio);
            user.setProgressStatus(UserProgressStatus.PortfolioRecommendation);
            userService.saveWithoutPrePost(user);
        } catch (ValidateException e) {
            errors = super.convertValidateExceptionToErrors(e, springErrors);
            userOperationContextService.warn(e);
            if (errors.getErrors().size() > 0) {
                String message = getContextMessage(errors.getErrors().get(0).getErrorKey(), errors.getErrors().get(0).getErrorArgs());
                userOperationContextService.set(UserOperationContextResultType.Failure, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            userOperationContextService.warn(e);
            addRejectError(springErrors, e.getMessage());
        }
        if (!springErrors.hasErrors()) {
            addStandardSuccessMessage();
        }
        return "redirect:/r/admin/user/list/user";
    }

    @Override
    public Object list(PagingDto<UserPortfolio> pagingDto, String ids, HttpServletRequest request) {
        String queryStatus = request.getParameter("executionStatus");
        String quryEmailAddress = request.getParameter("user.emailAddress");
        String queryPortfolioName = request.getParameter("portfolio.name");

        String queryDateTime = request.getParameter("transactionDate");

        if (queryDateTime != null) {
            LocalDateTime localDateTime = LocalDateTime.parse(queryDateTime);
            List<UserPortfolioTransaction> userPortfolioTransactions = userPortfolioTransactionService.retrieveUserPortFolio(queryStatus, localDateTime);
            List<UserPortfolio> userPortfolios = new ArrayList<>();

            for (UserPortfolioTransaction userPortfolioTransaction : userPortfolioTransactions) {
                userPortfolios.add(userPortfolioTransaction.getUserPortfolio());
            }
            pagingDto.updateResults(userPortfolios.size(), userPortfolios);
        } else {

            UserPortfolioStatus userPortfolioStatus = null;
            if (queryStatus != null && UserPortfolioStatus.fromString(queryStatus) != null) {
                userPortfolioStatus = UserPortfolioStatus.fromString(queryStatus);
            }

            pagingDto = service.retrieveForListPage(pagingDto, userPortfolioStatus, quryEmailAddress, queryPortfolioName);
        }

        List<FormOptionDto> userPortfoliostatusList = new ArrayList<FormOptionDto>();
        for (UserPortfolioStatus userPortfolioStatus : UserPortfolioStatus.values()) {
            userPortfoliostatusList.add(new FormOptionDto(userPortfolioStatus.getLabel(), userPortfolioStatus.getLabel()));
        }
        return modelAndView(getFullJspPath("list"), "list", pagingDto, "userPortfolioStatusList", userPortfoliostatusList);
    }

    @RequestMapping("/generateReport")
    public void downloadReport(HttpServletResponse response) throws WriteException, IOException {
        service.exportAsExcel(((UserPortfolioService) service).new Report() {

            @Override
            protected void writeRow(WritableSheet sheet, UserPortfolio item, int row) throws WriteException {
                int column = 0;
                sheet.addCell(new Label(column++, row, item.getId()));
                sheet.addCell(new Label(column++, row, item.getCreatedBy()));
                sheet.addCell(new Label(column++, row, item.getUpdatedBy() != null ? item.getUpdatedBy() : " "));
                sheet.addCell(new Label(column++, row, String.valueOf(item.getCreatedOn())));
                sheet.addCell(new Label(column++, row, String.valueOf(item.getUpdatedOn() != null ? item.getUpdatedOn() : " ")));

                sheet.addCell(new Label(column++, row, String.valueOf(item.getReturns() != null ? item.getReturns() : " ")));
                sheet.addCell(new Label(column++, row, String.valueOf(item.getExecutionStatus().getValue())));
                sheet.addCell(new Label(column++, row, String.valueOf(item.getGoal() != null ? item.getGoal().getValue() : " ")));
                sheet.addCell(new Label(column++, row, item.getUser() != null ? item.getUser().getId() : " "));
                sheet.addCell(new Label(column++, row, item.getPortfolio() != null ? item.getPortfolio().getId() : " "));
                sheet.addCell(new Label(column++, row, item.getRebalanceTargetPortfolio() != null ? item.getRebalanceTargetPortfolio().getId() : " "));
                sheet.addCell(new Label(column++, row, String.valueOf(item.getRealisedPnl() != null ? item.getRealisedPnl() : " ")));
                sheet.addCell(new Label(column++, row, String.valueOf(item.getUnrealisedPnl() != null ? item.getUnrealisedPnl() : " ")));
                sheet.addCell(new Label(column++, row, String.valueOf(item.getNetAssetValue() != null ? item.getNetAssetValue() : " ")));
                sheet.addCell(new Label(column++, row, String.valueOf(item.getNetInvestmentAmount() != null ? item.getNetInvestmentAmount() : " ")));
                sheet.addCell(new Label(column++, row, item.getPortfolioFundingStatus() != null ? item.getPortfolioFundingStatus().getLabel() : " "));

            }

            @Override
            protected void writeMoreHeadings(WritableSheet sheet, int row) throws WriteException {
                return;
            }

            @Override
            protected void writeHeadings(WritableSheet sheet, int row) throws WriteException {
                sheet.addCell(new Label(0, row, "ID"));
                sheet.addCell(new Label(1, row, "Created By"));
                sheet.addCell(new Label(2, row, "Updated By"));
                sheet.addCell(new Label(3, row, "Created On"));
                sheet.addCell(new Label(4, row, "Updated On"));
                sheet.addCell(new Label(5, row, "Deleted"));

                sheet.addCell(new Label(6, row, "Returns"));
                sheet.addCell(new Label(7, row, "Balance"));
                sheet.addCell(new Label(8, row, "User Portfolio Status"));
                sheet.addCell(new Label(9, row, "Goal"));
                sheet.addCell(new Label(10, row, "Computed Profile"));
                sheet.addCell(new Label(11, row, "Final Profile"));
                sheet.addCell(new Label(12, row, "User ID"));
                sheet.addCell(new Label(13, row, "Portfolio ID"));

                sheet.addCell(new Label(14, row, "ReAssign Portfolio ID"));
                sheet.addCell(new Label(15, row, "Target Date Of Withdrawal"));
                sheet.addCell(new Label(16, row, "Realised Pnl"));
                sheet.addCell(new Label(17, row, "Unrealised Pnl"));
                sheet.addCell(new Label(18, row, "Net Asset Value"));
                sheet.addCell(new Label(19, row, "Total Amount Invested"));
                sheet.addCell(new Label(20, row, "Funding Status"));
                sheet.addCell(new Label(21, row, "Fees"));
            }

            @Override
            protected String getSheetName() {
                return "User Portfolio";
            }

            @Override
            protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {
                return super.getWorkbook(response);
            }

        }, null, null, response);
    }

}
