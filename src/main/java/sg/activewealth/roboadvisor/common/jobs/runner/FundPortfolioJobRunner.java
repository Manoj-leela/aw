package sg.activewealth.roboadvisor.common.jobs.runner;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;

import java.util.List;

@Component
public class FundPortfolioJobRunner {

  private Logger logger = Logger.getLogger(FundPortfolioJobRunner.class);

  @Autowired
  private UserPortfolioService userPortfolioService;

  public void fundAssignedPortfolio(final int batchSize){
	  //TODO Remove this code afterwards as we do not fund from payment provider now
    /*final List<UserPortfolio> userPortfolios = userPortfolioService.getPortfoliosByStatusAndBatchSize(UserPortfolioStatus.Assigned,batchSize);
    for(final UserPortfolio userPortfolio : userPortfolios){
      try {
        userPortfolioService.fundUserPortfolioIfEligible(userPortfolio);
      }catch (Exception e){
        logger.error(e.getMessage(),e);
        //Do not throw otherwise it will stop other job records;
      }
    }*/
  }

}
