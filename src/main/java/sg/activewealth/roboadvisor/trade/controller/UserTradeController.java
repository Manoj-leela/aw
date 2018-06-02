package sg.activewealth.roboadvisor.trade.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sg.activewealth.roboadvisor.common.jobs.runner.CloseOrderJobRunner;
import sg.activewealth.roboadvisor.common.jobs.runner.PlaceOrderJobRunner;
import sg.activewealth.roboadvisor.common.jobs.runner.TradeCreationJobRunner;
import sg.activewealth.roboadvisor.common.jobs.runner.UpdateRateJobRunner;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.trade.enums.BuySell;
import sg.activewealth.roboadvisor.trade.enums.TradeStatus;
import sg.activewealth.roboadvisor.trade.model.UserTrade;
import sg.activewealth.roboadvisor.trade.service.UserTradeService;

@Controller
@RequestMapping("/admin/userTrade")
public class UserTradeController extends CRUDController<UserTrade, UserTradeService> {

	@Autowired
	public void setService(UserTradeService service) {
		this.service = service;
	}

	@Autowired
	public UserTradeController(UserTradeService service) {
		super(UserTrade.class, service);
	}

	public UserTradeController(Class<UserTrade> userTrade, UserTradeService service) {
		super(userTrade, service);
	}

	public UserTradeController() {
		super();
	}

	@Autowired
	TradeCreationJobRunner tradeCreationJobRunner;

	@Autowired
	PlaceOrderJobRunner placeOrderJobRunner;

	@Autowired
	CloseOrderJobRunner closeOrderJobRunner;

	@Autowired
	UpdateRateJobRunner updateRateJobRunner;

	@Autowired
	PropertiesHelper propertiesHelper;

	@RequestMapping(value = { "/createTradeTrigger" }, method = RequestMethod.POST)
	public Object createTradesTrigger() {
		tradeCreationJobRunner.processBatch(propertiesHelper.tradeCreationBatchSize);
		return "redirect:/r/admin/triggers/list";
	}

	@RequestMapping(value = { "/execute" }, method = RequestMethod.POST)
	public Object executeTrades(@RequestParam("tradeIdList") String tradeIdString, HttpServletRequest request) {

		String[] tradeIds = org.apache.commons.lang.StringUtils.split(tradeIdString, ",");
		logger.info("Total tradeIds to process = " + tradeIds.length);

		String status = request.getParameter("queryParamStatus");

		UserPortfolioStatus userPortfolioStatus = null;
		if (status != null && !ValidationUtils.getInstance().isEmptyString(status)
				&& UserPortfolioStatus.fromString(status) != null) {
			userPortfolioStatus = UserPortfolioStatus.fromString(status);
		}

		if (userPortfolioStatus != null) {
			BuySell buyOrSell = BuySell.Buy;
			if (UserPortfolioStatus.ReadyForClose == userPortfolioStatus
					|| UserPortfolioStatus.PartiallyClosed == userPortfolioStatus) {
				buyOrSell = BuySell.Sell;
			}

			if (BuySell.Buy.equals(buyOrSell)) {
				for (String id : tradeIds) {
					UserTrade userTrade = service.retrieve(id);
					service.processPlaceOrderTrade(userTrade);
				}
			} else {
				for (String id : tradeIds) {
					UserTrade userTrade = service.retrieve(id);
					service.processCloseOrderTrade(userTrade);
				}
			}
		}

		if (status == null) {
			return "redirect:/r/admin";
		}

		return "redirect:/r/admin/userPortfolio/execution?status=" + status + "&currentPage=0";
	}

	@RequestMapping(value = { "/placeOrderTrigger/{action}" }, method = RequestMethod.POST)
	public Object executePlaceOrderScheduler(@PathVariable(value = "action") String action) {
		if (action.equals("executePlaceOrderRequest")) {
			placeOrderJobRunner.processByTradeStatus(TradeStatus.PlaceOrderRequest);
		} else if (action.equals("executePlaceOrderError")) {
			placeOrderJobRunner.processByTradeStatus(TradeStatus.PlaceOrderError);
		} else if (action.equals("executeCloseOrderRequest")) {
			closeOrderJobRunner.processByTradeStatus(TradeStatus.CloseOrderRequest);
		} else if (action.equals("executeCloseOrderError")) {
			closeOrderJobRunner.processByTradeStatus(TradeStatus.CloseOrderError);
		}
		return "redirect:/r/admin/triggers/list";
	}

	@RequestMapping(value = { "/updateRateTrigger" }, method = RequestMethod.POST)
	public Object updateRateTrigger() {
		updateRateJobRunner.updateRate();
		return "redirect:/r/admin/triggers/list";
	}

	@Override
	public Object list(PagingDto<UserTrade> pagingDto, String ids, HttpServletRequest request) {
		String userName = request.getParameter("user.firstName");
		String[] executionStatus = request.getParameterValues("executionStatus");
		String queryCreatedOnDate = request.getParameter("createdOn");
		LocalDate createdOnDate = null;
		if (!ValidationUtils.getInstance().isEmptyString(queryCreatedOnDate)) {
			try {
				createdOnDate = LocalDate.parse(queryCreatedOnDate);
			} catch (DateTimeParseException e) {
				logger.error(e.getMessage());
			}
		}
		pagingDto = service.retrieveForListPage(pagingDto, userName, executionStatus, createdOnDate);
		return modelAndView(getFullJspPath("list"), "list", pagingDto, "executionStatuses", TradeStatus.values());
	}
}
