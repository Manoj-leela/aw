package sg.activewealth.roboadvisor.common.jobs.runner;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.trade.enums.TradeStatus;
import sg.activewealth.roboadvisor.trade.model.UserTrade;

@Component
public class CloseOrderJobRunner extends AbstractOrderProcessorJobRunner {
    private Logger logger = Logger.getLogger(CloseOrderJobRunner.class);

    public void processByTradeStatus(final TradeStatus tradeStatus) {
        final List<UserTrade> eligibleUserTrades = getTradesOfFirstUserPortfolioFromStatus(tradeStatus);
        logger.debug("Records to process for Closing Order Request for status : " + tradeStatus + " =  " + eligibleUserTrades.size());

        if (eligibleUserTrades.isEmpty())
            return;

        // process each trade
        for (final UserTrade userTrade : eligibleUserTrades) {
            userTradeService.processCloseOrderTrade(userTrade);
        }
    }

}
