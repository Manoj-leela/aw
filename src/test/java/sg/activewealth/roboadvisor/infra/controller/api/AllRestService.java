package sg.activewealth.roboadvisor.infra.controller.api;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.portfolio.enums.InstrumentType;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserRiskProfile;
import sg.activewealth.roboadvisor.portfolio.model.Instrument;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.model.PortfolioInstrument;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.InstrumentService;
import sg.activewealth.roboadvisor.portfolio.service.PortfolioInstrumentService;
import sg.activewealth.roboadvisor.portfolio.service.PortfolioService;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;
import sg.activewealth.roboadvisor.trade.enums.TradePosition;

/**
 * The Class AllRestService.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:test-appContext-infra.xml", "classpath:test-webContext-infra.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AllRestService {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserPortfolioService userPortfolioService;
	
	@Autowired
	InstrumentService instrumentService;
	
	@Autowired
	PortfolioInstrumentService portfolioInstrumentService;
	
	@Autowired
	PortfolioService portfolioService;
	
	String av = "40287e815cb170a2015cb17191c70000%7C0%7C0%7C5_Rgqn8NueOkmigBJMpF1muyJ4Mfop8pOYsQuODMGhM=";
	
	String c = "MobileApp";
	
	String cv = "1.0.0";
	
	public static String userId;
	
	public static String userportfolioId;
	
	public static String portfolioId;
	
	public static String intrumentId;
	
	public static String potfolioInstrumentId;
	
	@Test
	public void AAexecuteBeforeClass() {
		User user = new User();
		user.setFirstName("Marti");
		user.setLastName("Lee");
		user.setEmail("mlee123@xyz.com");
		user.setMobileNumber("987456589678");
		user.setPassword("123456789");
		user = userService.save(user);
		userId = user.getId();
		
		Instrument instrument = new Instrument();
		instrument.setName("Instrument1");
		instrument.setInstrumentType(InstrumentType.ETF);
		instrument.setCreatedBy(userId);
		instrument.setCreatedOn(LocalDateTime.now());
		instrument = instrumentService.save(instrument);
		intrumentId = instrument.getId();
		
		PortfolioInstrument portfolioInstrument = new PortfolioInstrument();
		portfolioInstrument.setInstrument(instrument);
		portfolioInstrument.setWeightage(new BigDecimal(90));
		portfolioInstrument.setTradePosition(TradePosition.SHORT);
		portfolioInstrument.setCreatedOn(LocalDateTime.now());
		portfolioInstrument.setCreatedBy(userId);
		portfolioInstrument = portfolioInstrumentService.save(portfolioInstrument);
		potfolioInstrumentId = portfolioInstrument.getId();
		
		List<PortfolioInstrument> portfolioInstruments = new ArrayList<>();
		portfolioInstruments.add(portfolioInstrument);
		
		Portfolio portfolio = new Portfolio();
		portfolio.setName("ABC Portfolio");
		portfolio.setDescription("ABC");
		portfolio.setPortfolioInstruments(portfolioInstruments);
		portfolio.setRiskProfile(UserRiskProfile.Conservative);
		portfolio.setCreatedBy(userId);
		portfolio.setCreatedOn(LocalDateTime.now());
		portfolio = portfolioService.save(portfolio);
		portfolioId = portfolio.getId();
		
		UserPortfolio userPortfolio = new UserPortfolio();
		userPortfolio.setUser(user);
		userPortfolio.setExecutionStatus(UserPortfolioStatus.Executed);
		userPortfolio.setCreatedBy(user.getId());
		userPortfolio.setCreatedOn(LocalDateTime.now());
		userPortfolio.setUpdatedBy(user.getId());
		userPortfolio.setUpdatedOn(LocalDateTime.now());
		userPortfolio = userPortfolioService.save(userPortfolio);
		userportfolioId = userPortfolio.getId();
		
	}
	
	@Test
	public void AgetUserInfo() {
		String APIUri = "http://localhost:8080/a/r/api/v1/user/"+userId;
		
		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.header("av", av)
				.when().get(APIUri);
		System.out.println(response.getBody().print());
		Integer httpStatus = response.getStatusCode();
		String id = response.getBody().jsonPath().get("id");
		Assert.assertEquals(Integer.valueOf(200),httpStatus);
		Assert.assertNotNull(id);
	}
	
	@Test
	public void BgetQuesAndAns() {
		String APIUri = "http://localhost:8080/a/r/api/v1/user/profilequestions";
		
		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.header("av", av)
				.when().get(APIUri);
		Assert.assertNotNull(response);
		System.out.println(response.getBody().print());
		Integer httpStatus = response.getStatusCode();
		Assert.assertEquals(Integer.valueOf(200),httpStatus);
	}
	
	@Test
	public void CgetUserportfolioQuesAndAns() {
		String APIUri = "http://localhost:8080/a/r/api/v1/userPortfolio/questions";
		
		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.header("av", av)
				.when().get(APIUri);
		Assert.assertNotNull(response);
		System.out.println(response.getBody().print());
		Integer httpStatus = response.getStatusCode();
		Assert.assertEquals(Integer.valueOf(200),httpStatus);
	}
	
	@Test
	public void DgetInvestmentGoalQuestions() {
		String APIUri = "http://localhost:8080/a/r/api/v1/userPortfolio/investmentgoals/questions";
		
		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.header("av", av)
				.when().get(APIUri);
		Assert.assertNotNull(response);
		System.out.println(response.getBody().print());
		Integer httpStatus = response.getStatusCode();
		Assert.assertEquals(Integer.valueOf(200),httpStatus);
	}
	
	@Test
	public void EgetPortfolioGeneralInfo() {
		String APIUri = "http://localhost:8080/a/r/api/v1/portfolio/get/"+portfolioId;
		
		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.header("av", av)
				.when().get(APIUri);
		Assert.assertNotNull(response);
		System.out.println(response.getBody().print());
		Integer httpStatus = response.getStatusCode();
		Assert.assertEquals(Integer.valueOf(200),httpStatus);
	}
	
	@Test
	public void FgetPortfolioByRiskProfile() {
		String APIUri = "http://localhost:8080/a/r/api/v1/portfolio/Aggressive";
		
		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.header("av", av)
				.when().get(APIUri);
		Assert.assertNotNull(response);
		System.out.println(response.getBody().print());
		Integer httpStatus = response.getStatusCode();
		Assert.assertEquals(Integer.valueOf(200),httpStatus);
	}
	
	@Test
	public void GcreateUser() {
		String APIUri = "http://localhost:8080/a/r/api/v1/user/create";
		String APIBody = "{\"firstName\": \"John\"," + 
				  "\"lastName\": \"Doe\"," +
				  "\"emailAddress\": \"johndoe212@xyz.com\"," +
				  "\"password\": \"password\","+
				  "\"phone\": \"123456781252\"" +
				"}";
		
		RequestSpecBuilder builder = new RequestSpecBuilder();
		
		builder.setBody(APIBody);
		builder.setContentType(MediaType.APPLICATION_JSON_VALUE);
		builder.addHeader("av",
				av);
		
		RequestSpecification requestSpec = builder.build();

		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.spec(requestSpec).when().post(APIUri);
		
		Assert.assertNotNull(response);
		System.out.println(response.getBody().print());
		Integer httpStatus = response.getStatusCode();
		if(httpStatus != 201) {
			if(response.getBody().jsonPath().get("phone") != null){
				Assert.assertNull(response.getBody().jsonPath().get("phone"));
			}
			if(response.getBody().jsonPath().get("emailAddress") != null) {
				Assert.assertNull(response.getBody().jsonPath().get("emailAddress"));
			}
		}
		Assert.assertEquals(Integer.valueOf(201),httpStatus);
	}
	
	@Test
	public void HaddPaymentMethod() {
		String APIUri = "http://localhost:8080/a/r/api/v1/user/addPaymentMethod/"+userId;
		String APIBody = "{"+
				"\"number\" : \"4242424242424242\","+
				"\"exp_month\" : 7,"+
				"\"exp_year\" : 2018,"+
				"\"cvc\" : \"314\"}";
		
		RequestSpecBuilder builder = new RequestSpecBuilder();
		
		builder.setBody(APIBody);
		builder.setContentType(MediaType.APPLICATION_JSON_VALUE);
		builder.addHeader("av",
				av);
		
		RequestSpecification requestSpec = builder.build();

		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.spec(requestSpec).when().post(APIUri);
		
		Assert.assertNotNull(response);
		System.out.println(response.getBody().print());
		Integer httpStatus = response.getStatusCode();
		Boolean success = response.getBody().jsonPath().get("success");
		Assert.assertEquals(Integer.valueOf(200),httpStatus);
		Assert.assertTrue(success);
	}
	
	@Test
	public void IfundTransfer() {
		String APIUri = "http://localhost:8080/a/r/api/v1/user/"+userId+"/fundtransfer";
		String APIBody = "{\"amount\" : 20000}";
		
		RequestSpecBuilder builder = new RequestSpecBuilder();
		
		builder.setBody(APIBody);
		builder.setContentType(MediaType.APPLICATION_JSON_VALUE);
		builder.addHeader("av",
				av);
		
		RequestSpecification requestSpec = builder.build();

		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.spec(requestSpec).when().post(APIUri);
		
		Assert.assertNotNull(response);
		System.out.println(response.getBody().print());
		Integer httpStatus = response.getStatusCode();
		Boolean success = response.getBody().jsonPath().get("success");
		Assert.assertEquals(Integer.valueOf(200),httpStatus);
		Assert.assertTrue(success);
	}
	
	@Ignore
	@Test
	public void loginUser() {
		String APIUri = "http://localhost:8080/a/r/api/v1/login";
		String APIBody = "{\"emailAddress\" : \"pdoshi@staunchsys.com\","+
				"\"password\" : \"123456789\"}";
		
		RequestSpecBuilder builder = new RequestSpecBuilder();
		
		builder.setBody(APIBody);
		builder.setContentType(MediaType.APPLICATION_JSON_VALUE);
		builder.addHeader("av",
				av);
		
		RequestSpecification requestSpec = builder.build();

		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.spec(requestSpec).when().post(APIUri);
		
		Assert.assertNotNull(response);
		System.out.println(response.getBody().print());
		Integer httpStatus = response.getStatusCode();
		String token = response.getBody().jsonPath().get("token");
		Assert.assertEquals(Integer.valueOf(200),httpStatus);
		Assert.assertNotNull(token);
	}
	
	@Test
	public void JsubmitInvestmentQues() {
		String APIUri = "http://localhost:8080/a/r/api/v1/userPortfolio/"+userId+"/answers";
		String APIBody = "{\"questionAndAnswerList\" :[{" +
					"\"question\" : \"annualincome_question\"," +
					"\"answer\" : \"1000000SGD\"},{" +
					"\"question\" : \"investmentobjective_question\","+
					"\"answer\" : \"investmentobjective_answer_2\"},{"+
					"\"question\" : \"riskreturnexpectations_question\","+
					"\"answer\" : \"riskreturnexpectations_answer_2\"},{"+
					"\"question\" : \"volatilitytolerance_question\","+
					"\"answer\" : \"volatilitytolerance_answer_2\"},{"+
					"\"question\" : \"losstolerance_question\","+
					"\"answer\" : \"losstolerance_answer_2\"},{"+
					"\"question\" : \"personalityandbehavior_question\","+
					"\"answer\" : \"personalityandbehavior_answer_2\"},{"+
					"\"question\" : \"timehorizon_question\","+
					"\"answer\" : \"timehorizon_answer_2\"},{"+
					"\"question\" : \"liquidityconsideration_1_question\","+
					"\"answer\" : \"liquidityconsideration_1_answer_2\"},{"+
					"\"question\" : \"liquidityconsideration_2_question\","+
					"\"answer\" : \"liquidityconsideration_2_answer_2\"}]}";
		
		RequestSpecBuilder builder = new RequestSpecBuilder();
		
		builder.setBody(APIBody);
		builder.setContentType(MediaType.APPLICATION_JSON_VALUE);
		builder.addHeader("av",
				av);
		
		RequestSpecification requestSpec = builder.build();

		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.spec(requestSpec).when().post(APIUri);
		
		Assert.assertNotNull(response);
		Integer httpStatus = response.getStatusCode();
		String id = response.getBody().jsonPath().get("id");
		Assert.assertEquals(Integer.valueOf(200),httpStatus);
		Assert.assertNotNull(id);
		System.out.println(id);
		userportfolioId = id;
	}
	
	@Test
	public void KupdateUserPortfolio() {
		String APIUri = "http://localhost:8080/a/r/api/v1/userPortfolio/update/"+userportfolioId;
		String APIBody = "{\"initialAmount\": \"20000\","+
				"\"monthlyContribution\": \"400\","+
				"\"targetDateOfWithdrawal\" : \"2020-07-02\","+
				"\"userRiskProfile\" : \"Safe\","+
				"\"goal\": \"Health\"}";
		
		RequestSpecBuilder builder = new RequestSpecBuilder();
		
		builder.setBody(APIBody);
		builder.setContentType(MediaType.APPLICATION_JSON_VALUE);
		builder.addHeader("av",
				av);
		
		RequestSpecification requestSpec = builder.build();

		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.spec(requestSpec).when().put(APIUri);
		
		Assert.assertNotNull(response);
		Integer httpStatus = response.getStatusCode();
		String id = response.getBody().jsonPath().get("id");
		Assert.assertEquals(Integer.valueOf(200),httpStatus);
		Assert.assertNotNull(id);
		System.out.println(id);
	}
	
	@Test
	public void LgetUserPortfolioForUser() {
		String APIUri = "http://localhost:8080/a/r/api/v1/userPortfolio/user/"+userId;
		
		Response response = RestAssured.given().queryParam("c", c).queryParam("cv", cv)
				.header("av", av)
				.when().get(APIUri);
		Assert.assertNotNull(response);
		System.out.println(response.getBody().print());
		Integer httpStatus = response.getStatusCode();
		Assert.assertEquals(Integer.valueOf(200),httpStatus);
	}
	
	@Test 
	public void Mdeletefields() {
		
		PortfolioInstrument portfolioInstrument = portfolioInstrumentService.retrieve(potfolioInstrumentId);
		portfolioInstrumentService.delete(portfolioInstrument);
		
		Instrument instrument = instrumentService.retrieve(intrumentId);
		instrumentService.delete(instrument);
		
		
		
		Portfolio portfolio = portfolioService.retrieve(portfolioId);
		portfolioService.delete(portfolio);
	}

}
