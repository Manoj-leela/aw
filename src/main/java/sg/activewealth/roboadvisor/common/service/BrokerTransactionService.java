package sg.activewealth.roboadvisor.common.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.common.dao.BrokerTransactionDao;
import sg.activewealth.roboadvisor.common.model.BrokerTransaction;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.trade.model.UserTrade;

@Service
public class BrokerTransactionService extends AbstractService<BrokerTransaction> {

	public BrokerTransactionService() {
		super(BrokerTransaction.class);
	}

	public BrokerTransactionService(Class<BrokerTransaction> modelClass) {
		super(modelClass);
	}

	@Autowired
	public void setDao(BrokerTransactionDao dao) {
		super.dao = dao;
	}

	public void addBrokertTransaction(UserTrade userTrade) {
		BrokerTransaction model = new BrokerTransaction();
		model.setTransactionDate(LocalDateTime.now());
		model.setInstrument(userTrade.getPortfolioInstrument().getInstrument());
		model.setPrice(userTrade.getEnteredPrice());
		// TODO profit and loss need to be calculated and other things need to take care after discussion with client
		// model.setTotalInvestmentAmount(userTrade.get());
		// BigDecimal totalFee =
		// userTrade.getSubscriptionTradeAmount().subtract(userTrade.getNetTradeAmount());
		// model.setTotalFees(totalFee);
		//model.setNetInvestmentAmount(userTrade.getNetTradeAmount());
		dao.save(model);
	}

}
