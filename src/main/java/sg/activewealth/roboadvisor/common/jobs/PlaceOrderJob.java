package sg.activewealth.roboadvisor.common.jobs;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import sg.activewealth.roboadvisor.common.jobs.runner.PlaceOrderJobRunner;
import sg.activewealth.roboadvisor.trade.enums.TradeStatus;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PlaceOrderJob extends QuartzJobBean {

  private Logger logger = Logger.getLogger(PlaceOrderJob.class);

  @Override
  protected void executeInternal(final JobExecutionContext jobExecutionContext) throws JobExecutionException {
    ApplicationContext appContext = null;
    try {
      appContext = (ApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContext");
      final PlaceOrderJobRunner placeOrderJobRunner = appContext.getBean(PlaceOrderJobRunner.class);
      placeOrderJobRunner.processByTradeStatus(TradeStatus.PlaceOrderRequest);
    } catch (Exception e) {
      logger.error(e.getMessage(),e);
      throw new JobExecutionException(e);
    }
  }
}
