package sg.activewealth.roboadvisor.common.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sg.activewealth.roboadvisor.banking.enums.RedemptionStatus;
import sg.activewealth.roboadvisor.banking.model.Redemption;
import sg.activewealth.roboadvisor.banking.service.RedemptionService;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;

import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        locations = {"classpath:test-appContext-infra.xml", "classpath:test-webContext-infra.xml"})
public class RedemptionServiceTest {

  @Autowired
  private RedemptionService redemptionService;

  @Autowired
  private UserPortfolioService userPortfolioService;

  @Test
  public void testRedemptionSaveAndUpdate() {

    Redemption redemption = new Redemption();
    redemption.setAmountReceivedFees(BigDecimal.TEN);
    redemption.setRedemptionDate(LocalDate.now());
    redemption.setRedemptionAmount(BigDecimal.valueOf(1000L));
    redemption.setAmountReceivedFromBroker(BigDecimal.ONE);
    redemption.setUserPortfolio(userPortfolioService.retrieve("ff80818162bd6a040162bd819a7a004c",true));
    redemption.setCreatedBy(UserPortfolioServiceTest.USER_ID);
    redemption.setRedemptionStatus(RedemptionStatus.ReceivedFromBroker);
    redemptionService.save(redemption);


    redemption.setRedemptionStatus(RedemptionStatus.Completed);
    redemption.setRemarks("Completed Status");
    redemptionService.save(redemption);
  }
}

