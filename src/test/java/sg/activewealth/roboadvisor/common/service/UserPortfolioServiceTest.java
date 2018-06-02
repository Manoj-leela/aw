package sg.activewealth.roboadvisor.common.service;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.PortfolioService;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:test-appContext-infra.xml", "classpath:test-webContext-infra.xml" })
public class UserPortfolioServiceTest {

	public static final String USER_ID = "ff8081815a3dfb16015a3dfb28a80000";
	public static final String PORTFOLIO_ID = "ff808181596011d0015960186b1b0002";
	public static final String USER_PORTFOLIO_ID = "ff80818159863bf70159864ede010000";

	private User user;
	private Portfolio portfolio;

	@Autowired
	private UserService userService;

	@Autowired
	private PortfolioService portfolioService;

	@Autowired
	private UserPortfolioService userPortfolioService;

	@Before
	public void beforeMethod() {
		user = userService.retrieve(USER_ID);
		portfolio = portfolioService.retrieve(PORTFOLIO_ID);
		//System.out.println(user.getEmailAddress());

	}

	@Test
	public void createUserPortfolio() {
		UserPortfolio userPortfolio = createUserPortfolios(portfolio,UserPortfolioStatus.Executed);
		Assert.assertNotNull(userPortfolio);
	}

	@Test
	public void getUserPortfolios() {
		List<UserPortfolio> userPortfolios = userPortfolioService
				.retrieveByUserStatusAndEmailAndPortfolio(UserPortfolioStatus.NotAssigned, null, null);
		Assert.assertNotNull(userPortfolios);
		Assert.assertNotEquals(0, userPortfolios.size());
		for (UserPortfolio userPortfolio : userPortfolios) {
			System.out.println("value is:" + userPortfolio.getUser().getEmail());
		}
	}

	@Test
	public void testAssignPortfolioToUser() {
		
		Portfolio toPortfolio = portfolioService.retrieve("4028891959d42c0e0159d436d21c0007");
		String[] userPortfolioIds = new String[2];
		userPortfolioIds[0] = createUserPortfolios(null, UserPortfolioStatus.NotAssigned).getId();
		userPortfolioIds[1] = createUserPortfolios(null, UserPortfolioStatus.NotAssigned).getId();
		List<UserPortfolio> userPortfolios = userPortfolioService.assignPortfolio(userPortfolioIds, null, toPortfolio,
				UserPortfolioStatus.Assigned);

		Assert.assertNotNull(userPortfolios);
		Assert.assertSame(userPortfolioIds.length, userPortfolios.size());

		for (UserPortfolio userPortfolio2 : userPortfolios) {
			Assert.assertEquals(userPortfolio2.getExecutionStatus(), UserPortfolioStatus.Assigned);
			Assert.assertNotNull(userPortfolio2.getPortfolio());
		}

	}
	
	@Test
	public void testAssignLaterPortfolioToUser() {
		
		Portfolio toPortfolio = portfolioService.retrieve("4028891e59b695db0159b69947380000");
		Portfolio fromPortfolio = portfolioService.retrieve("4028891959d42c0e0159d436d21c0007");
		
		String[] userPortfolioIds = new String[2];
		userPortfolioIds[0] = createUserPortfolios(fromPortfolio, UserPortfolioStatus.Executed).getId();
		userPortfolioIds[1] = createUserPortfolios(fromPortfolio, UserPortfolioStatus.Executed).getId();
		
		List<UserPortfolio> userPortfolios = userPortfolioService.assignPortfolio(userPortfolioIds, fromPortfolio, toPortfolio,
				UserPortfolioStatus.AssignedForLater);

		Assert.assertNotNull(userPortfolios);
		Assert.assertSame(userPortfolioIds.length, userPortfolios.size());

		for (UserPortfolio userPortfolio2 : userPortfolios) {
			Assert.assertEquals(userPortfolio2.getExecutionStatus(), UserPortfolioStatus.AssignedForLater);
			Assert.assertNotNull(userPortfolio2.getPortfolio());
		}

	}
	
	@Test
	public void testAssignReplacePortfolioToUser() {
		
		Portfolio toPortfolio = portfolioService.retrieve("4028891e59b695db0159b69947380000");
		Portfolio fromPortfolio = portfolioService.retrieve("4028891959d42c0e0159d436d21c0007");
		
		String[] userPortfolioIds = new String[2];
		userPortfolioIds[0] = createUserPortfolios(fromPortfolio, UserPortfolioStatus.Executed).getId();
		userPortfolioIds[1] = createUserPortfolios(fromPortfolio, UserPortfolioStatus.Executed).getId();
		
		List<UserPortfolio> userPortfolios = userPortfolioService.assignPortfolio(userPortfolioIds, fromPortfolio, toPortfolio,
				UserPortfolioStatus.AssignedForReplace);

		Assert.assertNotNull(userPortfolios);
		Assert.assertSame(userPortfolioIds.length, userPortfolios.size());

		for (UserPortfolio userPortfolio2 : userPortfolios) {
			Assert.assertEquals(userPortfolio2.getExecutionStatus(), UserPortfolioStatus.AssignedForReplace);
			Assert.assertNotNull(userPortfolio2.getPortfolio());
		}
		
	}
	
	@Test
	public void testClosePortfolioToUser() {
		
		Portfolio fromPortfolio = portfolioService.retrieve("4028891959d42c0e0159d436d21c0007");
		
		String[] userPortfolioIds = new String[2];
		userPortfolioIds[0] = createUserPortfolios(fromPortfolio, UserPortfolioStatus.Executed).getId();
		userPortfolioIds[1] = createUserPortfolios(fromPortfolio, UserPortfolioStatus.Executed).getId();
		
		List<UserPortfolio> userPortfolios = userPortfolioService.assignPortfolio(userPortfolioIds, null, null,
				UserPortfolioStatus.CloseRequested);

		Assert.assertNotNull(userPortfolios);
		Assert.assertSame(userPortfolioIds.length, userPortfolios.size());

		for (UserPortfolio userPortfolio2 : userPortfolios) {
			Assert.assertEquals(userPortfolio2.getExecutionStatus(), UserPortfolioStatus.CloseRequested);
			Assert.assertNotNull(userPortfolio2.getPortfolio());
		}
		
	}

//	@Test
//	public void assignLaterPortfolioToUser() {
//		UserPortfolio userPortfolio = userPortfolioService.retrieve(USER_PORTFOLIO_ID);
//		System.out.println("--getStatus--" + userPortfolio.getStatus());
//		userPortfolioService.assignPortfolioToUser(portfolio, userPortfolio);
//		userPortfolio = userPortfolioService.retrieve(USER_PORTFOLIO_ID);
//		System.out.println("--getStatus--" + userPortfolio.getStatus());
//	}
//
//	@Test
//	public void replaceUserPortfolioStatus() {
//		UserPortfolio userPortfolio = userPortfolioService.retrieve(USER_PORTFOLIO_ID);
//		System.out.println("--getStatus--" + userPortfolio.getStatus());
//		userPortfolioService.changeUserPortfolioStatus(userPortfolio, UserPortfolioStatus.ASSIGNED_FOR_LATER);
//		userPortfolio = userPortfolioService.retrieve(USER_PORTFOLIO_ID);
//		System.out.println("--getStatus--" + userPortfolio.getStatus());
//	}
//
//	@Test
//	public void closePortfolio() {
//		UserPortfolio userPortfolio = userPortfolioService.retrieve(USER_PORTFOLIO_ID);
//		Portfolio toPortfolio = portfolioService.retrieve(PORTFOLIO_ID);
//		userPortfolioService.replacePortfolio(userPortfolio, toPortfolio);
//	}
	

	public UserPortfolio createUserPortfolios(Portfolio portfolio, UserPortfolioStatus userPortfolioStatus) {
		UserPortfolio userPortfolio = new UserPortfolio();
		userPortfolio.setUser(user);
		userPortfolio.setExecutionStatus(userPortfolioStatus);
		userPortfolio.setCreatedBy(USER_ID);
		userPortfolio.setCreatedOn(LocalDateTime.now());
		userPortfolio.setUpdatedBy(USER_ID);
		userPortfolio.setUpdatedOn(LocalDateTime.now());
		userPortfolio.setPortfolio(portfolio);
		userPortfolio = userPortfolioService.save(userPortfolio);
		return userPortfolio;
	}

	@After
	public void afterMethod() {

	}

}
