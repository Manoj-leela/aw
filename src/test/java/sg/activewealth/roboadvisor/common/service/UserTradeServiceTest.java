package sg.activewealth.roboadvisor.common.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.PortfolioInstrumentService;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;
import sg.activewealth.roboadvisor.trade.enums.TradePosition;
import sg.activewealth.roboadvisor.trade.enums.TradeStatus;
import sg.activewealth.roboadvisor.trade.model.UserTrade;
import sg.activewealth.roboadvisor.trade.service.UserTradeService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:test-appContext-infra.xml", "classpath:test-webContext-infra.xml" })
public class UserTradeServiceTest {

  public static final String PORTFOLIO_INSTRUMENT_ID = "ff80818159863bf70159864f83cb0001";

  @Autowired
  private UserTradeService userTradeService;

  @Autowired
  private UserPortfolioService userPortfolioService;

  @Autowired
  private PortfolioInstrumentService portfolioInstrumentService;

  @Test
  public void testUserTradeInsertion(){
    UserPortfolio userPortfolio = userPortfolioService.retrieve(UserPortfolioServiceTest.USER_PORTFOLIO_ID);
    UserTrade userTrade = new UserTrade();
    userTrade.setExecutionStatus(TradeStatus.PlaceOrderRequest);
    userTrade.setTradePosition(TradePosition.SHORT);
    userTrade.setEnteredUnits(BigDecimal.ONE);
    userTrade.setUserPortfolio(userPortfolio);
    userTrade.setPortfolioInstrument(portfolioInstrumentService.retrieve(PORTFOLIO_INSTRUMENT_ID));
    userTrade.setCreatedBy(UserPortfolioServiceTest.USER_ID);
    userTrade.setCreatedOn(LocalDateTime.now());
    userTrade.setUpdatedBy(UserPortfolioServiceTest.USER_ID);
    userTrade.setUpdatedOn(LocalDateTime.now());
    userTradeService.save(userTrade);

  }
}
