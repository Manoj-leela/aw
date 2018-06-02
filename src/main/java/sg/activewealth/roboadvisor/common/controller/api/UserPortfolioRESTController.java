package sg.activewealth.roboadvisor.common.controller.api;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;

import sg.activewealth.roboadvisor.banking.dto.RedemptionDto;
import sg.activewealth.roboadvisor.banking.dto.RemittanceDto;
import sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus;
import sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus;
import sg.activewealth.roboadvisor.banking.enums.RedemptionStatus;
import sg.activewealth.roboadvisor.banking.model.Redemption;
import sg.activewealth.roboadvisor.banking.model.Remittance;
import sg.activewealth.roboadvisor.banking.service.RedemptionService;
import sg.activewealth.roboadvisor.banking.service.RemittanceService;
import sg.activewealth.roboadvisor.common.enums.AgentOTPStatus;
import sg.activewealth.roboadvisor.common.enums.BankDetailsStatus;
import sg.activewealth.roboadvisor.common.enums.UserProgressStatus;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.controller.AbstractController;
import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.JsonObjectNotFoundException;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.utils.FileUtils;
import sg.activewealth.roboadvisor.infra.utils.SystemUtils;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.dto.UserPortfolioDto;
import sg.activewealth.roboadvisor.portfolio.dto.UserPortfolioListDto;
import sg.activewealth.roboadvisor.portfolio.dto.UserProfileAnswerDto;
import sg.activewealth.roboadvisor.portfolio.dto.UserProfileGoalDto;
import sg.activewealth.roboadvisor.portfolio.dto.UserProfileQuestionDto;
import sg.activewealth.roboadvisor.portfolio.enums.Goal;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioAssignmentCategory;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioFundingStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserRiskProfile;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.PortfolioService;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;
import sg.activewealth.roboadvisor.trade.dto.TradeDetailsDto;
import sg.activewealth.roboadvisor.trade.model.UserTrade;
import sg.activewealth.roboadvisor.trade.service.UserTradeService;

@RestController
@RequestMapping("/api/v1/userPortfolio")
public class UserPortfolioRESTController extends AbstractController {

	@Autowired
	private UserPortfolioService userPortfolioService;

	@Autowired
	private PortfolioService portfolioService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserTradeService userTradeService;

	@Autowired
	private RemittanceService remittanceService;

	@Autowired
	private RedemptionService redemptionService;

	@RequestMapping(value = "/{userPortfolioId}/fund", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> fundPortfolio(@PathVariable String userPortfolioId,
			@RequestBody RemittanceDto remittenceDto) {
		Map<String, Object> ret = new LinkedHashMap<>(3);
		UserPortfolio userPortfolio = userPortfolioService.retrieve(userPortfolioId);
		if (userPortfolio == null) {
			throw new JsonObjectNotFoundException("User Portfolio id: " + userPortfolioId + " not found.");
		}
		ErrorsDto errors = new ErrorsDto();
		if (BankDetailsStatus.NotSubmitted.equals(userPortfolio.getUser().getBankDetailsStatus())) {
			errors.add(new ErrorDto("bankDetailsStatus", "error.notSubmitted", "Bank Detail"));
		} else if (BankDetailsStatus.Submitted.equals(userPortfolio.getUser().getBankDetailsStatus())
				|| BankDetailsStatus.AwaitingApproval.equals(userPortfolio.getUser().getBankDetailsStatus())) {
			errors.add(new ErrorDto("bankDetailsStatus", "error.approve", "Bank Detail"));
		} else if (BankDetailsStatus.SubmissionIssues.equals(userPortfolio.getUser().getBankDetailsStatus())) {
			errors.add(new ErrorDto("bankDetailsStatus", "error.submittionIssue", "Bank Detail"));
		} else if (BankDetailsStatus.Rejected.equals(userPortfolio.getUser().getBankDetailsStatus())) {
			errors.add(new ErrorDto("bankDetailsStatus", "error.rejected", "Bank Detail"));
		}
		if (errors.hasErrors()) {
			throw new ValidateException(errors);
		}
		Remittance userPortfolioRemittance = new Remittance();
		userPortfolioRemittance.setUserPortfolio(userPortfolio);
		userPortfolioRemittance.setReferenceNo(remittenceDto.getReferenceNo());
		if (!ValidationUtils.getInstance().isEmptyString(remittenceDto.getRemittanceFile())) {
			String remittanceSlip = FileUtils.getInstance().setFileExtensionUsingContentType(
					userPortfolioId + "_remittanceSlip", remittenceDto.getRemittanceFileContentType());
			if (!ValidationUtils.getInstance().isEmptyString(FilenameUtils.getExtension(remittanceSlip))) {
				String encodedFilePath = propertiesHelper.encodedFileUpload + File.separator + remittanceSlip;
				String filePath = propertiesHelper.appUploads + File.separator + remittanceSlip;
				MultipartFile multipartFile = FileUtils.getInstance().convertBase64ToMultipartFile(
						remittenceDto.getRemittanceFile(), remittanceSlip, remittenceDto.getRemittanceFileContentType(),
						encodedFilePath, filePath);
				userPortfolioRemittance.setRemittanceSlipFileName(remittanceSlip);
				userPortfolioRemittance.setAttachment(multipartFile);
				
			} else {
				errors.add(new ErrorDto("remittanceSlip", "upload.unacceptableextensionsexception", "remittanceSlip"));
				throw new ValidateException(errors);
			}
		}
		userPortfolioRemittance.setBrokerFundingStatus(BrokerFundingStatus.Unprocessed);
		userPortfolioRemittance.setInvestorRemittanceStatus(InvestorRemittanceStatus.Submitted);
		userPortfolioRemittance.setInvestorRemittanceRemittedAmount(null);
		remittanceService.save(userPortfolioRemittance);
		userPortfolio.setPortfolioFundingStatus(PortfolioFundingStatus.Funded);
		userPortfolioService.saveWithoutPrePost(userPortfolio);
		ret.put("success", Boolean.TRUE);
		ret.put("status", userPortfolioRemittance.getBrokerFundingStatus().getLabel());
		ret.put("message", "Fund Transferred Successfully");
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userPortfolioId}/redemption", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> requestRedemption(@PathVariable String userPortfolioId,
			@RequestBody RedemptionDto redemptionDto) {
		Map<String, Object> ret = new LinkedHashMap<>(3);
		UserPortfolio userPortfolio = userPortfolioService.retrieve(userPortfolioId);
		if (userPortfolio == null) {
			throw new JsonObjectNotFoundException("User Portfolio id: " + userPortfolioId + " not found.");
		}
		Redemption redemption = new Redemption();
		redemption.setUserPortfolio(userPortfolio);
		redemption.setRedemptionAmount(redemptionDto.getRedemptionAmount());
		redemption.setRedemptionDate(redemptionDto.getRedemptionDate());
		redemption.setRedemptionStatus(RedemptionStatus.RequestedByInvestor);
		redemptionService.save(redemption);
		ret.put("success", Boolean.TRUE);
		ret.put("message", "Redemption done successfully");
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@RequestMapping(value = "/questions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserProfileQuestionDto>> getUnderstandingYouQuestions() {
		final List<UserProfileQuestionDto> newquestionsDtos = new ArrayList<>(8);
		newquestionsDtos.add(getBasicInvestmentGoal());
		newquestionsDtos.add(selectInvestmentGoal());
		newquestionsDtos.add(getQuestionOfInvestmentObjective());
		newquestionsDtos.add(getQuestionOfRiskReturnExpectations());
		newquestionsDtos.add(getQuestionOfVolatilityTolerance());
		newquestionsDtos.add(getQuestionOfLossTolerance());
		newquestionsDtos.add(getQuestionOfPersonalityAndBehavior());
		newquestionsDtos.add(getQuestionOfTimeHorizon());
		newquestionsDtos.add(getQuestionOfFirstLiquidityConsideration());
		newquestionsDtos.add(getQuestionOfSecondLiquidityConsideration());
		return new ResponseEntity<>(newquestionsDtos, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userId}/answers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserPortfolioDto> createUnderstandingProfile(@PathVariable String userId,
			@RequestBody UserPortfolioDto userPortfolioDto)
			throws ValidateException, IllegalAccessException, InvocationTargetException {
		User user = userService.retrieve(userId, false);
		if (user == null) {
			throw new JsonObjectNotFoundException("User Id: " + userId + "not found.");
		}
		UserRiskProfile userRiskProfile = SystemUtils.getInstance().getRandomRiskProfile();
		Portfolio portfolio = portfolioService.retrieveByRiskProfileAndAssignmentCategory(userRiskProfile,
				user.getPortfolioCategory(), false).get(0);
		UserPortfolio userPortfolio = new UserPortfolio();
		BeanUtils.copyProperties(userPortfolio, userPortfolioDto);
		userPortfolio.setCreatedOn(LocalDateTime.now());
		userPortfolio.setUser(user);
		userPortfolio.setCreatedBy(userSessionService.get().getUserId());
		userPortfolio.setExecutionStatus(UserPortfolioStatus.Assigned);
		userPortfolio.setPortfolio(portfolio);
		userPortfolioService.save(userPortfolio);
		if(PortfolioAssignmentCategory.PublicBVI.equals(user.getPortfolioCategory())) {
			user.setAgentOTPStatus(AgentOTPStatus.Completed);
		}
		user.setProgressStatus(UserProgressStatus.PortfolioRecommendation);
		userService.saveWithoutPrePost(user);
		Portfolio portfolioUser = new Portfolio(userPortfolio.getPortfolio().getId(),
				userPortfolio.getPortfolio().getName(), userPortfolio.getPortfolio().getRiskProfile(),
				userPortfolio.getPortfolio().getPortfolioInstruments(), userPortfolio.getPortfolio().getAssetClassAllocations());
		UserPortfolioDto userPortfoliodto = new UserPortfolioDto(userPortfolio.getNetInvestmentAmount(),
				userPortfolio.getNetAssetValue(), userPortfolio.getReturns(), userPortfolio.getId(), portfolioUser,
				userPortfolio.getExecutionStatus().getLabel());
		return new ResponseEntity<>(userPortfoliodto, HttpStatus.OK);
	}

	@RequestMapping(value = "/investmentgoals/questions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserProfileQuestionDto>> getInvestmentGoalsQuestions() {
		final List<UserProfileQuestionDto> investmentGoalQuestionDtos = new ArrayList<>(2);
		investmentGoalQuestionDtos.add(getBasicInvestmentGoal());
		investmentGoalQuestionDtos.add(selectInvestmentGoal());
		return new ResponseEntity<>(investmentGoalQuestionDtos, HttpStatus.OK);
	}

	/**
	 * API for screen #26 to update the Funding status for the Portfolio and
	 * Portfolio remittance
	 * 
	 * @param userPortfolioId
	 * @param userPortfolioStatus
	 * @return
	 */
	@RequestMapping(value = "/{userPortfolioId}/updateFundingStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserPortfolioDto> getUserPortfolioFundingStatus(@PathVariable String userPortfolioId,
			@RequestBody Map<String, Object> userPortfolioStatus) {

		final UserPortfolio userPortfolio = userPortfolioService.retrieve(userPortfolioId, true);
		if (userPortfolio == null) {
			throw new JsonObjectNotFoundException("UserPortfolio Id: " + userPortfolioId + "not found.");
		}
		PortfolioFundingStatus portfolioFundingStatus = PortfolioFundingStatus.get((String) userPortfolioStatus.get("fundingStatus"));
		userPortfolio.setPortfolioFundingStatus(portfolioFundingStatus);

		userPortfolioService.saveWithoutPrePost(userPortfolio);

		Portfolio portfolioDto = new Portfolio(userPortfolio.getPortfolio().getId(),
				userPortfolio.getPortfolio().getName(), userPortfolio.getPortfolio().getRiskProfile(),
				userPortfolio.getPortfolio().getPortfolioInstruments(), userPortfolio.getPortfolio().getAssetClassAllocations());

		UserPortfolioDto userPortfolioDtos = new UserPortfolioDto(userPortfolio.getNetInvestmentAmount(),
				userPortfolio.getNetAssetValue(), userPortfolio.getReturns(), userPortfolio.getId(), portfolioDto,
				userPortfolio.getGoal() != null ? userPortfolio.getGoal().getLabel() : null,
				userPortfolio.getPortfolioFundingStatus().getLabel());

		return new ResponseEntity<>(userPortfolioDtos, HttpStatus.OK);
	}

	@RequestMapping(value = "/update/{userPortfolioId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserPortfolioDto> updatePortfolio(@PathVariable String userPortfolioId,
			@RequestBody UserPortfolioDto userPortfolioDto)
			throws InvocationTargetException, IllegalAccessException, JsonParseException {
		final UserPortfolio userPortfolio = userPortfolioService.retrieve(userPortfolioId, true);
		if (userPortfolio == null) {
			throw new JsonObjectNotFoundException("UserPortfolio Id: " + userPortfolioId + "not found.");
		}
		UserRiskProfile userRiskProfile = UserRiskProfile.getByString(userPortfolioDto.getPortfolioType());
		if (userRiskProfile == null) {
			throw new JsonObjectNotFoundException(
					"User Risk Profile: " + userPortfolioDto.getPortfolioType() + " not found.");
		}
		UserRiskProfile assignedPortfolioRiskProfile = userPortfolio.getPortfolio().getRiskProfile();
		if (userRiskProfile != null) {
			if (!userRiskProfile.equals(userPortfolio.getPortfolio().getRiskProfile())) {
				Portfolio portfolio = portfolioService.retrieveByRiskProfileAndAssignmentCategory(userRiskProfile,
						userPortfolio.getUser().getPortfolioCategory(), false).get(0);
				userPortfolio.setPortfolio(portfolio);
			}
		}

		// Only create new trades if risk profile is changed otherwise do not
		// create trade
		if (!userRiskProfile.equals(assignedPortfolioRiskProfile)) {
			// Get all the created trades and soft delete them as well
			List<UserTrade> userTrades = userTradeService.getUserTradeByUserPortfolioId(userPortfolio.getId());
			for (UserTrade userTrade : userTrades) {
				userTradeService.delete(userTrade);
			}
			userPortfolioService.setTradesForUserPortfolio(userPortfolio);
		}
		userPortfolioService.saveWithoutPrePost(userPortfolio);
		Portfolio portfolioDto = new Portfolio(userPortfolio.getPortfolio().getId(),
				userPortfolio.getPortfolio().getName(), userPortfolio.getPortfolio().getRiskProfile(),
				userPortfolio.getPortfolio().getPortfolioInstruments(), userPortfolio.getPortfolio().getAssetClassAllocations());
		UserPortfolioDto userPortfolioDtos = new UserPortfolioDto(userPortfolio.getNetInvestmentAmount(),
				userPortfolio.getNetAssetValue(), userPortfolio.getReturns(), userPortfolio.getId(), portfolioDto,
				userPortfolio.getGoal() != null ? userPortfolio.getGoal().getLabel() : null,
				userPortfolio.getPortfolioFundingStatus().getLabel());
		return new ResponseEntity<>(userPortfolioDtos, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userPortfolioId}/getTrade/{portfolioInstrumentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TradeDetailsDto> getInstrumentDetails(@PathVariable String userPortfolioId,
			@PathVariable String portfolioInstrumentId) {
		UserTrade userTrade = userTradeService.getUserTrade(userPortfolioId, portfolioInstrumentId);
		if (userTrade == null) {
			throw new JsonObjectNotFoundException("No details found for this Instrument's Trade");
		}
		TradeDetailsDto instrumentDetailsDto = new TradeDetailsDto();
		instrumentDetailsDto.setInstrumentName(userTrade.getPortfolioInstrument().getInstrument().getName());
		instrumentDetailsDto.setNumberOfShares(userTrade.getEnteredUnits());
		instrumentDetailsDto.setCurrentValue(userTrade.getPortfolioInstrument().getInstrument().getCurrentPrice());
		instrumentDetailsDto
				.setIntrumentDescription("");
		instrumentDetailsDto.setInstrumentCode(userTrade.getPortfolioInstrument().getInstrument().getCode());
		return new ResponseEntity<>(instrumentDetailsDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserPortfolioListDto> getAllPortfolioForUser(@PathVariable String userId,
			HttpServletRequest request) {
		final User user = userService.retrieve(userId);
		if (request.getParameter("currentPage") == null || request.getParameter("resultsPerPage") == null) {
			throw new JsonObjectNotFoundException("Current Page or Results per page can not have null values.");
		}
		int currentPage = Integer.parseInt(request.getParameter("currentPage"));
		int resultsPerPage = Integer.parseInt(request.getParameter("resultsPerPage"));
		if (currentPage < 0 || resultsPerPage < 0) {
			throw new JsonObjectNotFoundException("Current Page or Results per page can not be negative values.");
		}
		if (resultsPerPage == 0) {
			throw new JsonObjectNotFoundException("Results per page can not be zero value.");
		}

		PagingDto<UserPortfolio> pagingDto = new PagingDto<>();
		pagingDto.setResultsPerPage(resultsPerPage);
		pagingDto.setCurrentPage(currentPage);
		BigDecimal totalBalance = new BigDecimal(0);
		BigDecimal totalReturns = new BigDecimal(0);
		BigDecimal totalInvested = new BigDecimal(0);
		BigDecimal totalEarned = new BigDecimal(0);

		if (user == null) {
			throw new JsonObjectNotFoundException("User Id: " + userId + " not found.");
		}
		final List<UserPortfolio> userPortfolios = userPortfolioService.retrieveByUserIdAndPortfolioStatus(user.getId(),
				pagingDto);
		List<UserPortfolioDto> userPortfolioDtos = new ArrayList<>();
		for (UserPortfolio userPortfolio : userPortfolios) {
			Portfolio portfolio = new Portfolio(userPortfolio.getPortfolio().getId(),
					userPortfolio.getPortfolio().getName(), userPortfolio.getPortfolio().getRiskProfile(),
					userPortfolio.getPortfolio().getPortfolioInstruments(), userPortfolio.getPortfolio().getAssetClassAllocations());
			Remittance remittance = remittanceService.getRemittanceByUserPortfolioId(userPortfolio.getId());
			UserPortfolioDto userPortfolioDto = new UserPortfolioDto(userPortfolio.getNetAssetValue(),
					userPortfolio.getReturns(), userPortfolio.getNetInvestmentAmount(), userPortfolio.getId(),
					portfolio, userPortfolio.getGoal() != null ? userPortfolio.getGoal().getLabel() : null, null,
					userPortfolio.getPortfolioFundingStatus().getLabel(),remittance != null ? remittance.getUpdatedOn().toLocalDate() : null);
			userPortfolioDtos.add(userPortfolioDto);
		}
		Map<String, BigDecimal> userPortfolioDetails = userPortfolioService.getPortfolioSummary(user.getId());
		totalBalance = userPortfolioDetails.get("totalBalance");
		totalInvested = userPortfolioDetails.get("totalInvested");
		totalReturns = userPortfolioDetails.get("totalReturns");
		totalEarned = userPortfolioDetails.get("totalEarned");
		UserPortfolioListDto userPortfolioListDto = new UserPortfolioListDto(totalBalance, totalReturns, totalInvested,
				totalEarned, userPortfolioDtos);
		return new ResponseEntity<>(userPortfolioListDto, HttpStatus.OK);
	}

	@RequestMapping(value = { "/{userId}/{userPorfolioStatus}",
			"/{userId}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserPortfolioListDto> getUserPortfolioByStatus(@PathVariable String userId,
			@PathVariable String userPorfolioStatus) {
		List<UserPortfolio> userPortfolioList = new ArrayList<>();
		UserPortfolioStatus userPortfolioStatus = UserPortfolioStatus.get(userPorfolioStatus);
		if (userId == null || userPorfolioStatus == null) {
			throw new JsonObjectNotFoundException("UserPortfolioStatus or UserId can not be null.");
		}
		if (userPortfolioStatus == null) {
			throw new JsonObjectNotFoundException("UserPortfolioStatus Invalid or not found.");
		}
		User user = userService.retrieve(userId);
		if (user == null) {
			throw new JsonObjectNotFoundException("User with Id : " + userId + " not found.");
		}
		userPortfolioList = userPortfolioService.retrieveByUserIdAndStatus(userPortfolioStatus, userId);
		List<UserPortfolioDto> userPortfolioDtos = new ArrayList<>();
		for (UserPortfolio userPortfolio : userPortfolioList) {
			Portfolio portfolio = new Portfolio(userPortfolio.getPortfolio().getId(),
					userPortfolio.getPortfolio().getName(), userPortfolio.getPortfolio().getRiskProfile(),
					userPortfolio.getPortfolio().getPortfolioInstruments(), userPortfolio.getPortfolio().getAssetClassAllocations());
			UserPortfolioDto userPortfolioDto = new UserPortfolioDto(userPortfolio.getNetInvestmentAmount(),
					userPortfolio.getNetAssetValue(), userPortfolio.getReturns(), userPortfolio.getId(), portfolio,
					userPortfolio.getExecutionStatus().getLabel());
			userPortfolioDtos.add(userPortfolioDto);
		}
		UserPortfolioListDto userPortfolioListDto = new UserPortfolioListDto(userPortfolioDtos);
		return new ResponseEntity<>(userPortfolioListDto, HttpStatus.OK);
	}

	private UserProfileQuestionDto getQuestionOfInvestmentObjective() {
		UserProfileQuestionDto investmentObjectiveQuestion = new UserProfileQuestionDto();
		investmentObjectiveQuestion.setQuestion(propertiesHelper.investmentObjectiveQuestion);
		investmentObjectiveQuestion.setQuestionCode("investmentobjective_question");
		List<UserProfileAnswerDto> answers = new ArrayList<>(5);
		answers.add(
				new UserProfileAnswerDto("investmentobjective_answer_1", propertiesHelper.investmentObjectiveAnswer1));
		answers.add(
				new UserProfileAnswerDto("investmentobjective_answer_2", propertiesHelper.investmentObjectiveAnswer2));
		answers.add(
				new UserProfileAnswerDto("investmentobjective_answer_3", propertiesHelper.investmentObjectiveAnswer3));
		answers.add(
				new UserProfileAnswerDto("investmentobjective_answer_4", propertiesHelper.investmentObjectiveAnswer4));
		answers.add(
				new UserProfileAnswerDto("investmentobjective_answer_5", propertiesHelper.investmentObjectiveAnswer5));
		investmentObjectiveQuestion.setAnswers(answers);
		investmentObjectiveQuestion.setTitle(propertiesHelper.investmentObjectiveTitle);
		investmentObjectiveQuestion.setDescription(propertiesHelper.investmentObjectiveDescription);
		investmentObjectiveQuestion.setQuestionId(2);
		return investmentObjectiveQuestion;
	}

	private UserProfileQuestionDto getQuestionOfRiskReturnExpectations() {
		UserProfileQuestionDto riskReturnExpectationsQuestion = new UserProfileQuestionDto();
		riskReturnExpectationsQuestion.setQuestion(propertiesHelper.riskReturnExpectationsQuestion);
		riskReturnExpectationsQuestion.setQuestionCode("riskreturnexpectations_question");
		List<UserProfileAnswerDto> answers = new ArrayList<>(5);
		answers.add(new UserProfileAnswerDto("riskreturnexpectations_answer_1",
				propertiesHelper.riskReturnExpectationsAnswer1));
		answers.add(new UserProfileAnswerDto("riskreturnexpectations_answer_2",
				propertiesHelper.riskReturnExpectationsAnswer2));
		answers.add(new UserProfileAnswerDto("riskreturnexpectations_answer_3",
				propertiesHelper.riskReturnExpectationsAnswer3));
		answers.add(new UserProfileAnswerDto("riskreturnexpectations_answer_4",
				propertiesHelper.riskReturnExpectationsAnswer4));
		answers.add(new UserProfileAnswerDto("riskreturnexpectations_answer_5",
				propertiesHelper.riskReturnExpectationsAnswer5));
		riskReturnExpectationsQuestion.setAnswers(answers);
		riskReturnExpectationsQuestion.setTitle(propertiesHelper.riskReturnExpectationsTitle);
		riskReturnExpectationsQuestion.setDescription(propertiesHelper.riskReturnExpectationsDescription);
		riskReturnExpectationsQuestion.setQuestionId(3);
		return riskReturnExpectationsQuestion;
	}

	private UserProfileQuestionDto getQuestionOfVolatilityTolerance() {
		UserProfileQuestionDto volatilityToleranceQuestion = new UserProfileQuestionDto();
		volatilityToleranceQuestion.setQuestion(propertiesHelper.volatilityToleranceQuestion);
		volatilityToleranceQuestion.setQuestionCode("volatilitytolerance_question");
		List<UserProfileAnswerDto> answers = new ArrayList<>(5);
		answers.add(
				new UserProfileAnswerDto("volatilitytolerance_answer_1", propertiesHelper.volatilityToleranceAnswer1));
		answers.add(
				new UserProfileAnswerDto("volatilitytolerance_answer_2", propertiesHelper.volatilityToleranceAnswer2));
		answers.add(
				new UserProfileAnswerDto("volatilitytolerance_answer_3", propertiesHelper.volatilityToleranceAnswer3));
		answers.add(
				new UserProfileAnswerDto("volatilitytolerance_answer_4", propertiesHelper.volatilityToleranceAnswer4));
		answers.add(
				new UserProfileAnswerDto("volatilitytolerance_answer_5", propertiesHelper.volatilityToleranceAnswer5));
		volatilityToleranceQuestion.setAnswers(answers);
		volatilityToleranceQuestion.setTitle(propertiesHelper.volatilityToleranceTitle);
		volatilityToleranceQuestion.setDescription(propertiesHelper.volatilityToleranceDescription);
		volatilityToleranceQuestion.setQuestionId(4);
		return volatilityToleranceQuestion;
	}

	private UserProfileQuestionDto getQuestionOfLossTolerance() {
		UserProfileQuestionDto lossToleranceQuestion = new UserProfileQuestionDto();
		lossToleranceQuestion.setQuestion(propertiesHelper.lossToleranceQuestion);
		lossToleranceQuestion.setQuestionCode("losstolerance_question");
		List<UserProfileAnswerDto> answers = new ArrayList<>(5);
		answers.add(new UserProfileAnswerDto("losstolerance_answer_1", propertiesHelper.lossToleranceAnswer1));
		answers.add(new UserProfileAnswerDto("losstolerance_answer_2", propertiesHelper.lossToleranceAnswer2));
		answers.add(new UserProfileAnswerDto("losstolerance_answer_3", propertiesHelper.lossToleranceAnswer3));
		answers.add(new UserProfileAnswerDto("losstolerance_answer_4", propertiesHelper.lossToleranceAnswer4));
		answers.add(new UserProfileAnswerDto("losstolerance_answer_5", propertiesHelper.lossToleranceAnswer5));
		lossToleranceQuestion.setAnswers(answers);
		lossToleranceQuestion.setTitle(propertiesHelper.lossToleranceTitle);
		lossToleranceQuestion.setDescription(propertiesHelper.lossToleranceDescription);
		lossToleranceQuestion.setQuestionId(5);
		return lossToleranceQuestion;
	}

	private UserProfileQuestionDto getQuestionOfPersonalityAndBehavior() {
		UserProfileQuestionDto personalityAndBehaviorQuestion = new UserProfileQuestionDto();
		personalityAndBehaviorQuestion.setQuestion(propertiesHelper.personalityAndBehaviorQuestion);
		personalityAndBehaviorQuestion.setQuestionCode("personalityandbehavior_question");
		List<UserProfileAnswerDto> answers = new ArrayList<>(4);
		answers.add(new UserProfileAnswerDto("personalityandbehavior_answer_1",
				propertiesHelper.personalityAndBehaviorAnswer1));
		answers.add(new UserProfileAnswerDto("personalityandbehavior_answer_2",
				propertiesHelper.personalityAndBehaviorAnswer2));
		answers.add(new UserProfileAnswerDto("personalityandbehavior_answer_3",
				propertiesHelper.personalityAndBehaviorAnswer3));
		answers.add(new UserProfileAnswerDto("personalityandbehavior_answer_4",
				propertiesHelper.personalityAndBehaviorAnswer4));
		personalityAndBehaviorQuestion.setAnswers(answers);
		personalityAndBehaviorQuestion.setTitle(propertiesHelper.personalityAndBehaviorTitle);
		personalityAndBehaviorQuestion.setDescription(propertiesHelper.personalityAndBehaviorDescription);
		personalityAndBehaviorQuestion.setQuestionId(6);
		return personalityAndBehaviorQuestion;
	}

	private UserProfileQuestionDto getQuestionOfTimeHorizon() {
		UserProfileQuestionDto timeHorizonQuestion = new UserProfileQuestionDto();
		timeHorizonQuestion.setQuestion(propertiesHelper.timeHorizonQuestion);
		timeHorizonQuestion.setQuestionCode("timehorizon_question");
		List<UserProfileAnswerDto> answers = new ArrayList<>(4);
		answers.add(new UserProfileAnswerDto("timehorizon_answer_1", propertiesHelper.timeHorizonAnswer1));
		answers.add(new UserProfileAnswerDto("timehorizon_answer_2", propertiesHelper.timeHorizonAnswer2));
		answers.add(new UserProfileAnswerDto("timehorizon_answer_3", propertiesHelper.timeHorizonAnswer3));
		answers.add(new UserProfileAnswerDto("timehorizon_answer_4", propertiesHelper.timeHorizonAnswer4));
		timeHorizonQuestion.setTitle(propertiesHelper.timeHorizonTitle);
		timeHorizonQuestion.setDescription(propertiesHelper.timeHorizonDescription);
		timeHorizonQuestion.setAnswers(answers);
		timeHorizonQuestion.setQuestionId(7);
		return timeHorizonQuestion;
	}

	private UserProfileQuestionDto getQuestionOfFirstLiquidityConsideration() {
		UserProfileQuestionDto liquidityConsiderationQuestion1 = new UserProfileQuestionDto();
		liquidityConsiderationQuestion1.setQuestion(propertiesHelper.liquidityConsiderationQuestion1);
		liquidityConsiderationQuestion1.setQuestionCode("liquidityconsideration_1_question");
		List<UserProfileAnswerDto> answers = new ArrayList<>(5);
		answers.add(new UserProfileAnswerDto("liquidityconsideration_1_answer_1",
				propertiesHelper.liquidityConsideration1Answer1));
		answers.add(new UserProfileAnswerDto("liquidityconsideration_1_answer_2",
				propertiesHelper.liquidityConsideration1Answer2));
		answers.add(new UserProfileAnswerDto("liquidityconsideration_1_answer_3",
				propertiesHelper.liquidityConsideration1Answer3));
		answers.add(new UserProfileAnswerDto("liquidityconsideration_1_answer_4",
				propertiesHelper.liquidityConsideration1Answer4));
		answers.add(new UserProfileAnswerDto("liquidityconsideration_1_answer_5",
				propertiesHelper.liquidityConsideration1Answer5));
		liquidityConsiderationQuestion1.setAnswers(answers);
		liquidityConsiderationQuestion1.setTitle(propertiesHelper.liquidityConsideration1Title);
		liquidityConsiderationQuestion1.setDescription(propertiesHelper.liquidityConsideration1Description);
		liquidityConsiderationQuestion1.setQuestionId(8);
		return liquidityConsiderationQuestion1;
	}

	private UserProfileQuestionDto getQuestionOfSecondLiquidityConsideration() {
		UserProfileQuestionDto liquidityConsiderationQuestion2 = new UserProfileQuestionDto();
		liquidityConsiderationQuestion2.setQuestion(propertiesHelper.liquidityConsiderationQuestion2);
		liquidityConsiderationQuestion2.setQuestionCode("liquidityconsideration_2_question");
		List<UserProfileAnswerDto> answers = new ArrayList<>(3);
		answers.add(new UserProfileAnswerDto("liquidityconsideration_2_answer_1",
				propertiesHelper.liquidityConsideration2Answer1));
		answers.add(new UserProfileAnswerDto("liquidityconsideration_2_answer_2",
				propertiesHelper.liquidityConsideration2Answer2));
		answers.add(new UserProfileAnswerDto("liquidityconsideration_2_answer_3",
				propertiesHelper.liquidityConsideration2Answer3));
		liquidityConsiderationQuestion2.setAnswers(answers);
		liquidityConsiderationQuestion2.setTitle(propertiesHelper.liquidityConsideration2Title);
		liquidityConsiderationQuestion2.setDescription(propertiesHelper.liquidityConsideration2Description);
		liquidityConsiderationQuestion2.setQuestionId(9);
		return liquidityConsiderationQuestion2;
	}

	@SuppressWarnings("unused")
	private UserProfileQuestionDto getQuestionOfIncome() {
		UserProfileQuestionDto incomeQuestion = new UserProfileQuestionDto();
		incomeQuestion.setQuestion(propertiesHelper.userannualincomequestion);
		incomeQuestion.setQuestionCode("annualincome_question");
		incomeQuestion.setQuestionId(1);
		return incomeQuestion;
	}

	private UserProfileQuestionDto getBasicInvestmentGoal() {
		UserProfileQuestionDto firstInvestmentGoal = new UserProfileQuestionDto();
		firstInvestmentGoal.setQuestion(propertiesHelper.investmentGoalBasicQuestion);
		firstInvestmentGoal.setQuestionCode("investmentgoal_basic_question");
		List<UserProfileAnswerDto> answers = new ArrayList<>(2);
		answers.add(new UserProfileAnswerDto("investmentgoal_basic_answer_1",
				propertiesHelper.investmentGoalBasicAnswer1, propertiesHelper.specificGoalImageUrl));
		answers.add(new UserProfileAnswerDto("investmentgoal_basic_answer_2",
				propertiesHelper.investmentGoalBasicAnswer2, propertiesHelper.generalInvestmentGoalImageUrl));
		firstInvestmentGoal.setAnswers(answers);
		firstInvestmentGoal.setQuestionId(1);
		return firstInvestmentGoal;
	}

	private UserProfileQuestionDto selectInvestmentGoal() {
		UserProfileQuestionDto selectInvestmentGoal = new UserProfileQuestionDto();
		selectInvestmentGoal.setQuestion(propertiesHelper.investmentGoalSelectQuestion);
		selectInvestmentGoal.setQuestionCode("investmentgoal_selectgoal_question");
		List<UserProfileGoalDto> goals = new ArrayList<>(8);
		goals.add(new UserProfileGoalDto(Goal.Car, propertiesHelper.carGoalDesc, propertiesHelper.carGoalCost,
				propertiesHelper.carGoalImageUrl));
		goals.add(new UserProfileGoalDto(Goal.ChildrenEducation, propertiesHelper.childaccountGoalDesc,
				propertiesHelper.childaccountGoalCost, propertiesHelper.childAccountGoalImageUrl));
		goals.add(new UserProfileGoalDto(Goal.DepositForHouse, propertiesHelper.depositforhouseGoalDesc,
				propertiesHelper.depositforhomeGoalCost, propertiesHelper.depositForHouseGoalImageUrl));
		goals.add(new UserProfileGoalDto(Goal.GeneralInvesting, propertiesHelper.generalinvestingGoalDesc,
				propertiesHelper.generalinvestingGoalCost, propertiesHelper.generalInvestmentGoalImageUrl));
		goals.add(new UserProfileGoalDto(Goal.Retirement, propertiesHelper.retirementGoalDesc,
				propertiesHelper.retirementGoalCost, propertiesHelper.retirementGoalImageUrl));
		selectInvestmentGoal.setGoals(goals);
		selectInvestmentGoal.setQuestionId(2);
		return selectInvestmentGoal;
	}

	public void saveFile(MultipartFile multipartFile) {
		String path = multipartFile.getOriginalFilename();
		File fileToCreate = new File(path);
		try {
			fileToCreate.createNewFile();
			multipartFile.transferTo(fileToCreate);
		} catch (IllegalStateException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
}