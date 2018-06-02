package sg.activewealth.roboadvisor.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import sg.activewealth.roboadvisor.banking.model.Redemption;
import sg.activewealth.roboadvisor.banking.model.Remittance;
import sg.activewealth.roboadvisor.banking.service.RedemptionService;
import sg.activewealth.roboadvisor.banking.service.RemittanceService;
import sg.activewealth.roboadvisor.common.enums.AccountStatus;
import sg.activewealth.roboadvisor.common.enums.AgentOTPStatus;
import sg.activewealth.roboadvisor.common.enums.BankDetailsStatus;
import sg.activewealth.roboadvisor.common.enums.DeclarationStatus;
import sg.activewealth.roboadvisor.common.enums.KycStatus;
import sg.activewealth.roboadvisor.common.enums.ResidenceCountry;
import sg.activewealth.roboadvisor.common.enums.UserProgressStatus;
import sg.activewealth.roboadvisor.common.model.Agent;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.model.UserSubmission;
import sg.activewealth.roboadvisor.common.service.AgentService;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.common.service.UserSubmissionService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.FormOptionDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.enums.UserOperationContextResultType;
import sg.activewealth.roboadvisor.infra.exception.ObjectNotFoundException;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.infra.utils.FileUtils;
import sg.activewealth.roboadvisor.infra.utils.SystemUtils;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioAssignmentCategory;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;
import sg.activewealth.roboadvisor.trade.model.UserTrade;
import sg.activewealth.roboadvisor.trade.service.UserTradeService;

@Controller
@RequestMapping("/admin/user")
public class UserController extends CRUDController<User, UserService> {

	@Autowired
	public void setService(UserService service) {
		this.service = service;
	}

	@Autowired
	public UserController(UserService service) {
		super(User.class, service);
	}

	@Autowired
	protected UserPortfolioService userportfolioService;

	@Autowired
	private AgentService agentService;
	
	@Autowired
	private UserTradeService userTradeService;
	
	@Autowired private RemittanceService remittanceService;

	@Autowired private RedemptionService redemptionService;
	
	@Autowired private UserSubmissionService userSubmissionService;
	

	@Override
	public Object[] preCreateUpdateGet(User model, HttpServletRequest request) {
		String isNonUser = request.getParameter("nu");
		if (isNonUser != null && isNonUser.equals("true")) {
			model.setNeedToRehashPassword(true);
		}
		
		List<FormOptionDto> residenceContryList = new ArrayList<FormOptionDto>();
		for (ResidenceCountry country : ResidenceCountry.values()) {
			residenceContryList.add(new FormOptionDto(country.name(), country.getCountryName()));
		}

		List<FormOptionDto> agentOtpStatusList = new ArrayList<FormOptionDto>();
		for (AgentOTPStatus agentOTPStatus : AgentOTPStatus.values()) {
			agentOtpStatusList.add(new FormOptionDto(agentOTPStatus.name(), agentOTPStatus.getLabel()));
		}

		List<FormOptionDto> kycStatusList = new ArrayList<FormOptionDto>();
		for (KycStatus kycStatus : KycStatus.values()) {
			kycStatusList.add(new FormOptionDto(kycStatus.name(), kycStatus.getLabel()));
		}

		List<FormOptionDto> bankDetailStatusList = new ArrayList<FormOptionDto>();
		for (BankDetailsStatus bankDetailStatus : BankDetailsStatus.values()) {
			bankDetailStatusList.add(new FormOptionDto(bankDetailStatus.name(), bankDetailStatus.getLabel()));
		}
		
		List<FormOptionDto> userProgressStatusList = new ArrayList<FormOptionDto>();
		for (UserProgressStatus userProgressStatus : UserProgressStatus.values()) {
			userProgressStatusList.add(new FormOptionDto(userProgressStatus.name(), userProgressStatus.name()));
		}

		List<FormOptionDto> portfolioAssignmentCategoryList = new ArrayList<FormOptionDto>();
		for (PortfolioAssignmentCategory portfolioAssignmentCategory : PortfolioAssignmentCategory.values()) {
			portfolioAssignmentCategoryList
					.add(new FormOptionDto(portfolioAssignmentCategory.name(), portfolioAssignmentCategory.getLabel()));
		}

		List<FormOptionDto> accountStatusOpts = new ArrayList<FormOptionDto>();
		for (AccountStatus accountStatus : AccountStatus.values()) {
			accountStatusOpts.add(new FormOptionDto(accountStatus.name(), accountStatus.getLabel()));
		}

		List<Portfolio> portfolioList = null;
		
		PagingDto<UserPortfolio> userPortfolioPagingDto = new PagingDto<>(null, null);
		userPortfolioPagingDto = userportfolioService.retriveByUser(model, userPortfolioPagingDto);
		List<UserTrade> userTrades = new ArrayList<>();
		if (userPortfolioPagingDto.getResults() != null && userPortfolioPagingDto.getResults().size() > 0) {
			for (UserPortfolio userPortfolio : userPortfolioPagingDto.getResults()) {
				userTrades = userTradeService.getUserTradeByUserPortfolioId(userPortfolio.getId());
				userPortfolio.setUserTradeList(userTrades);
			}
		}

		List<UserPortfolio> userPortfolioList = userPortfolioPagingDto.getResults();
		Collections.sort(userPortfolioList, Collections.reverseOrder(AbstractModel.COMPARE_BY_CREATION_DATE));
		
		List<Remittance> remittanceList = remittanceService.getRemittancesByUserId(model.getId());
		Collections.sort(remittanceList, Collections.reverseOrder(AbstractModel.COMPARE_BY_CREATION_DATE));
		
		List<Redemption> redemptionList = redemptionService.getRedemptionsByUserId(model.getId());
		Collections.sort(redemptionList, Collections.reverseOrder(AbstractModel.COMPARE_BY_CREATION_DATE));
		
		List<UserSubmission> userSubmissions = userSubmissionService.getUserSubmissionsByUserId(model.getId());
		
		return new Object[] { "model", model, "portfolioList", portfolioList, 
				"userPortfolioList", userPortfolioList,
				"remittanceList", remittanceList,
				"redemptionList", redemptionList,
				"residenceContryList", residenceContryList, "agentOTPStatusList", agentOtpStatusList, "kycStatusList",
				kycStatusList, "bankDetailStatus", bankDetailStatusList, "portfolioAssignmentCategoryList",
				portfolioAssignmentCategoryList, "accountStatusOpts", accountStatusOpts, "userProgressStatusList", userProgressStatusList,
				"userSubmissions", userSubmissions};
	}

	@Override
	public Object createUpdatePost(User model, Errors springErrors, HttpServletRequest request) {
		Object obj = super.createUpdatePost(model, springErrors, request);
		if (userSessionService.get() == null && model.getCreatingNewObject() && model.getNeedToSendMail()
				&& springErrors.hasErrors() == false) {
			userOperationContextService.set(UserOperationContextResultType.Success,
					super.getContextMessage("message.signup.success"));
			obj = "redirect:../";
		}
		return obj;
	}

	@RequestMapping(value = { "/profile/create", }, method = RequestMethod.GET)
	public Object createUpdateGet(HttpServletRequest request) {
		User user = new User();
		Object[] ret = preCreateUpdateGet(user, request);
		return modelAndView(getFullJspPath("profile"), ret).addObject("model", user);
	}

	@RequestMapping(value = { "/profile" }, method = RequestMethod.GET)
	public Object profile(HttpServletRequest request, @RequestParam String id) {
		// User model = userSessionService.get().getUser();
		User model = service.getUser(id);

		Object[] ret = preCreateUpdateGet(model, request);
		return modelAndView(getFullJspPath("profile"), ret).addObject("user", model);
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = { "/profile" }, method = RequestMethod.POST)
	public Object saveProfile(@ModelAttribute User model, Errors springErrors, HttpServletRequest request) {
		ErrorsDto errors = null;
		try {
			model = service.save(model);
		} catch (ValidateException e) {
			errors = super.convertValidateExceptionToErrors(e, springErrors);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			userOperationContextService.warn(e);
			addRejectError(springErrors, e.getMessage());
		}

		if (springErrors.hasErrors()) {
			addStandardFailureMessage();
			Object[] ret = preCreateUpdateGet(model, request);
			return modelAndView(getFullJspPath("profile"), ret).addObject("user", model);
		} else
			addStandardSuccessMessage();

		return redirect(model, request);
	}

	public @ResponseBody Object sendNewMessageNotification(@RequestParam(value = "userId") String userId) {
		service.sendNewMessageNotification(service.retrieve(userId, true));
		return SystemUtils.getInstance().buildMap(new HashMap<String, String>(), "success", true);
	}

	@Override
	public Object list(@ModelAttribute PagingDto<User> pagingDto, String ids, HttpServletRequest request) {
		String[] queryAccountStatus = request.getParameterValues("accountStatus");
		String[] queryagentOTPStatus = request.getParameterValues("agentOTPStatus");
		String[] querykycStatus = request.getParameterValues("kycStatus");
		String[] querybankDetailsStatus = request.getParameterValues("bankDetailsStatus");
		String[] queryDeclarationStatus = request.getParameterValues("declarationStatus");
		String queryEmailAddress = request.getParameter("emailAddress");
		Boolean isAdmin = Boolean.FALSE;
		if (null != request.getParameter("isAdmin")) {
			isAdmin = ("on".equals(request.getParameter("isAdmin")) ? true : false);
		}

		pagingDto = service.retrieveForListPage(queryAccountStatus, queryagentOTPStatus, querykycStatus,
				querybankDetailsStatus, queryDeclarationStatus, pagingDto, isAdmin, queryEmailAddress);

		List<FormOptionDto> accountStatusOpts = new ArrayList<FormOptionDto>();
		for (AccountStatus accountStatus : AccountStatus.values()) {
			accountStatusOpts.add(new FormOptionDto(accountStatus.getLabel(), accountStatus.getLabel()));
		}
		List<FormOptionDto> agentOTPStatusOpts = new ArrayList<FormOptionDto>();
		for (AgentOTPStatus agentOTPStatus : AgentOTPStatus.values()) {
			agentOTPStatusOpts.add(new FormOptionDto(agentOTPStatus.getLabel(), agentOTPStatus.getLabel()));
		}
		List<FormOptionDto> kycStatusOpts = new ArrayList<FormOptionDto>();
		for (KycStatus kycStatus : KycStatus.values()) {
			kycStatusOpts.add(new FormOptionDto(kycStatus.getLabel(), kycStatus.getLabel()));
		}
		List<FormOptionDto> bankDetailsStatusOpts = new ArrayList<FormOptionDto>();
		for (BankDetailsStatus bankDetailsStatus : BankDetailsStatus.values()) {
			bankDetailsStatusOpts.add(new FormOptionDto(bankDetailsStatus.getLabel(), bankDetailsStatus.getLabel()));
		}
		List<FormOptionDto> declarationStatusOpts = new ArrayList<FormOptionDto>();
		for (DeclarationStatus declarationStatus : DeclarationStatus.values()) {
			declarationStatusOpts.add(new FormOptionDto(declarationStatus.getLabel(), declarationStatus.getLabel()));
		}
		return modelAndView(getFullJspPath("list"), "list", pagingDto, "accountStatusOpts", accountStatusOpts,
				"agentOTPStatusOpts", agentOTPStatusOpts, "kycStatusOpts", kycStatusOpts, "bankDetailsStatusOpts",
				bankDetailsStatusOpts, "declarationStatusOpts", declarationStatusOpts, "isAdmin", isAdmin);
	}

	@ResponseBody
	@RequestMapping(value = { "/notifyagent" }, method = RequestMethod.POST)
	public Object sendAgentOtp(@RequestParam("userId") String userId, @RequestParam("agentCode") String agentCode) {
		User model = service.getUser(userId);
		Pattern.matches("^[a-zA-Z]{4}\\d{4}", agentCode);

		Agent agent = agentService.retrieveByAgentCode(agentCode);
		if (agent == null) {
			throw new ObjectNotFoundException("Agent with code: " + agentCode + " not found.");
		}
		model = service.sendOtpToAgent(model, agent);
		return model;
	}

	@ResponseBody
	@RequestMapping(value = { "/verifyotp" }, method = RequestMethod.POST)
	public Object veryfyOtp(@RequestParam("userId") String userId, @RequestParam("agentOtp") String agentOtp) {
		User model = service.getUser(userId);
		Boolean otpVerifiedStatus = service.otpVerification(model, agentOtp);
		return otpVerifiedStatus;
	}

	@ResponseBody
	@RequestMapping("/downloadfile")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
		String fileName = request.getParameter("fileName");
		String view = request.getParameter("view");

		String filePath = propertiesHelper.appUploads + File.separator + fileName;
		File file = new File(filePath);
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			
			if (null != view) {
				if (null != fileName) {
					if (fileName.endsWith(".pdf")) {
						response.setHeader("Content-Type", "application/pdf");
					} else if (fileName.endsWith(".png")){
						response.setHeader("Content-Type", "image/png");					
					} else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")){
						response.setHeader("Content-Type", "image/jpeg");					
					}	
				}
			} else {
				response.setHeader("content-disposition", "attachment; filename = \"" + fileName + "\"");				
			}
			
			InputStream io = new FileInputStream(file);
			out.write(FileUtils.getInstance().getBytes(io));
		} catch (IOException e) {
			logger.error(e);
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = { "/deletefile" }, method = RequestMethod.GET)
	public Object deleteFile(@RequestParam String fileName, @RequestParam String userId) {
		String filePath = propertiesHelper.appUploads + File.separator + fileName;
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}

		User user = service.retrieve(userId);
		if (fileName.equalsIgnoreCase(user.getKyc1FileName())) {
			user.setKyc1FileName(null);
			user.setKycStatus(KycStatus.NotSubmitted);
		} else if (fileName.equalsIgnoreCase(user.getKyc2FileName())) {
			user.setKyc2FileName(null);
			user.setKycStatus(KycStatus.NotSubmitted);
		} else if (fileName.equalsIgnoreCase(user.getKyc3FileName())) {
			user.setKyc3FileName(null);
			user.setKycStatus(KycStatus.NotSubmitted);
		} else if (fileName.equalsIgnoreCase(user.getDeclarationsSignatureFileName())) {
			user.setDeclarationsSignatureFileName(null);
		}

		user = service.saveWithoutPrePost(user);
		return user;
	}
	
	@Override
	public Object delete(User model, HttpServletRequest request) {
		service.delete(model);
		 return redirect(model, request);
	}	
	
	@RequestMapping("/generateReport")
	public void downloadReport(HttpServletResponse response) throws WriteException, IOException {
		service.exportAsExcel(((UserService)service).new Report() {
			int c =0;
			@Override
			protected void writeRow(WritableSheet sheet, User item, int row) throws WriteException {
				sheet.addCell(new Label(0, row, item.getId()));
				sheet.addCell(new Label(1, row, item.getCreatedBy()));
				sheet.addCell(new Label(2, row, item.getUpdatedBy() !=null ? item.getUpdatedBy() : " "));
				sheet.addCell(new Label(3, row, String.valueOf(item.getCreatedOn())));
				sheet.addCell(new Label(4, row, String.valueOf(item.getUpdatedOn() != null ? item.getUpdatedOn() : " ")));
				
				sheet.addCell(new Label(6, row, item.getFirstName()));
				sheet.addCell(new Label(7, row, item.getLastName()));
				sheet.addCell(new Label(8, row, item.getEmail()));
				sheet.addCell(new Label(9, row, item.getMobileNumber() !=null ? item.getMobileNumber() : " "));
				sheet.addCell(new Label(10, row, String.valueOf(item.getMobileVerified())));
				
				sheet.addCell(new Label(11, row, String.valueOf(item.getProgressStatus() !=null ? item.getProgressStatus().getValue() : " ")));
				sheet.addCell(new Label(12, row, String.valueOf(item.getAccountSummary() !=null ? item.getAccountSummary() : " ")));
				sheet.addCell(new Label(14, row, String.valueOf(item.getMobileVerifiedTimestamp() !=null ? item.getMobileVerifiedTimestamp() : " ")));
				sheet.addCell(new Label(15, row, item.getHashSalt() !=null ? item.getHashSalt() : " "));
				sheet.addCell(new Label(16, row, item.getPassword() !=null ? item.getPassword() : " "));
				sheet.addCell(new Label(17, row, String.valueOf(item.getIsAdmin() !=null ? item.getIsAdmin() : " ")));
				sheet.addCell(new Label(18, row, item.getLastLoggedInIpAddress() !=null ? item.getLastLoggedInIpAddress() : " "));
				sheet.addCell(new Label(19, row, String.valueOf(item.getDateOfBirth() !=null ? item.getDateOfBirth() : " ")));
				
				sheet.addCell(new Label(22, row, item.getBrokerSaxoApiKey() !=null ? item.getBrokerSaxoApiKey() : " "));
				sheet.addCell(new Label(23, row, item.getBrokerSaxoApiSecret() !=null ? item.getBrokerSaxoApiSecret() : " "));
				sheet.addCell(new Label(24, row, item.getSocialId() !=null ? item.getSocialId() : " "));
				sheet.addCell(new Label(25, row, String.valueOf(item.getAccountStatus() !=null ? item.getAccountStatus().getValue() : " ")));
				sheet.addCell(new Label(26, row, item.getAgent() !=null ? item.getAgent().getId() : " "));
				sheet.addCell(new Label(27, row, String.valueOf(item.getAgentOTPStatus()!=null ? item.getAgentOTPStatus().getValue() : " ")));
				sheet.addCell(new Label(28, row, String.valueOf(item.getKycStatus()!=null ? item.getKycStatus().getValue() : " ")));
				sheet.addCell(new Label(29, row, String.valueOf(item.getBankDetailsStatus() !=null?item.getBankDetailsStatus().getValue() : " ")));
				sheet.addCell(new Label(30, row, item.getAgentOtp() !=null ? item.getAgentOtp() : " "));
				
				sheet.addCell(new Label(31, row, item.getAgentOtp() !=null ? item.getKyc1FileName() : ""));
				sheet.addCell(new Label(32, row, item.getKyc2FileName() !=null ? item.getKyc2FileName() : ""));
				sheet.addCell(new Label(33, row, item.getKyc3FileName()!=null ? item.getKyc3FileName() : ""));
				sheet.addCell(new Label(34, row, item.getBankDetailsBankName()!=null ? item.getBankDetailsBankName() : " "));
				sheet.addCell(new Label(35, row, item.getBankDetailsBankAddress() !=null ? item.getBankDetailsBankAddress() : " "));
				sheet.addCell(new Label(36, row, item.getBankDetailsAba() !=null ? item.getBankDetailsAba() : " "));
				sheet.addCell(new Label(37, row, item.getBankDetailsChips() !=null ? item.getBankDetailsChips() : " "));
				sheet.addCell(new Label(38, row, item.getBankDetailsSwiftNumber() !=null ? item.getBankDetailsSwiftNumber() : " "));
				sheet.addCell(new Label(39, row, item.getBankDetailsAccountName() !=null ? item.getBankDetailsAccountName() : " "));
				sheet.addCell(new Label(40, row, item.getBankDetailsAccountNumber() !=null ? item.getBankDetailsAccountNumber() : " "));
				
				sheet.addCell(new Label(41, row, item.getBankDetailsReference() !=null ? item.getBankDetailsReference() : " "));
				sheet.addCell(new Label(42, row, String.valueOf(item.getDeclarationsAi() != null ? item.getDeclarationsAi() : " ")));
				sheet.addCell(new Label(43, row, String.valueOf(item.getDeclarationsPep() != null ? item.getDeclarationsPep() : " ")));
				sheet.addCell(new Label(44, row, String.valueOf(item.getDeclarationsCrc() != null ? item.getDeclarationsCrc() : " ")));
				sheet.addCell(new Label(45, row, String.valueOf(item.getDeclarationsTaxCrime() != null ? item.getDeclarationsTaxCrime() : " ")));
				sheet.addCell(new Label(46, row, item.getDeclarationsSourceOfIncome() !=null ? item.getDeclarationsSourceOfIncome() : " "));
				sheet.addCell(new Label(47, row, String.valueOf(item.getAgreementUserAgreement())));
				sheet.addCell(new Label(48, row, item.getDeclarationsSignatureFileName() !=null ? item.getDeclarationsSignatureFileName() : " "));
				sheet.addCell(new Label(49, row, String.valueOf(item.getResidenceCountry() !=null ? item.getResidenceCountry().getValue() : " ")));
				sheet.addCell(new Label(50, row, String.valueOf(item.getAnnualIncome() !=null ? item.getAnnualIncome() : " ")));
				
				sheet.addCell(new Label(51, row, String.valueOf(item.getAgreementUserAgreementAcknowledged() !=null ? item.getAgreementUserAgreementAcknowledged() : " ")));
				sheet.addCell(new Label(52, row, String.valueOf(item.getDeclarationsUsCitizen() !=null ? item.getDeclarationsUsCitizen() : " ")));
				sheet.addCell(new Label(53, row, String.valueOf(item.getPortfolioCategory() !=null ? item.getPortfolioCategory().getValue() : " ")));
				
				sheet.addCell(new Label(54, row, String.valueOf(item.getSignupEmailSent() !=null ? item.getSignupEmailSent() : " ")));
				sheet.addCell(new Label(55, row, String.valueOf(item.getKycUploadEmailSent() !=null ? item.getKycUploadEmailSent() : " ")));
				sheet.addCell(new Label(56, row, String.valueOf(item.getBankDetailsDeclarationsEmailSent() !=null ? item.getBankDetailsDeclarationsEmailSent() : " ")));
				sheet.addCell(new Label(57, row, String.valueOf(item.getBankDetailsDeclarationsStatusCompletedEmailSent() !=null ? item.getBankDetailsDeclarationsStatusCompletedEmailSent() : " ")));
				sheet.addCell(new Label(58, row, String.valueOf(item.getKycIssueEmailSent() !=null ? item.getKycIssueEmailSent() : " ")));
				sheet.addCell(new Label(59, row, String.valueOf(item.getKycCompletedEmailSent() !=null ? item.getKycCompletedEmailSent() : " ")));
			}

			@Override
			protected void writeMoreHeadings(WritableSheet sheet, int row) throws WriteException {
				return;
			}

			@Override
			protected void writeHeadings(WritableSheet sheet, int row) throws WriteException {
				sheet.addCell(new Label(0, row, "ID"));
				sheet.addCell(new Label(1, row, "Created By"));
				sheet.addCell(new Label(2, row, "Updated By"));
				sheet.addCell(new Label(3, row, "Created On"));
				sheet.addCell(new Label(4, row, "Updated On"));
				sheet.addCell(new Label(5, row, "Deleted"));
				
				sheet.addCell(new Label(6, row, "First Name"));
				sheet.addCell(new Label(7, row, "Last Name"));
				sheet.addCell(new Label(8, row, "Email Address"));
				sheet.addCell(new Label(9, row, "Phone Number"));
				sheet.addCell(new Label(10, row, "Verified"));
				
				sheet.addCell(new Label(11, row, "Progress Status"));
				sheet.addCell(new Label(12, row, "Account Summary"));
				sheet.addCell(new Label(13, row, "Joined Date"));
				sheet.addCell(new Label(14, row, "Verified Timestamp"));
				sheet.addCell(new Label(15, row, "Hash Salt"));
				sheet.addCell(new Label(16, row, "Password"));
				sheet.addCell(new Label(17, row, "Admin"));
				sheet.addCell(new Label(18, row, "Last Logged In IpAddress"));
				sheet.addCell(new Label(19, row, "Date Of Birth"));
				sheet.addCell(new Label(20, row, "Goal"));
				
				sheet.addCell(new Label(21, row, "Customer Id"));
				sheet.addCell(new Label(22, row, "Broker ApiKey"));
				sheet.addCell(new Label(23, row, "Broker ApiSecret"));
				sheet.addCell(new Label(24, row, "Social Id"));
				sheet.addCell(new Label(25, row, "Account Status"));
				sheet.addCell(new Label(26, row, "Agent ID"));
				sheet.addCell(new Label(27, row, "Agent OTPStatus"));
				sheet.addCell(new Label(28, row, "Kyc Status"));
				sheet.addCell(new Label(29, row, "Bank Details Status"));
				sheet.addCell(new Label(30, row, "Agent Otp"));
				
				sheet.addCell(new Label(31, row, "Kyc1 FileName"));
				sheet.addCell(new Label(32, row, "Kyc2 FileName"));
				sheet.addCell(new Label(33, row, "Kyc3 FileName"));
				sheet.addCell(new Label(34, row, "Bank Name"));
				sheet.addCell(new Label(35, row, "Bank Address"));
				sheet.addCell(new Label(36, row, "Bank Aba"));
				sheet.addCell(new Label(37, row, "Bank Chips"));
				sheet.addCell(new Label(38, row, "Bank Swift Number"));
				sheet.addCell(new Label(39, row, "Bank Account Name"));
				sheet.addCell(new Label(40, row, "Bank Account Number"));
				
				sheet.addCell(new Label(41, row, "Bank Reference"));
				sheet.addCell(new Label(42, row, "Bank Accredited Investor"));
				sheet.addCell(new Label(43, row, "Bank Pep"));
				sheet.addCell(new Label(44, row, "Bank Crc"));
				sheet.addCell(new Label(45, row, "Bank Tax Crime"));
				sheet.addCell(new Label(46, row, "Source Of Income"));
				sheet.addCell(new Label(47, row, "Agree Terms And Conditions"));
				sheet.addCell(new Label(48, row, "Bank Signature FileName"));
				sheet.addCell(new Label(49, row, "Residence Country"));
				sheet.addCell(new Label(50, row, "Annual Income"));
				
				sheet.addCell(new Label(51, row, "Acknowledge Recommedation"));
				sheet.addCell(new Label(52, row, "Us Citizen"));
				sheet.addCell(new Label(53, row, "Portfolio Assignment Category"));
				
				sheet.addCell(new Label(54, row, "Sign Up Email Sent"));
				sheet.addCell(new Label(55, row, "Kyc  Upload Email Sent"));
				sheet.addCell(new Label(56, row, "Bank Declaration Email Sent"));
				sheet.addCell(new Label(57, row, "Bank Status Completed Email Sent"));
				sheet.addCell(new Label(58, row, "Kyc Issue Email Sent"));
				sheet.addCell(new Label(59, row, "Kyc Completed Email Sent"));
			}

			@Override
			protected String getSheetName() {
				return "User";			}

			@Override
			protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {
				return super.getWorkbook(response);
			}

		}, null, null, response);
	}
	
}
