package sg.activewealth.roboadvisor.common.jobs;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import sg.activewealth.roboadvisor.banking.enums.PaymentStatus;
import sg.activewealth.roboadvisor.common.jobs.runner.PaymentInitiationJobRunner;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PaymentInitiationErrorRetryJob extends QuartzJobBean {

  private Logger logger = Logger.getLogger(PaymentInitiationErrorRetryJob.class);

  @Override
  protected void executeInternal(final JobExecutionContext jobExecutionContext) throws JobExecutionException {
    try {
      ApplicationContext appContext = (ApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContext");
      final PaymentInitiationJobRunner paymentInitiationJobRunner = appContext.getBean(PaymentInitiationJobRunner.class);
      //paymentInitiationJobRunner.initiatePayment(PaymentStatus.PaymentError);
    } catch (Exception e) {
      logger.error(e.getMessage(),e);
      throw new JobExecutionException(e);
    }
  }
}
