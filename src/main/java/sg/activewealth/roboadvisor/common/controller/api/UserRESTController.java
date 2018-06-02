package sg.activewealth.roboadvisor.common.controller.api;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

import javassist.tools.rmi.ObjectNotFoundException;
import sg.activewealth.roboadvisor.banking.dto.BankDetailsDto;
import sg.activewealth.roboadvisor.banking.model.Redemption;
import sg.activewealth.roboadvisor.banking.model.Remittance;
import sg.activewealth.roboadvisor.banking.service.RedemptionService;
import sg.activewealth.roboadvisor.banking.service.RemittanceService;
import sg.activewealth.roboadvisor.common.dto.ActivityDto;
import sg.activewealth.roboadvisor.common.dto.KycAttachmentsDto;
import sg.activewealth.roboadvisor.common.dto.SignatureAttachmentDto;
import sg.activewealth.roboadvisor.common.dto.UserDto;
import sg.activewealth.roboadvisor.common.dto.UserMobileVerificationDto;
import sg.activewealth.roboadvisor.common.dto.UserStatusDto;
import sg.activewealth.roboadvisor.common.dto.UserUpdateDto;
import sg.activewealth.roboadvisor.common.enums.BankDetailsStatus;
import sg.activewealth.roboadvisor.common.enums.KycStatus;
import sg.activewealth.roboadvisor.common.enums.ResidenceCountry;
import sg.activewealth.roboadvisor.common.model.Agent;
import sg.activewealth.roboadvisor.common.model.DeviceInfo;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.AgentService;
import sg.activewealth.roboadvisor.common.service.DeviceInfoService;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.controller.AbstractController;
import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.UserSessionDto;
import sg.activewealth.roboadvisor.infra.exception.JsonObjectNotFoundException;
import sg.activewealth.roboadvisor.infra.exception.OTPNotMatchingException;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.helper.sms.NexmoSMSSender;
import sg.activewealth.roboadvisor.infra.utils.FileUtils;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.dto.UserProfileAnswerDto;
import sg.activewealth.roboadvisor.portfolio.dto.UserProfileQuestionDto;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolioTransaction;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioTransactionService;

@RestController
@RequestMapping("/api/v1/user")
public class UserRESTController extends AbstractController {
	@Autowired
	private UserService userService;

	@Autowired
	private DeviceInfoService deviceInfoService;

	@Autowired
	private NexmoSMSSender nexmoSMSSender;

	@Autowired
	private PropertiesHelper propertiesHelper;
	
	@Autowired
	private UserPortfolioTransactionService userPortfolioTransactionService;

	@Autowired
	private AgentService agentService;
	
	@Autowired
	private RemittanceService remittanceService;

	@Autowired
	private RedemptionService redemptionService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> create(@RequestBody User user) {
		userService.save(user);
		userSessionService.initUserSession(user, false);
		UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getMobileNumber(), user.getProgressStatus(), user.getAccountSummary(), user.getDateOfBirth(),
				null, null, user.getResidenceCountry(), user.getAnnualIncome(), user.getAccountStatus());
		userDto.setToken(userSessionService.buildFreshCookieValue());
		UserStatusDto userStatusDto = getUserStatusDto(user,new UserStatusDto());
		userDto.setUserStatusDto(userStatusDto);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/checkIfDuplicateEmail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> checkIfDuplicateEmail(@RequestBody Map<String, String> data,
			HttpServletResponse response) {
		Map<String, Object> res = new LinkedHashMap<>(2);
		User user = userService.retrieveByEmailAddress(data.get("emailAddress"));

		if (user != null) {
			res.put("result", false);
			res.put("message", "Email is duplicated");
		} else {
			res.put("result", true);
			res.put("message", "Email is not duplicated");
		}

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@RequestMapping(value = "/kyc/get", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> retrieveKYCS(HttpServletResponse response) {
		UserSessionDto userSessionDto = userSessionService.get();
		User user = userSessionDto.getUser();

		String[] kycs = {user.getKyc1FileName(), user.getKyc2FileName(), user.getKyc3FileName() };
		List<String> urls = new ArrayList<>();

		for (String fileName : kycs ) {
			urls.add(String.format("%s/admin/user/downloadfile?filename=%s", propertiesHelper.appContextName, fileName));
		}

		Map<String, Object> result = new HashMap<>();
		result.put("kycs", urls);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/bankDetail/get", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> retrieveBankDetail(HttpServletResponse response) {
		UserSessionDto userSessionDto = userSessionService.get();
		User user = userSessionDto.getUser();

		Map<String, Object> result = new HashMap<>();
		result.put("bankName", user.getBankDetailsBankName());
		result.put("bankAddress", user.getBankDetailsBankAddress());
		result.put("bankAba", user.getBankDetailsAba());
		result.put("bankChips", user.getBankDetailsChips());
		result.put("bankBankSwiftNumber", user.getBankDetailsSwiftNumber());
		result.put("bankSwiftNumber", user.getBankDetailsSwiftNumber());
		result.put("bankAccountName", user.getBankDetailsAccountName());
		result.put("bankAccountNumber", user.getBankDetailsAccountNumber());
		result.put("bankReference", user.getBankDetailsReference());
		result.put("sourceOfIncome", user.getDeclarationsSourceOfIncome());
		result.put("usCitizen", user.getDeclarationsUsCitizen());

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/checkIfDuplicatePhone", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> checkIfDuplicatePhone(@RequestBody Map<String, String> data,
            HttpServletResponse response) {
        Map<String, Object> res = new LinkedHashMap<>(2);
        User user = userService.retrieveByPhone(data.get("phone"));

        if (user != null) {
            res.put("result", false);
            res.put("message", "Phone is duplicated ");
        } else {
            res.put("result", true);
            res.put("message", "Phone is not duplicated");
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/country/getCountries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, ResidenceCountry[]>> getLocalizedCountryInfo() {
		Map<String, ResidenceCountry[]> residenceCountryMap = new HashMap<>();
		residenceCountryMap.put("countries", ResidenceCountry.values());
		return new ResponseEntity<>(residenceCountryMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userId}/requestVerification", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserMobileVerificationDto> requestVerifyMobileNumber(@PathVariable String userId,
			@RequestBody UserMobileVerificationDto userMobileVerificationDto) {
		final User userToVerify = getUser(userId);
		userToVerify.setMobileNumber(userMobileVerificationDto.getPhone());
		userToVerify.setMobileVerified(Boolean.FALSE);
		userService.save(userToVerify);
		final String token = nexmoSMSSender.sendChallenge(userMobileVerificationDto.getPhone());
		userMobileVerificationDto.setToken(token);
		return new ResponseEntity<>(userMobileVerificationDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userId}/verifyNumber", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> verifyMobileNumber(@PathVariable String userId,
			@RequestBody UserMobileVerificationDto userMobileVerificationDto) {

		// Verify challenge
		nexmoSMSSender.verifyCode(userMobileVerificationDto.getToken(), userMobileVerificationDto.getCode());

		final User userToVerify = getUser(userId);
		userToVerify.setMobileVerified(Boolean.TRUE);
		userToVerify.setMobileVerifiedTimestamp(LocalDateTime.now());
		userService.save(userToVerify);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> getById(@PathVariable String userId) {
		final User user = getUser(userId);
		UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getMobileNumber(), user.getProgressStatus(), user.getAccountSummary(), user.getDateOfBirth(),
				null, null, user.getResidenceCountry(), user.getAnnualIncome(), user.getAccountStatus());
		userDto.setPortfolioAssignmentCategory(user.getPortfolioCategory());
		userDto.setHasAcknowledged(user.getAgreementUserAgreementAcknowledged());
		userDto.setAgree(user.getAgreementUserAgreement());
		UserStatusDto userStatusDto = getUserStatusDto(user,new UserStatusDto());
		userDto.setUserStatusDto(userStatusDto);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	private UserStatusDto getUserStatusDto(User user, UserStatusDto userStatusDto) {
		userStatusDto.setAgentOtp(user.getAgentOTPStatus());
		userStatusDto.setBankDetails(user.getBankDetailsStatus());
		userStatusDto.setKyc(user.getKycStatus());
		return userStatusDto;
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> updateUser(@PathVariable String userId, @RequestBody UserUpdateDto userUpdateDto)
			throws InvocationTargetException, IllegalAccessException {
		final User userPreSave = getUser(userId);
		if(userUpdateDto.getAgree()!=null) {
			userPreSave.setAgreementUserAgreement(userUpdateDto.getAgree());
		}
		if(userUpdateDto.getHasAcknowledged()!=null) {
			userPreSave.setAgreementUserAgreementAcknowledged(userUpdateDto.getHasAcknowledged());
		}
		if(userUpdateDto.getFirstName()!=null) {
			userPreSave.setFirstName(userUpdateDto.getFirstName());
		}
		if(userUpdateDto.getLastName()!=null) {
			userPreSave.setLastName(userUpdateDto.getLastName());
		}
		if(userUpdateDto.getEmailAddress()!=null) {
			userPreSave.setEmail(userUpdateDto.getEmailAddress());
		}
		if(userUpdateDto.getResidenceCountry()!=null){
			userPreSave.setResidenceCountry(userUpdateDto.getResidenceCountry());
		}
		if(userUpdateDto.getAnnualIncome()!=null && userUpdateDto.getAnnualIncome().compareTo(BigDecimal.ZERO) > 0 ) {
			userPreSave.setAnnualIncome(userUpdateDto.getAnnualIncome());
		}
		final User updatedUser = userService.save(userPreSave);
		UserDto userDto = new UserDto(updatedUser.getId(), updatedUser.getFirstName(), updatedUser.getLastName(),
				updatedUser.getEmail(), updatedUser.getMobileNumber(), updatedUser.getProgressStatus(),
				updatedUser.getAccountSummary(), updatedUser.getDateOfBirth(), null,
				null, updatedUser.getResidenceCountry(), updatedUser.getAnnualIncome(), updatedUser.getAccountStatus());
		UserStatusDto userStatusDto = getUserStatusDto(updatedUser,new UserStatusDto());
		userDto.setAgree(updatedUser.getAgreementUserAgreement());
		userDto.setHasAcknowledged(updatedUser.getAgreementUserAgreementAcknowledged());
		userDto.setUserStatusDto(userStatusDto);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

//	@RequestMapping(value = "/addPaymentMethod/{userId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Map<String, Object>> addPaymentDetails(@PathVariable String userId,
//			@RequestBody Map<String, Object> cardMap) throws InvocationTargetException, IllegalAccessException,
//			AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
//
//		Map<String, Object> ret = new LinkedHashMap<>(2);
//		List<String> userCards = new ArrayList<>();
//		Customer customer = null;
//		User user = getUser(userId);
//		List<UserPaymentMethod> userPaymentMethodlist = userPaymentMethodService.retrieveByUserId(userId);
//		for (UserPaymentMethod userPaymentMethod : userPaymentMethodlist) {
//			userCards.add(userPaymentMethod.getSourceId());
//		}
//		try {
//			if (userPaymentMethodlist.size() > 0 && userPaymentMethodlist.get(0).getId() != null) {
//				customer = Customer.retrieve(userPaymentMethodlist.get(0).getUser().getCustomerId());
//			} else {
//				customer = stripePaymentHelper.registerCustomer(user.getEmailAddress());
//			}
//			customer = stripePaymentHelper.addPaymentMethod(customer, cardMap);
//		} catch (Exception e) {
//			logger.error("Error occured while registering payment method with Stripe", e);
//			ret.put("success", Boolean.FALSE);
//			ret.put("message", "Error occured while registering payment method with Stripe:" + e.getMessage());
//			return new ResponseEntity<>(ret, HttpStatus.OK);
//		}
//		if (customer.getId() != null) {
//			for (ExternalAccount data : customer.getSources().getData()) {
//				if (!userCards.contains(data.getId())) {
//					UserPaymentMethod newUserPaymentMethod = new UserPaymentMethod();
//					newUserPaymentMethod.setUser(user);
//					newUserPaymentMethod.setPaymentMethod("card");
//					newUserPaymentMethod.setCreatedBy(userService.getSystemUser().getId());
//					newUserPaymentMethod.setSourceId(data.getId());
//					newUserPaymentMethod.setCreatedOn(LocalDateTime.now());
//					newUserPaymentMethod.setCardNumber(cardMap.get("number").toString());
//					userPaymentMethodService.save(newUserPaymentMethod);
//					stripePaymentHelper.updateDefaultSource(customer, data.getId());
//					if (user.getCustomerId() == null) {
//						user.setCustomerId(customer.getId());
//
//					}
//					user.setUserPaymentMethod(newUserPaymentMethod);
//					userService.save(user);
//				}
//			}
//		}
//		ret.put("success", Boolean.TRUE);
//		ret.put("message", "Payment Method Added Successfully");
//		return new ResponseEntity<>(ret, HttpStatus.OK);
//	}

//	@RequestMapping(value = "/{userId}/fundtransfer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Map<String, Object>> transferFund(@PathVariable String userId,
//			@RequestBody UserPayment userPayment) {
//		Map<String, Object> ret = new LinkedHashMap<>(2);
//
//		User user = getUser(userId);
//		if (user != null && user.getUserPaymentMethod() == null) {
//			ret.put("success", Boolean.FALSE);
//			ret.put("message", "Please add Payment Method before Transferring funds");
//			return new ResponseEntity<>(ret, HttpStatus.OK);
//		}
//		userPayment.setStatus(PaymentStatus.ReadyForPayment);
//		userPayment.setUser(user);
//		userPayment.setPaymentMethod(user.getUserPaymentMethod());
//		userPaymentService.save(userPayment);
//
//		ret.put("success", Boolean.TRUE);
//		ret.put("message", "Fund Transferred Successfully");
//		return new ResponseEntity<>(ret, HttpStatus.OK);
//	}

	@RequestMapping(value = "/{userId}/notifyagent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> notifyAgent(@PathVariable String userId,
			@RequestBody Map<String, Object> map) throws ObjectNotFoundException {
		User user = getUser(userId);
		Map<String, Object> ret = new LinkedHashMap<>(3);
		String agentCode = "";
		if (map.get("agentCode") != null) {
			agentCode = (String) map.get("agentCode");
		}
		ErrorsDto errors = new ErrorsDto();
		Boolean isValidAgentCode = Pattern.matches("^[a-zA-Z]{4}\\d{4}", agentCode);
		if(!isValidAgentCode) {
			errors.add(new ErrorDto("agentCode", "error.invalid", "Reference Code is "));
			throw new ValidateException(errors);
		}
		
		Agent agent = agentService.retrieveByAgentCode(agentCode);
		if (agent == null) {
			throw new JsonObjectNotFoundException("Reference with code: " + agentCode + " not found.");
		}
		UserStatusDto userStatusDto = new UserStatusDto();
		user = userService.sendOtpToAgent(user, agent);
		userStatusDto.setAgentOtp(user.getAgentOTPStatus());
		ret.put("userStatus", userStatusDto);
		ret.put("success", Boolean.TRUE);
		// TODO Remove code after integration with Nexmo is done
		ret.put("message", "Access token sent to Agent successfully -" + user.getAgentOtp());
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userId}/otpverification", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> otpverification(@PathVariable String userId,
			@RequestBody Map<String, Object> map) {
		User user = getUser(userId);
		Map<String, Object> ret = new LinkedHashMap<>(3);
		String otp = "";
		if (map.get("otp") != null) {
			otp = (String) map.get("otp");
		}
		UserStatusDto userStatusDto = new UserStatusDto();
		String message = "";
		Boolean otpVerifiedStatus = userService.otpVerification(user, otp);

		if (otpVerifiedStatus) {
			message = "OTP verfied successfully";
		} else {
			message = "OTP Verification Failed";
		}
		userStatusDto.setAgentOtp(user.getAgentOTPStatus());

		ret.put("userStatus", userStatusDto);
		ret.put("success", otpVerifiedStatus);
		ret.put("message", message);
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userId}/kyc", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> submitKyc(@PathVariable String userId,
			@RequestBody KycAttachmentsDto kycAttachmentsDto) {
		User user = getUser(userId);
		ErrorsDto errors = new ErrorsDto();

		List<MultipartFile> multipartFiles = new ArrayList<>();
		// Upload first kyc document start
		if (!ValidationUtils.getInstance().isEmptyString(kycAttachmentsDto.getKycContentType1())
				&& !ValidationUtils.getInstance().isEmptyString(kycAttachmentsDto.getKycDoc1())) {
			String kyc1FileName = userId + "_1";
			kyc1FileName = FileUtils.getInstance().setFileExtensionUsingContentType(kyc1FileName,
					kycAttachmentsDto.getKycContentType1());
			String encodedFilePath = propertiesHelper.encodedFileUpload + File.separator + kyc1FileName;
			String filePath = propertiesHelper.appUploads + File.separator + kyc1FileName;
			if (!ValidationUtils.getInstance().isEmptyString(FilenameUtils.getExtension(kyc1FileName))) {
				MultipartFile multipartFile = FileUtils.getInstance().convertBase64ToMultipartFile(kycAttachmentsDto.getKycDoc1(), kyc1FileName,
						kycAttachmentsDto.getKycContentType1(), encodedFilePath,filePath);
				if (multipartFile != null) {
					multipartFiles.add(multipartFile);
					user.setKyc1FileName(kyc1FileName);
					logger.debug("Kyc document uploaded file name1 :" + kyc1FileName);
				} else {
					errors.add(new ErrorDto("kyc1FileName", "error.file.notfound"));
				}
			} else {
				errors.add(new ErrorDto("kyc1FileName", "error.invalid", "kyc1 Content type"));
			}
		}
		// Upload first kyc document end
		// Upload second kyc document start
		if (!ValidationUtils.getInstance().isEmptyString(kycAttachmentsDto.getKycContentType2())
				&& !ValidationUtils.getInstance().isEmptyString(kycAttachmentsDto.getKycDoc2())) {
			String kyc2FileName = userId + "_2";
			kyc2FileName = FileUtils.getInstance().setFileExtensionUsingContentType(kyc2FileName,
					kycAttachmentsDto.getKycContentType2());
			String encodedFilePath = propertiesHelper.encodedFileUpload + File.separator + kyc2FileName;
			String filePath = propertiesHelper.appUploads + File.separator + kyc2FileName;
			if (!ValidationUtils.getInstance().isEmptyString(FilenameUtils.getExtension(kyc2FileName))) {
				MultipartFile multipartFile = FileUtils.getInstance().convertBase64ToMultipartFile(kycAttachmentsDto.getKycDoc2(), kyc2FileName,
						kycAttachmentsDto.getKycContentType2(), encodedFilePath,filePath);
				if (multipartFile != null) {
					multipartFiles.add(multipartFile);
					user.setKyc2FileName(kyc2FileName);
					logger.debug("Kyc document uploaded file name2 :" + kyc2FileName);
				} else {
					errors.add(new ErrorDto("kyc2FileName", "error.file.notfound"));
				}
			} else {
				errors.add(new ErrorDto("kyc2FileName", "error.invalid", "kyc2 Content Type"));
			}
		}
		// Upload second kyc document end
		// Upload third kyc document start
		if (!ValidationUtils.getInstance().isEmptyString(kycAttachmentsDto.getKycContentType3())
				&& !ValidationUtils.getInstance().isEmptyString(kycAttachmentsDto.getKycDoc3())) {
			String kyc3FileName = userId + "_3";
			kyc3FileName = FileUtils.getInstance().setFileExtensionUsingContentType(kyc3FileName,
					kycAttachmentsDto.getKycContentType3());
			String encodedFilePath = propertiesHelper.encodedFileUpload + File.separator + kyc3FileName;
			String filePath = propertiesHelper.appUploads + File.separator + kyc3FileName;
			if (!ValidationUtils.getInstance().isEmptyString(FilenameUtils.getExtension(kyc3FileName))) {
				MultipartFile multipartFile = FileUtils.getInstance().convertBase64ToMultipartFile(kycAttachmentsDto.getKycDoc3(), kyc3FileName,
						kycAttachmentsDto.getKycContentType3(), encodedFilePath,filePath);
				if (multipartFile != null) {
					multipartFiles.add(multipartFile);
					user.setKyc3FileName(kyc3FileName);
					logger.debug("Kyc document uploaded file name3 :" + kyc3FileName);
				} else {
					errors.add(new ErrorDto("kyc3FileName", "error.file.notfound"));
				}
			} else {
				errors.add(new ErrorDto("kyc3FileName", "error.invalid", "kyc3 Content Type"));
			}
		}
		user.setMultipartFiles(multipartFiles);
		try {
			if (!errors.hasErrors()) {
				user.setKycStatus(KycStatus.Submitted);
				userService.save(user);
			}
		} catch (ValidateException e) {
			errors = e.getErrors();
			for (ErrorDto errorDto : errors.getErrors()) {
				if (errorDto.getErrorField().equals("kyc1FileName")) {
					user.setKyc1FileName(null);
				} else if (errorDto.getErrorField().equals("kyc2FileName")) {
					user.setKyc2FileName(null);
				} else if (errorDto.getErrorField().equals("kyc3FileName")) {
					user.setKyc3FileName(null);
				}
			}
		} finally {
			if (errors.hasErrors()) {
				user.setKycStatus(KycStatus.SubmissionIssues);
				userService.saveWithoutPrePost(user);
				throw new ValidateException(errors);
			}
		}
		UserStatusDto userStatusDto = new UserStatusDto();
		userStatusDto.setAgentOtp(user.getAgentOTPStatus());
		userStatusDto.setKyc(user.getKycStatus());
		Map<String, Object> ret = new LinkedHashMap<>(3);
		ret.put("userStatus", userStatusDto);
		ret.put("success", Boolean.TRUE);
		ret.put("message", "KYC Document Uplaoded successfully");
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userId}/bankdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> submitBankDetails(@PathVariable String userId,
			@RequestBody BankDetailsDto bankDetailsDto) {
		Map<String, Object> ret = new LinkedHashMap<>(3);
		User user = getUser(userId);
		ErrorsDto errors = new ErrorsDto();
		errors = checkKycSubmittion(errors, user.getKycStatus());
		if(errors.hasErrors()) {
			throw new ValidateException(errors);
		}
		
		UserStatusDto userStatusDto = new UserStatusDto();
		errors = userService.validateBankDetails(bankDetailsDto, errors);
		
		if(errors.hasErrors()) {
			user.setBankDetailsStatus(BankDetailsStatus.SubmissionIssues);
			userService.saveWithoutPrePost(user);
			throw new ValidateException(errors);
		}

		user.setBankDetailsBankName(bankDetailsDto.getName());
		user.setBankDetailsBankAddress(bankDetailsDto.getAddress());
		user.setBankDetailsAba(bankDetailsDto.getAba());
		user.setBankDetailsChips(bankDetailsDto.getChips());
		user.setBankDetailsSwiftNumber(bankDetailsDto.getSwift());
		user.setBankDetailsAccountName(bankDetailsDto.getAccountName());
		user.setBankDetailsAccountNumber(bankDetailsDto.getAccountNumber());
		user.setBankDetailsReference(bankDetailsDto.getReference());
		user.setDeclarationsAi(bankDetailsDto.getAccreditedInvestor());
		user.setDeclarationsPep(bankDetailsDto.getPep());
		user.setDeclarationsCrc(bankDetailsDto.getCrc());
		user.setDeclarationsTaxCrime(bankDetailsDto.getTaxcrime());
		user.setDeclarationsSourceOfIncome(bankDetailsDto.getSourceOfIncome());
		user.setDeclarationsUsCitizen(bankDetailsDto.getUsCitizen());
		
		if(user.getDeclarationsSignatureFileName() != null) {
			if(BankDetailsStatus.NotSubmitted.equals(user.getBankDetailsStatus()) || BankDetailsStatus.SubmissionIssues.equals(user.getBankDetailsStatus())) {
				user.setBankDetailsStatus(BankDetailsStatus.Submitted);
			}
		} else {
			user.setBankDetailsStatus(BankDetailsStatus.NotSubmitted);
		}
		
		userService.save(user);

		userStatusDto.setAgentOtp(user.getAgentOTPStatus());
		userStatusDto.setKyc(user.getKycStatus());
		userStatusDto.setBankDetails(user.getBankDetailsStatus());
		ret.put("userStatus", userStatusDto);
		ret.put("success", Boolean.TRUE);
		ret.put("message", "Bank Details submitted successfully");
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userId}/bankdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getBankDetails(@PathVariable String userId) {
		Map<String, Object> ret = new LinkedHashMap<>(3);
		User user = getUser(userId);
		BankDetailsDto bankDetailsDto = new BankDetailsDto();
		bankDetailsDto.setName(user.getBankDetailsBankName());
		bankDetailsDto.setAddress(user.getBankDetailsBankAddress());
		bankDetailsDto.setAba(user.getBankDetailsAba());
		bankDetailsDto.setChips(user.getBankDetailsChips());
		bankDetailsDto.setSwift(user.getBankDetailsSwiftNumber());
		bankDetailsDto.setAccountName(user.getBankDetailsAccountName());
		bankDetailsDto.setAccountNumber(user.getBankDetailsAccountNumber());
		bankDetailsDto.setReference(user.getBankDetailsReference());
		bankDetailsDto.setAccreditedInvestor(user.getDeclarationsAi());
		bankDetailsDto.setPep(user.getDeclarationsPep());
		bankDetailsDto.setCrc(user.getDeclarationsCrc());
		bankDetailsDto.setTaxcrime(user.getDeclarationsTaxCrime());
		bankDetailsDto.setSourceOfIncome(user.getDeclarationsSourceOfIncome());
		bankDetailsDto.setUsCitizen(user.getDeclarationsUsCitizen());
		ret.put("bankDetails", bankDetailsDto);
		ret.put("success", Boolean.TRUE);
		ret.put("message", "Bank Details returned");
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userId}/signature", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> submitSignature(@PathVariable String userId,
			@RequestBody SignatureAttachmentDto signatureAttachmentDto) {
		User user = getUser(userId);
		ErrorsDto errors = new ErrorsDto();
		errors = checkKycSubmittion(errors, user.getKycStatus());
		if(errors.hasErrors()) {
			throw new ValidateException(errors);
		}
		
		String signatureFileName = userId + "_signature";
		if (!ValidationUtils.getInstance().isEmptyString(signatureAttachmentDto.getSignatureContentType())
				&& !ValidationUtils.getInstance().isEmptyString(signatureAttachmentDto.getSignatureFile())) {

			signatureFileName = FileUtils.getInstance().setFileExtensionUsingContentType(signatureFileName,
					signatureAttachmentDto.getSignatureContentType());

			String encodedFilePath = propertiesHelper.encodedFileUpload + File.separator + signatureFileName;
			String filePath = propertiesHelper.appUploads + File.separator + signatureFileName;
			if (!ValidationUtils.getInstance().isEmptyString(FilenameUtils.getExtension(signatureFileName))) {
				List<MultipartFile> multipartFiles = new ArrayList<>();
				MultipartFile multipartFile = FileUtils.getInstance().convertBase64ToMultipartFile(signatureAttachmentDto.getSignatureFile(),
						signatureFileName, signatureAttachmentDto.getSignatureContentType(), encodedFilePath,filePath);
				if (multipartFile != null) {
					multipartFiles.add(multipartFile);
					user.setMultipartFiles(multipartFiles);
					user.setDeclarationsSignatureFileName(signatureFileName);
				} else {
					errors.add(new ErrorDto("error.file.notfound"));
					throw new ValidateException(errors);
				}
			} else {
				errors.add(new ErrorDto("signatureFileName", "error.invalid", "Signature Content Type"));
			}
		}
		try {
			if (!errors.hasErrors()) {
				if(user.getBankDetailsBankName() != null && user.getBankDetailsAccountName() != null && user.getBankDetailsAccountNumber() != null) {
					if(BankDetailsStatus.NotSubmitted.equals(user.getBankDetailsStatus()) || BankDetailsStatus.SubmissionIssues.equals(user.getBankDetailsStatus())) {
						user.setBankDetailsStatus(BankDetailsStatus.Submitted);
					}
				} else {
					user.setBankDetailsStatus(BankDetailsStatus.NotSubmitted);
				}
				userService.save(user);
			}
		} catch (ValidateException e) {
			errors = e.getErrors();
			for (ErrorDto errorDto : errors.getErrors()) {
				if (errorDto.getErrorField().equals("signatureFileName")) {
					user.setDeclarationsSignatureFileName(null);
					user.setBankDetailsStatus(BankDetailsStatus.SubmissionIssues);
					userService.saveWithoutPrePost(user);
				}
			}
		} finally {
			if (errors.hasErrors()) {
				throw new ValidateException(errors);

			}
		}
		
		UserStatusDto userStatusDto = new UserStatusDto();
		userStatusDto.setAgentOtp(user.getAgentOTPStatus());
		userStatusDto.setKyc(user.getKycStatus());
		userStatusDto.setBankDetails(user.getBankDetailsStatus());
		logger.debug("Signature uploaded fileName :" + signatureFileName);
		Map<String, Object> ret = new LinkedHashMap<>(3);
		ret.put("userStatus", userStatusDto);
		ret.put("success", Boolean.TRUE);
		ret.put("message", "Signature Uplaoded successfully");
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
	
	public ErrorsDto checkKycSubmittion(ErrorsDto errors, KycStatus Status) {
		String errorMessage = "KYC Document";
		if(KycStatus.NotSubmitted.equals(Status)) {
			errors.add(new ErrorDto("kycStatus", "error.notSubmitted", errorMessage));
		} else if(KycStatus.Submitted.equals(Status) || KycStatus.AwaitingApproval.equals(Status)) {
			errors.add(new ErrorDto("kycStatus", "error.approve", errorMessage));
		} else if(KycStatus.SubmissionIssues.equals(Status)){
			errors.add(new ErrorDto("kycStatus", "error.submittionIssue", errorMessage));
		} else if(KycStatus.Rejected.equals(Status)) {
			errors.add(new ErrorDto("kycStatus", "error.rejected", errorMessage));
		}
		return errors;
	}
	
	@RequestMapping(value = "/{userId}/activity", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ActivityDto>> getActivity(@PathVariable String userId, HttpServletRequest request) {
		List<ActivityDto> activityDtos = new ArrayList<>();
		String startDate = request.getParameter("from");
		String endDate = request.getParameter("to");
		if (userId == null) {
			throw new JsonObjectNotFoundException("User Id is required");
		}
		User user = userSessionService.get().getUser();
		if (startDate == null || endDate == null) {
			throw new JsonObjectNotFoundException("Start DateTime or End DateTime are required");
		}
		LocalDateTime startDateTime = null;
		LocalDateTime endDateTime = null;
		ErrorsDto errors = new ErrorsDto();
		try {
			LocalDate startdate = LocalDate.parse(startDate);
			LocalDate enddate = LocalDate.parse(endDate);
			startDateTime = LocalDateTime.of(startdate, LocalTime.of(0, 0, 0));
			endDateTime = LocalDateTime.of(enddate, LocalTime.of(23, 23, 59));
		} catch (DateTimeParseException e) {
			errors.add(new ErrorDto(e.getParsedString(), "error.invalid", "StartDate or EndDate"));
			throw new ValidateException(errors);
		}

		List<UserPortfolioTransaction> userPortfolioTransactions = userPortfolioTransactionService
				.getByCreatedOnAndUser(startDateTime, endDateTime, user.getId());

		for (UserPortfolioTransaction userPortfolioTransaction : userPortfolioTransactions) {
			switch (userPortfolioTransaction.getStatus()) {
			case UserPortfolioAssigned:
				activityDtos
						.add(new ActivityDto(
								userPortfolioTransaction.getUserPortfolio().getPortfolio().getName()
										+ " Portfolio Assigned",
								userPortfolioTransaction.getCreatedOn(),
								userPortfolioTransaction.getUserPortfolio().getPortfolio().getRiskProfile()
										.getKey(),
								"Predicted "
										+ userPortfolioTransaction.getUserPortfolio().getPortfolio()
												.getProjectedReturns().setScale(0, BigDecimal.ROUND_DOWN)
										+ "% growth Annually"));
				break;
			case UserPortfolioFunded:
				activityDtos.add(new ActivityDto(
						userPortfolioTransaction.getUserPortfolio().getPortfolio().getName() + " Portfolio Funded",
						userPortfolioTransaction.getCreatedOn(),
						"Current Value S$" + userPortfolioTransaction.getUserPortfolio().getNetInvestmentAmount(),
						"Invested S$" + userPortfolioTransaction.getUserPortfolio().getNetInvestmentAmount()));
				break;
			case UserPortfolioExecuted:
				activityDtos
						.add(new ActivityDto(
								userPortfolioTransaction.getUserPortfolio().getPortfolio().getName()
										+ " Portfolio Executed",
								userPortfolioTransaction.getCreatedOn(),
								"Current Value S$" + userPortfolioTransaction.getUserPortfolio().getNetAssetValue(),
								"Invested S$ " + userPortfolioTransaction.getUserPortfolio().getNetInvestmentAmount()));
				break;
			case UserPortfolioReadyForClose:
				activityDtos.add(new ActivityDto(
						userPortfolioTransaction.getUserPortfolio().getPortfolio().getName()
								+ " Portfolio Ready to Close",
						userPortfolioTransaction.getCreatedOn(),
						"Current Value S$" + userPortfolioTransaction.getUserPortfolio().getNetAssetValue(),
						"Invested S$ " + userPortfolioTransaction.getUserPortfolio().getNetInvestmentAmount()));
				break;
			case UserPortfolioClosed:
				activityDtos
						.add(new ActivityDto(
								userPortfolioTransaction.getUserPortfolio().getPortfolio().getName()
										+ " Portfolio Closed",
								userPortfolioTransaction.getCreatedOn(),
								"Current Value S$" + userPortfolioTransaction.getUserPortfolio().getNetAssetValue(),
								"Invested S$ " + userPortfolioTransaction.getUserPortfolio().getNetInvestmentAmount()));
				break;
			case UserPortfolioFundReleased:
				activityDtos.add(new ActivityDto(
						"Released funds for " + userPortfolioTransaction.getUserPortfolio().getPortfolio().getName()
								+ " Portfolio",
						userPortfolioTransaction.getCreatedOn(),
						"Current Value S$" + userPortfolioTransaction.getUserPortfolio().getNetAssetValue(),
						"Invested S$ " + userPortfolioTransaction.getUserPortfolio().getNetInvestmentAmount()));
				break;
			default:
				break;
			}

		}

		List<Remittance> remittances = remittanceService.getRemittancesByUserId(userId);
		List<Redemption> redemptions = redemptionService.getRedemptionsByUserId(userId);
		for (Remittance remittance : remittances) {
			activityDtos.add(new ActivityDto(
					"Remittance for - " + remittance.getUserPortfolio().getPortfolio().getName(),
					remittance.getCreatedOn(), remittance.getBrokerFundingStatus().getLabel(),
					remittance.getInvestorRemittanceRemittedAmount() != null ? String.valueOf(remittance.getInvestorRemittanceRemittedAmount()) : "-"));
		}

		for (Redemption redemption : redemptions) {
			activityDtos.add(new ActivityDto(
					"Redemption for - " + redemption.getUserPortfolio().getPortfolio().getName(),
					redemption.getCreatedOn(), redemption.getRedemptionStatus().getLabel(),
					redemption.getRedemptionAmount() != null ? String.valueOf(redemption.getRedemptionAmount()) : "-"));
		}
	
		activityDtos.sort((ActivityDto o1, ActivityDto o2) -> o1.getDateLabel().compareTo(o2.getDateLabel()));
		Collections.reverse(activityDtos);
		return new ResponseEntity<>(activityDtos, HttpStatus.OK);
	}

	@RequestMapping(value = "/profilequestions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserProfileQuestionDto>> getUserProfileQuestions() {

		final List<UserProfileQuestionDto> questionDtos = new ArrayList<>();

		questionDtos.add(getUserProfileQuestionOfSavingsFor());
		questionDtos.add(getUserProfileQuestionOfAnnualIncome());
		questionDtos.add(getUserProfileQuestionOfInvestingPeriod());
		questionDtos.add(getUserProfileQuestionOfCurrentAge());
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}

	private UserProfileQuestionDto getUserProfileQuestionOfSavingsFor() {
		UserProfileQuestionDto savingForQuestion = new UserProfileQuestionDto();
		savingForQuestion.setQuestion(propertiesHelper.savingReasonQuestion);
		savingForQuestion.setQuestionId(1);

		List<UserProfileAnswerDto> answers = new ArrayList<>();
		answers.add(new UserProfileAnswerDto("RETIREMENT", propertiesHelper.savingReasonAnswer1));
		answers.add(new UserProfileAnswerDto("HOUSE", propertiesHelper.savingReasonAnswer2));
		answers.add(new UserProfileAnswerDto("EDUCATION", propertiesHelper.savingReasonAnswer3));
		answers.add(new UserProfileAnswerDto("WEDDING", propertiesHelper.savingReasonAnswer4));
		answers.add(new UserProfileAnswerDto("GENRAL_INVEST", propertiesHelper.savingReasonAnswer5));
		savingForQuestion.setAnswers(answers);
		return savingForQuestion;
	}

	private UserProfileQuestionDto getUserProfileQuestionOfAnnualIncome() {
		UserProfileQuestionDto annualIncomeQuestion = new UserProfileQuestionDto();
		annualIncomeQuestion.setQuestion(propertiesHelper.annualIncomeQuestion);
		annualIncomeQuestion.setQuestionId(2);

		List<UserProfileAnswerDto> answers = new ArrayList<>();
		answers.add(new UserProfileAnswerDto("ABV1LAKH", propertiesHelper.annualIncomeAnswer1));
		answers.add(new UserProfileAnswerDto("BTW50K_TO_99K", propertiesHelper.annualIncomeAnswer2));
		answers.add(new UserProfileAnswerDto("BTW30K_TO_49K", propertiesHelper.annualIncomeAnswer3));
		answers.add(new UserProfileAnswerDto("BLOW30K", propertiesHelper.annualIncomeAnswer4));
		annualIncomeQuestion.setAnswers(answers);
		return annualIncomeQuestion;
	}

	private UserProfileQuestionDto getUserProfileQuestionOfInvestingPeriod() {
		UserProfileQuestionDto investingPeriodQuestion = new UserProfileQuestionDto();
		investingPeriodQuestion.setQuestion(propertiesHelper.investingPeriodQuestion);
		investingPeriodQuestion.setQuestionId(3);

		List<UserProfileAnswerDto> answers = new ArrayList<>();
		answers.add(new UserProfileAnswerDto("LT3YEARS", propertiesHelper.investingPeriodAnswer1));
		answers.add(new UserProfileAnswerDto("BTW3TO5", propertiesHelper.investingPeriodAnswer2));
		answers.add(new UserProfileAnswerDto("BTW5TO10", propertiesHelper.investingPeriodAnswer3));
		answers.add(new UserProfileAnswerDto("10PLUS", propertiesHelper.investingPeriodAnswer4));
		investingPeriodQuestion.setAnswers(answers);
		return investingPeriodQuestion;
	}

	private UserProfileQuestionDto getUserProfileQuestionOfCurrentAge() {
		UserProfileQuestionDto currentAgeQuestion = new UserProfileQuestionDto();
		currentAgeQuestion.setQuestion(propertiesHelper.currentAgeQuestion);
		currentAgeQuestion.setQuestionId(4);

		List<UserProfileAnswerDto> answers = new ArrayList<>();
		answers.add(new UserProfileAnswerDto("18TO30", propertiesHelper.currentAgeAnswer1));
		answers.add(new UserProfileAnswerDto("31TO40", propertiesHelper.currentAgeAnswer2));
		answers.add(new UserProfileAnswerDto("41TO47", propertiesHelper.currentAgeAnswer3));
		answers.add(new UserProfileAnswerDto("48TO55", propertiesHelper.currentAgeAnswer4));
		answers.add(new UserProfileAnswerDto("55PLUS", propertiesHelper.currentAgeAnswer5));
		currentAgeQuestion.setAnswers(answers);
		return currentAgeQuestion;
	}

	@ExceptionHandler(OTPNotMatchingException.class)
	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	public Object handleOTPNotMatchingException(OTPNotMatchingException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
	}

	public User getUser(String userId) {
		User user = userService.retrieve(userId);
		if (user == null) {
			throw new JsonObjectNotFoundException("User id: " + userId + " not found.");
		}
		return user;
	}

	public void saveFile(MultipartFile multipartFile, String fileName) {
		String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
		File fileToCreate = new File(propertiesHelper.appDeploy + File.separator + fileName + "." + extension);
		try {
			fileToCreate.createNewFile();
			multipartFile.transferTo(fileToCreate);
		} catch (IllegalStateException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	@RequestMapping(value = "/device", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DeviceInfoDto> addDeviceInfo(@RequestBody DeviceInfoDto deviceInfoDto) {
		UserSessionDto userSessionDto = userSessionService.get();
		DeviceInfo deviceInfo =  deviceInfoService.retrieveByDeviceIdentifier(deviceInfoDto.getDeviceIdentifier());
		if (deviceInfo == null) {
			deviceInfo = new DeviceInfo();
			deviceInfo.setDeviceIdentifier(deviceInfoDto.getDeviceIdentifier());
			deviceInfo.setUser(userSessionDto.getUser());
			deviceInfoService.save(deviceInfo);
		}

		return new ResponseEntity<>(deviceInfoDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/device/testPush", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> testPushNotification(HttpServletRequest request)  {
		String deviceIdentifier = request.getParameter("deviceIdentifier");
		ApnsService service = APNS.newService()
            .withCert(this.getClass().getClassLoader().getResource(propertiesHelper.iOSPushNotificationCertification).getFile(),
					propertiesHelper.iOSPushNotificationPassword)
            .withSandboxDestination()
            .build();

        String payload = APNS.newPayload().alertBody("Testing from server").build();
        try {
			service.push(deviceIdentifier, payload);
		} catch(Exception ex) {
        	logger.trace(ex);
		}

		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}

class DeviceInfoDto {
    public DeviceInfoDto() {}
    private String deviceIdentifier;
    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }
}
