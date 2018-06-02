package sg.activewealth.roboadvisor.common.jobs;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import sg.activewealth.roboadvisor.common.jobs.runner.CloseOrderJobRunner;
import sg.activewealth.roboadvisor.trade.enums.TradeStatus;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CloseOrderErrorRetryJob extends QuartzJobBean {

  private Logger logger = Logger.getLogger(CloseOrderErrorRetryJob.class);

  @Override
  protected void executeInternal(final JobExecutionContext jobExecutionContext) throws JobExecutionException {
    try {
      ApplicationContext appContext = (ApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContext");
      final CloseOrderJobRunner closeOrderJobRunner = appContext.getBean(CloseOrderJobRunner.class);
      closeOrderJobRunner.processByTradeStatus(TradeStatus.CloseOrderError);
    } catch (Exception e) {
      logger.error(e.getMessage(),e);
      throw new JobExecutionException(e);
    }
  }
}
