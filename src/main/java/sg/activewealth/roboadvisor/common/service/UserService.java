package sg.activewealth.roboadvisor.common.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import sg.activewealth.roboadvisor.banking.dto.BankDetailsDto;
import sg.activewealth.roboadvisor.common.dao.UserDao;
import sg.activewealth.roboadvisor.common.dao.UserPaymentMethodDao;
import sg.activewealth.roboadvisor.common.dto.KycAttachmentsDto;
import sg.activewealth.roboadvisor.common.enums.AgentOTPStatus;
import sg.activewealth.roboadvisor.common.enums.KycStatus;
import sg.activewealth.roboadvisor.common.model.Agent;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.model.UserPaymentMethod;
import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.helper.notification.UserNotificationDispatcher;
import sg.activewealth.roboadvisor.infra.helper.sms.SMSSender;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.infra.service.AsynchronousTask;
import sg.activewealth.roboadvisor.infra.utils.FileUtils;
import sg.activewealth.roboadvisor.infra.utils.HashUtils;
import sg.activewealth.roboadvisor.infra.utils.StringUtils;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioAssignmentCategory;

@Service

public class UserService extends AbstractService<User> {

	public UserService() {
		super(User.class);
	}

	@Autowired
	public void setDao(UserDao dao) {
		super.dao = dao;
	}

	@Autowired
	private UserPaymentMethodService userPaymentMethodService;

	@Autowired
	UserPaymentMethodDao userPaymentMethodDao;

	@Autowired
	private AgentService agentService;

	@Autowired
	private SMSSender smsSender;

	@Autowired
	private UserNotificationDispatcher userNotificationDispatcher;

	@Autowired
	private UserSubmissionService userSubmissionService;

	public User getSystemUser() {
		return this.retrieveByEmailAddress(propertiesHelper.systemUserEmailId);
	}

	@Override
	public ErrorsDto validateForSave(User model, ErrorsDto errors) throws ValidateException {
		errors = super.validateForSave(model, errors);

		if (ValidationUtils.getInstance().isEmptyString(model.getFirstName())) {
			errors.add(new ErrorDto("firstName", "error.required", "Name"));
		}
		if (ValidationUtils.getInstance().isEmptyString(model.getLastName())) {
			errors.add(new ErrorDto("lastName", "error.required", "Surname"));
		}

		if (ValidationUtils.getInstance().isEmptyString(model.getEmail())) {
			errors.add(new ErrorDto("email", "error.required", "Email address"));
		} else if (!ValidationUtils.getInstance().isValidEmailAddress(model.getEmail())) {
			errors.add(new ErrorDto("email", "error.invalid", "Email address"));
		}

		// uniqueness check for Email Address
		User user = retrieveByEmailAddress(model.getEmail());
		if (model.getCreatingNewObject()) {
			if (user != null) {
				errors.add(new ErrorDto("email", "error.alreadyused", "Email address"));
			}
		} else {
			if (user != null && !user.getId().equals(model.getId())) {
				errors.add(new ErrorDto("email", "error.alreadyused", "Email address"));
			}
		}

		// Uniqueness check for Phone
		if (model.getMobileNumber() != null) {
			User phoneUser = retrieveByPhone(model.getMobileNumber());
			if (model.getCreatingNewObject()) {
				if (phoneUser != null) {
					errors.add(new ErrorDto("mobileNumber", "error.duplicate.value", "User", "Number"));
				}
			} else {
				if (phoneUser != null && !phoneUser.getId().equals(model.getId())) {
					errors.add(new ErrorDto("mobileNumber", "error.duplicate.value", "User", "Number"));
				}
			}
			if(model.getMobileNumber().length() >= 20) {
				errors.add(new ErrorDto("mobileNumber", "error.userphone.length", "Mobile Number"));
			}
		}

		// Add validation for password
		if (model.getCreatingNewObject() && ValidationUtils.getInstance().isEmptyString(model.getSocialId())) {
			if (ValidationUtils.getInstance().isEmptyString(model.getPassword())) {
				errors.add(new ErrorDto("password", "error.required", "Password"));
			} else {
				if (model.getPassword().length() < 8) {
					errors.add(new ErrorDto("password", "error.minlength", "Password", 8));
				}
			}
		}

		if (!model.getCreatingNewObject() && ValidationUtils.getInstance().isEmptyString(model.getSocialId())) {
			if (org.apache.commons.lang.StringUtils.isNotBlank(model.getPassword())) {
				if (model.getPassword().length() != 44) {
					model.setNeedToRehashPassword(true);
					if (model.getPassword().length() < 8) {
						errors.add(new ErrorDto("password", "error.minlength", "Password", 8));
					} else if (ValidationUtils.getInstance().isEmptyString(model.getRepassword())) {
						errors.add(new ErrorDto("repassword", "error.required", "Password (again)"));
					}
				}

			}
		}

		if (!ValidationUtils.getInstance().isEmptyString(model.getKyc1FileName())) {
			File kycFile1 = new File(propertiesHelper.appUploads + File.separator + model.getKyc1FileName());
			checkUploadedFile(errors, kycFile1, model.getKyc1FileName(), "kyc1FileName");
		}

		if (!ValidationUtils.getInstance().isEmptyString(model.getKyc2FileName())) {
			File kycFile2 = new File(propertiesHelper.appUploads + File.separator + model.getKyc2FileName());
			checkUploadedFile(errors, kycFile2, model.getKyc2FileName(), "kyc2FileName");
		}

		if (!ValidationUtils.getInstance().isEmptyString(model.getKyc3FileName())) {
			File kycFile3 = new File(propertiesHelper.appUploads + File.separator + model.getKyc3FileName());
			checkUploadedFile(errors, kycFile3, model.getKyc3FileName(), "kyc3FileName");
		}

		// TODO need to discuss with client
		if (KycStatus.Completed.equals(model.getKycStatus())
				&& !ValidationUtils.getInstance().isEmptyString(model.getDeclarationsSignatureFileName())) {
			File signatureFile = new File(
					propertiesHelper.appUploads + File.separator + model.getDeclarationsSignatureFileName());
			checkUploadedFile(errors, signatureFile, model.getDeclarationsSignatureFileName(), "signatureFileName");
		}

		if(model.getAnnualIncome() != null){
			if (model.getAnnualIncome().precision() > 15 || model.getAnnualIncome().compareTo(BigDecimal.ZERO) <= 0) {
				errors.add(new ErrorDto("annualIncome", "error.invalid", "Annual Income"));
			}
		}
		
		if (user!= null && user.getAgentOtp()!=null && !ValidationUtils.getInstance().isEmptyString(model.getAgentOtp())) {
			if (!user.getAgentOtp().equals(model.getAgentOtp())) {
				errors.add(new ErrorDto("agentOtp", "error.invalid", "Agent OTP"));
			}
		}
		
		if(user.getAccountSummary() != null){
			if(model.getAccountSummary().precision() > 10 || model.getAccountSummary().compareTo(BigDecimal.ZERO) <= 0) {
				errors.add(new ErrorDto("accountSummary","error.invalid", "Account Summary"));
			}
		}
		
		return errors;
	}

	private void checkUploadedFile(ErrorsDto errors, File file, String fileName, String errorField) {
		if (!ValidationUtils.getInstance().isEmptyString(fileName)) {
			if (file.length() > KycAttachmentsDto.UPLOAD_MAX_FILESIZE) {
				errors.add(new ErrorDto(errorField, "upload.maxuploadsizeexceeded", errorField));
			}
			// TODO need to test without this condition for kyc document
			// !KycAttachmentsDto.UPLOAD_ALLOWED_EXTENSIONS.contains(fileExtension)
			if (ValidationUtils.getInstance().isEmptyString(FilenameUtils.getExtension(fileName))
					|| !KycAttachmentsDto.UPLOAD_ALLOWED_EXTENSIONS.contains(FilenameUtils.getExtension(fileName))) {
				errors.add(new ErrorDto(errorField, "upload.unacceptableextensionsexception", errorField));
			}
		} else {
			errors.add(new ErrorDto(errorField, "error.required", errorField));
		}
	}

	@Override
	public User preSave(User model) {

		model = super.preSave(model);

		if (model.getCreatingNewObject()) {
			model.setEmail(model.getEmail().toLowerCase());
		}

		if (model.getHashSalt() == null) {
			model.setHashSalt(StringUtils.getInstance().generateRandomToken(32, true));
			String hashed = HashUtils.getInstance().getHash(model.getPassword() != null ? model.getPassword() : "",
					model.getHashSalt());
			model.setPassword(hashed);
		}

		if (model.getPassword() == null) {
			String oldPassword = ((UserDao) dao).retrievePassword(model.getId());
			model.setPassword(oldPassword);
		} else {
			if (!model.getCreatingNewObject() && model.getNeedToRehashPassword()) {
				String hashed = HashUtils.getInstance().getHash(model.getPassword(), model.getHashSalt());
				model.setPassword(hashed);
			}
		}

		if (model.getAgent() == null || model.getAgent().getId() == null) {
			model.setAgent(null);
		} else {
			model.setAgent(agentService.retrieve(model.getAgent().getId()));
		}

		userSubmissionService.logUserStatusChange(model);
		userNotificationDispatcher.notifyChange(model);
		return model;
	}

	@Override
	public User postSave(User model) {
		model = super.postSave(model);
		for (MultipartFile multipartFile : model.getMultipartFiles()) {
			if (multipartFile.getSize() > 0) {
				String filePath = multipartFile.getOriginalFilename();
				String extension = FilenameUtils.getExtension(filePath);
				String fileName = null;
				if (multipartFile.getOriginalFilename().equals(model.getKyc1FileName())) {
					fileName = model.getId() + "_1." + extension;
					filePath = propertiesHelper.appUploads + File.separator + fileName;
					model.setKyc1FileName(fileName);
				} else if (multipartFile.getOriginalFilename().equals(model.getKyc2FileName())) {
					fileName = model.getId() + "_2." + extension;
					filePath = propertiesHelper.appUploads + File.separator + fileName;
					model.setKyc2FileName(fileName);
				} else if (multipartFile.getOriginalFilename().equals(model.getKyc3FileName())) {
					fileName = model.getId() + "_3." + extension;
					filePath = propertiesHelper.appUploads + File.separator + fileName;
					model.setKyc3FileName(fileName);
				} else if (multipartFile.getOriginalFilename().equals(model.getDeclarationsSignatureFileName())) {
					fileName = model.getId() + "_signature." + extension;
					filePath = propertiesHelper.appUploads + File.separator + fileName;
					model.setDeclarationsSignatureFileName(fileName);
				}
				try {
					File fileToCreate = new File(filePath);
					if (!fileToCreate.exists()) {
						fileToCreate.createNewFile();
					} else {
						fileToCreate.delete();
					}
					multipartFile.transferTo(fileToCreate);
				} catch (Exception e) {
					logger.error("File not uploaded");
				}
			}
		}
		return model;
	}

	public User retrieveByEmailAddress(String emailAddress) {
		return ((UserDao) dao).retrieveByEmailAddress(emailAddress);
	}

	// get particular user base on id.
	public User getUser(String id) {
		return ((UserDao) dao).getUser(id);
	}

  /**
   * This is required since we need to load existing user from DB and compare it with updatedModel which requires two objects with same ID
   * And it cause NonUniqueObjectException: A different object with the same identifier value was already associated with the session
   * @param id userId
   * @return evicted User Object
   */
  public User getUserAndEvictIt(String id) {
      final User user = ((UserDao) dao).getUser(id);
      dao.evict(user);
      return user;
    }

	public User retrieveByEmailAddressAndPassword(String emailAddress, String password) {
		User ret = ((UserDao) dao).retrieveByEmailAddress(emailAddress);
		if (ret != null && password != null) {
			// check password
			String hashed = HashUtils.getInstance().getHash(password, ret.getHashSalt());
			// is it the same?
			if (hashed.equals(ret.getPassword())) {
				return ret;
			}
		}
		return null;
	}

	public User retrieveByPhoneOrEmail(String emailOrPhone, String password) {
		User ret = ((UserDao) dao).retrieveByPhone(emailOrPhone);
		if (ret == null) {
			ret = ((UserDao) dao).retrieveByEmailAddress(emailOrPhone);
		}
		if (ret != null && password != null) {
			// check password
			String hashed = HashUtils.getInstance().getHash(password, ret.getHashSalt());
			// is it the same?
			if (hashed.equals(ret.getPassword())) {
				return ret;
			}
		}
		return null;
	}

	public boolean isEmailAddressUsed(String emailAddress) {
		return ((UserDao) dao).isEmailAddressUsed(emailAddress);
	}

	public User retrieveByPhone(String phone) {
		return ((UserDao) dao).retrieveByPhone(phone);
	}

	public void updateLastLoggedIn(String userId, String ipAddress, String userAgent) {
		((UserDao) dao).updateLastLoggedIn(userId, ipAddress, userAgent);
	}

	public String resetPassword(User user) {
		String newPassword = StringUtils.getInstance().generateRandomToken(8, false);
		String hashed = HashUtils.getInstance().getHash(newPassword, user.getHashSalt());
		user.setPassword(hashed);
		String sent[] = { user.getEmail() };
		mailSenderHelper.sendMail("ActiveWealth Password Reset", sent, "Your new password is " + newPassword, true,
				true);
		saveWithoutPrePost(user);
		return newPassword;
	}

	public User retrieveByToken(String token) {
		return ((UserDao) dao).retrieveByToken(token);
	}

	public User saveForFBLogin(User model) {
		return ((UserDao) dao).save(model);
	}

	public void sendNewMessageNotification(final User finalModel) {
		if (finalModel != null) {
			if (!ValidationUtils.getInstance().isEmptyString(finalModel.getEmail())) {
				// send out html email
				new AsynchronousTask(userOperationContextService) {
					@Override
					public void body() throws Exception {
						String content = new String(FileUtils.getInstance().getBytes(getClass().getClassLoader()
								.getResourceAsStream("email_tpl/user_new_message_notification.html")));

						// variables
						content = StringUtils.getInstance().replace(content, "{{name}}", finalModel.getEmail());
						content = StringUtils.getInstance().replace(content, "{{signInLink}}",
								securityService.generateSecureLink(finalModel, null, true));

						mailSenderHelper.sendMail(finalModel.getEmail() + ", you received a new message!",
								new String[] { finalModel.getEmail() }, content, true, false);
					}
				}.execute();
			}
		}
	}

	public User assignUserPaymentMethod(User model, String method, String source) {
		UserPaymentMethod paymentMethod = new UserPaymentMethod();
		paymentMethod.setPaymentMethod(method);
		paymentMethod.setUser(model);
		paymentMethod.setSourceId(source);
		userPaymentMethodService.save(paymentMethod);
		return super.postSave(model);
	}

	public User getUserBySocialId(String apiKey, String emailAddress) {
		User user = ((UserDao) dao).getUserByFBApiKey(apiKey, emailAddress);
		return user;
	}

	public User sendOtpToAgent(User user, Agent agent) {
		boolean response = false;
		String agentOtp = null;
		if (ValidationUtils.getInstance().isEmptyString(user.getAgentOtp())) {
			agentOtp = StringUtils.getInstance().generateRandomTokenInDigits(4);
			response = sendCodeToNexmo(agent.getName(), agent.getMobileNumber(), agentOtp);
		} else {
			agentOtp = user.getAgentOtp();
			response = sendCodeToNexmo(agent.getName(), agent.getMobileNumber(), agentOtp);

		}
		if (response) {
			user.setAgentOTPStatus(AgentOTPStatus.SentToAgent);
			user.setAgentOtp(agentOtp);
			user.setAgent(agent);
		} else {
			user.setAgentOTPStatus(AgentOTPStatus.NotCompleted);
		}
		saveWithoutPrePost(user);
		return user;
	}

	private boolean sendCodeToNexmo(String agentName, String phoneNumber, String agentOtp) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("name", agentName);
		data.put("number", phoneNumber);
		data.put("code", agentOtp);
		StringWriter writer = new StringWriter();
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(this.getClass(), "/");
		Template template = null;
		try {
			template = cfg.getTemplate("email_tpl" + File.separator + "OTPMessageTemplate.html");
			template.process(data, writer);
		} catch (IOException e) {
			logger.info(e.getMessage());
		} catch (TemplateException e) {
			logger.info(e.getMessage());
		}
		String content = writer.toString();
		return smsSender.sendSms(phoneNumber, content);
	}

	public boolean otpVerification(User user, String otp) {
		boolean otpVerified = false;
		user.setAgentOTPStatus(AgentOTPStatus.OtpVerificationFailed);
		if (user.getAgentOtp().equals(otp)) {
			user.setAgentOTPStatus(AgentOTPStatus.Completed);
			otpVerified = true;
			user.setPortfolioCategory(PortfolioAssignmentCategory.PrivateBVI);
		}
		saveWithoutPrePost(user);
		return otpVerified;
	}

	public ErrorsDto validateBankDetails(BankDetailsDto bankDetailsDto, ErrorsDto errors) {
		if (ValidationUtils.getInstance().isEmptyString(bankDetailsDto.getName())) {
			errors.add(new ErrorDto("name", "error.required", "Bank Name"));
		}
		if (ValidationUtils.getInstance().isEmptyString(bankDetailsDto.getAccountName())) {
			errors.add(new ErrorDto("accountName", "error.required", "Account Name"));
		}
		if (ValidationUtils.getInstance().isEmptyString(bankDetailsDto.getAccountNumber())) {
			errors.add(new ErrorDto("accountNumber", "error.required", "Account Number"));
		}
		return errors;
	}

	public PagingDto<User> retrieveForListPage(String[] accountStatus, String[] agentOTPStatus, String[] kycStatus,
			String[] bankDetailsStatus, String[] declarationStatus, PagingDto<User> pagingDto, Boolean isAdmin,
			String emailAddress) {

		return ((UserDao) dao).retrieveForListPage(accountStatus, agentOTPStatus, kycStatus, bankDetailsStatus,
				declarationStatus, pagingDto, isAdmin, emailAddress);
	}

	public boolean isAgentInUse(String agentId) {
		return ((UserDao) dao).isAgentUsed(agentId);
	}

}
