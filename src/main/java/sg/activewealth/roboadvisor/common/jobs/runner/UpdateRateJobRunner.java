package sg.activewealth.roboadvisor.common.jobs.runner;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.portfolio.service.InstrumentService;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;

@Component
public class UpdateRateJobRunner {

    private Logger logger = Logger.getLogger(UpdateRateJobRunner.class);

    @Autowired
    private UserPortfolioService userPortfolioService;

    public void updateRate() {
        logger.debug("--- Update Rate Cron is started ---");

        userPortfolioService.recalculatePortfolio();
        logger.debug("--- Update Rate Cron ended ---");
    }

}
