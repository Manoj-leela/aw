package sg.activewealth.roboadvisor.dealing.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.dealing.dao.ExternalFundPriceDao;
import sg.activewealth.roboadvisor.dealing.dao.ExternalFundSubscriptionDao;
import sg.activewealth.roboadvisor.dealing.dao.ExternalFundSubscriptionResoldDao;
import sg.activewealth.roboadvisor.dealing.model.ExternalFundPrice;
import sg.activewealth.roboadvisor.dealing.model.ExternalFundSubscription;
import sg.activewealth.roboadvisor.dealing.model.ExternalFundSubscriptionResold;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ServiceException;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.trade.enums.BuySell;
import sg.activewealth.roboadvisor.trade.model.UserTrade;

@Service
public class ExternalFundSubscriptionResoldService extends AbstractService<ExternalFundSubscriptionResold> {

	public ExternalFundSubscriptionResoldService() {
		super(ExternalFundSubscriptionResold.class);
	}

	public ExternalFundSubscriptionResoldService(Class<ExternalFundSubscriptionResold> modelClass) {
		super(modelClass);
	}

	@Autowired
	public void setDao(ExternalFundSubscriptionResoldDao dao) {
		super.dao = dao;
	}

	@Autowired
	private ExternalFundPriceDao externalFundPriceDao;

	@Autowired
	private ExternalFundSubscriptionDao externalFundSubscriptionDao;

	/**
	 * Creates {@link ExternalFundSubscriptionResold} for user trade
	 * 
	 * @param userTrade
	 */
	public synchronized ExternalFundPrice addExternalFundSubscriptionResold(UserTrade userTrade, BuySell buySell) {
		ExternalFundPrice fundPrice = externalFundPriceDao.retrieveClosestPriceBeforeNowWithDealingForFund(
				userTrade.getPortfolioInstrument().getInstrument().getName(), Boolean.FALSE);
		if (fundPrice == null) {
			throw new ServiceException();
		}
		logger.info("FundPrice is:" + fundPrice.toString());

		ExternalFundSubscriptionResold model = new ExternalFundSubscriptionResold();
		model.setExternalFundPrice(fundPrice);
		model.setTransactionDate(LocalDateTime.now());
		
		BigDecimal shares = BigDecimal.ZERO;
		if (BuySell.Buy.equals(buySell)) {
			model.setTotalSubscriptionAmount(userTrade.getAllocatedAmount());
			model.setNetInvestAmount(userTrade.getEnteredUnits().multiply(fundPrice.getBuyPrice()));
		} 
		shares = userTrade.getEnteredUnits();
		model.setShares(shares);
		

		ExternalFundSubscription externalFundSubscription = externalFundSubscriptionDao
				.retrieveClosestHigherShares(shares, userTrade.getPortfolioInstrument().getInstrument().getName());
		if (externalFundSubscription == null) {
			throw new ServiceException();
		}
		logger.info("ExternalFundSubscription is:" + externalFundSubscription.toString());
		model.setExternalFundSubscription(externalFundSubscription);

		if (BuySell.Buy.equals(buySell)) {
			BigDecimal balancedShares = externalFundSubscription.getBalanceShares().subtract(shares);
			externalFundSubscription.setBalanceShares(balancedShares);
		} else {
			BigDecimal balancedShares = externalFundSubscription.getBalanceShares().add(shares);
			externalFundSubscription.setBalanceShares(balancedShares);
		}
		externalFundSubscriptionDao.save(externalFundSubscription);
		dao.save(model);
		return fundPrice;
	}
	
	
	public PagingDto<ExternalFundSubscriptionResold> retrieveForListPage(PagingDto<ExternalFundSubscriptionResold> pagingDto,
			String fundName, LocalDate transactionDate, BigDecimal buyPrice, BigDecimal sellPrice) {
		pagingDto = ((ExternalFundSubscriptionResoldDao) dao).retrieveForListPage(pagingDto, fundName, transactionDate, buyPrice, sellPrice);
		return pagingDto;
	}

}
