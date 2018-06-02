package sg.activewealth.roboadvisor.common.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus;
import sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus;
import sg.activewealth.roboadvisor.banking.model.Remittance;
import sg.activewealth.roboadvisor.banking.service.RemittanceService;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        locations = {"classpath:test-appContext-infra.xml", "classpath:test-webContext-infra.xml"})
public class RemittanceServiceTest {

  @Autowired
  private RemittanceService remittanceService;

  @Autowired
  private UserPortfolioService userPortfolioService;

  @Test
  public void testRemittanceEmailSending() {

    Remittance remittance = new Remittance();
    remittance.setReferenceNo("REM-1223");
    remittance.setInvestorRemittanceRemittedAmount(BigDecimal.TEN);
    remittance.setUserPortfolio(userPortfolioService.retrieve("ff80818162bd6a040162bd819a7a004c",true));
    remittance.setCreatedBy(UserPortfolioServiceTest.USER_ID);
    remittance.setBrokerFundingStatus(BrokerFundingStatus.Unprocessed);
    remittanceService.save(remittance);

    remittance.setInvestorRemittanceStatus(InvestorRemittanceStatus.Received);
    remittanceService.save(remittance);

    remittance.setBrokerFundingStatus(BrokerFundingStatus.Completed);
    remittance.setRemarks("Completed Status");
    remittanceService.save(remittance);
  }
}

