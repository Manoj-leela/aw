package sg.activewealth.roboadvisor.common.jobs.runner;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;

import java.util.List;

@Component
public class PortfolioFundReleaseJobRunner {

  private Logger logger = Logger.getLogger(PortfolioFundReleaseJobRunner.class);

  @Autowired
  private UserPortfolioService userPortfolioService;

  public void releaseFund(final int batchSize){
//    final List<UserPortfolio> closedUserPortfolios = userPortfolioService.getPortfoliosByStatusAndBatchSize(UserPortfolioStatus.Closed, batchSize);
//    for (final UserPortfolio userPortfolio:closedUserPortfolios){
//      try {
//        userPortfolioService.releaseFundOfPortfolio(userPortfolio);
//      }
//      catch (Exception e){
//        logger.error(e.getMessage(),e);
//      }
//    }
  }
}
