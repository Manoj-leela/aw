package sg.activewealth.roboadvisor.common.jobs;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import sg.activewealth.roboadvisor.common.jobs.runner.FundPortfolioJobRunner;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class FundPortfolioJob extends QuartzJobBean {

  private Logger logger = Logger.getLogger(FundPortfolioJob.class);

  
  @Override
  protected void executeInternal(final JobExecutionContext jobExecutionContext) throws JobExecutionException {
    try {
      ApplicationContext appContext = (ApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContext");
      final FundPortfolioJobRunner fundPortfolioJobRunner = appContext.getBean(FundPortfolioJobRunner.class);
      final PropertiesHelper propertiesHelper = appContext.getBean(PropertiesHelper.class);
      fundPortfolioJobRunner.fundAssignedPortfolio(propertiesHelper.fundPortfolioBatchSize);
    } catch (Exception e) {
      logger.error(e.getMessage(),e);
      throw new JobExecutionException(e);
    }
  }
}
