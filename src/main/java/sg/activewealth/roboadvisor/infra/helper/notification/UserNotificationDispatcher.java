package sg.activewealth.roboadvisor.infra.helper.notification;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus;
import sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus;
import sg.activewealth.roboadvisor.banking.enums.RedemptionStatus;
import sg.activewealth.roboadvisor.banking.model.Redemption;
import sg.activewealth.roboadvisor.banking.model.Remittance;
import sg.activewealth.roboadvisor.banking.service.RedemptionService;
import sg.activewealth.roboadvisor.banking.service.RemittanceService;
import sg.activewealth.roboadvisor.common.enums.BankDetailsStatus;
import sg.activewealth.roboadvisor.common.enums.KycStatus;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.helper.MailSenderHelper;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Component
public class UserNotificationDispatcher {

	private Logger logger = Logger.getLogger(UserNotificationDispatcher.class);

	@Autowired
	private MailSenderHelper mailSenderHelper;

	@Autowired
	private NotificationMacroResolver notificationMacroResolver;

	@Autowired
	private UserService userService;

	@Autowired
	private RemittanceService remittanceService;

	@Autowired
	private RedemptionService redemptionService;

	public void notifyChange(final User updatedModel) {

		final Map<String, Object> dataMap = new HashMap<>(1);
		dataMap.put("user", updatedModel);
		try {
			if (BooleanUtils.isTrue(updatedModel.getCreatingNewObject())) {
				// Sign up

				final NotificationMacroResolver.NotificationMetaData placeHolders = notificationMacroResolver
						.from(NotificationType.SIGN_UP, dataMap, updatedModel.getEmail());
				mailSenderHelper.sendMailToUser(placeHolders.getSubject(), placeHolders.getRecipients(),
						placeHolders.getBody(), true, null, false);
				updatedModel.setSignupEmailSent(LocalDateTime.now());
				return;// Return since further flow fetches existing user
			}

			if (updatedModel.getKycUploadEmailSent() == null) {

				if (KycStatus.AwaitingApproval != getExistingUserById(updatedModel.getId()).getKycStatus()
						&& KycStatus.AwaitingApproval == updatedModel.getKycStatus()) {
					// New KYC Upload

					final NotificationMacroResolver.NotificationMetaData placeHolders = notificationMacroResolver
							.from(NotificationType.NEW_KYC_UPLOAD, dataMap, updatedModel.getEmail());
					mailSenderHelper.sendMailToUser(placeHolders.getSubject(), placeHolders.getRecipients(),
							placeHolders.getBody(), true, null, false);
					updatedModel.setKycUploadEmailSent(LocalDateTime.now());
				}

			}

			if (updatedModel.getBankDetailsDeclarationsEmailSent() == null) {

				if (BankDetailsStatus.AwaitingApproval != getExistingUserById(updatedModel.getId())
						.getBankDetailsStatus()
						&& BankDetailsStatus.AwaitingApproval == updatedModel.getBankDetailsStatus()) {
					// New Bank Details and Declaration

					final NotificationMacroResolver.NotificationMetaData placeHolders = notificationMacroResolver
							.from(NotificationType.BANK_DECLARATION, dataMap, updatedModel.getEmail());
					mailSenderHelper.sendMailToUser(placeHolders.getSubject(), placeHolders.getRecipients(),
							placeHolders.getBody(), true, null, false);
					updatedModel.setBankDetailsDeclarationsEmailSent(LocalDateTime.now());
				}

			}

			if (KycStatus.SubmissionIssues != getExistingUserById(updatedModel.getId()).getKycStatus()
					&& KycStatus.SubmissionIssues == updatedModel.getKycStatus()) {
				// Kyc Issues

				final NotificationMacroResolver.NotificationMetaData placeHolders = notificationMacroResolver
						.from(NotificationType.KYC_ISSUE, dataMap, updatedModel.getEmail());
				mailSenderHelper.sendMailToUser(placeHolders.getSubject(), placeHolders.getRecipients(),
						placeHolders.getBody(), true, null, false);
				updatedModel.setKycIssueEmailSent(LocalDateTime.now());

			}

			// Bank Detail Submission Issues
			if (BankDetailsStatus.SubmissionIssues == updatedModel.getBankDetailsStatus()
					&& !ValidationUtils.getInstance().isEmptyString(updatedModel.getBankDetailsRemarks())) {
				final NotificationMacroResolver.NotificationMetaData placeHolders = notificationMacroResolver
						.from(NotificationType.BANK_DETAIL_SUBMISSION_ISSUES, dataMap, updatedModel.getEmail());
				mailSenderHelper.sendMailToUser(placeHolders.getSubject(), placeHolders.getRecipients(),
						updatedModel.getBankDetailsRemarks(), true, null, false);
			}

			if (updatedModel.getKycCompletedEmailSent() == null) {

				if (KycStatus.Completed != getExistingUserById(updatedModel.getId()).getKycStatus()
						&& KycStatus.Completed == updatedModel.getKycStatus()) {
					// KYC Completed

					final NotificationMacroResolver.NotificationMetaData placeHolders = notificationMacroResolver
							.from(NotificationType.KYC_COMPLETED, dataMap, updatedModel.getEmail());
					mailSenderHelper.sendMailToUser(placeHolders.getSubject(), placeHolders.getRecipients(),
							placeHolders.getBody(), true, null, false);
					updatedModel.setKycCompletedEmailSent(LocalDateTime.now());
				}

			}

			if (updatedModel.getBankDetailsDeclarationsStatusCompletedEmailSent() == null) {

				if (BankDetailsStatus.Completed != getExistingUserById(updatedModel.getId()).getBankDetailsStatus()
						&& BankDetailsStatus.Completed == updatedModel.getBankDetailsStatus()) {
					// Bank Details completed

					final NotificationMacroResolver.NotificationMetaData placeHolders = notificationMacroResolver
							.from(NotificationType.BANK_STATUS_COMPLETED, dataMap, updatedModel.getEmail());
					mailSenderHelper.sendMailToUser(placeHolders.getSubject(), placeHolders.getRecipients(),
							placeHolders.getBody(), true, null, false);
					updatedModel.setBankDetailsDeclarationsStatusCompletedEmailSent(LocalDateTime.now());
				}

			}

		} catch (IOException e) {
			// Don't rethrow
			logger.error(e.getMessage(), e);
		}

	}

	private User getExistingUserById(final String id) {
		return userService.getUserAndEvictIt(id);
	}

	private Remittance getExistingRemittanceById(final String id) {
		return remittanceService.retrieveAndEvictIt(id);
	}

	private Redemption getExistingRedemptionById(final String id) {
		return redemptionService.retrieveAndEvictIt(id);
	}

	public void notifyRedemptionChange(final Redemption redemption) {

		final Map<String, Object> dataMap = new HashMap<>(1);
		dataMap.put("redemption", redemption);

		try {

			if (BooleanUtils.isTrue(redemption.getCreatingNewObject())) {
				return;
			}

			if (redemption.getRedemptionCompletedEmailSent() == null
					&& RedemptionStatus.Completed != getExistingRedemptionById(redemption.getId()).getRedemptionStatus()
					&& RedemptionStatus.Completed == redemption.getRedemptionStatus()) {
				final NotificationMacroResolver.NotificationMetaData placeHolders = notificationMacroResolver.from(
						NotificationType.REDEMPTION_COMPLETED, dataMap,
						redemption.getUserPortfolio().getUser().getEmail());
				mailSenderHelper.sendMailToUser(placeHolders.getSubject(), placeHolders.getRecipients(),
						placeHolders.getBody(), true, null, false);
				redemption.setRedemptionCompletedEmailSent(LocalDate.now());
			}

		} catch (Exception ex) {
			// Don't rethrow
			logger.error(ex.getMessage(), ex);
		}

	}

	public void notifyRemittanceChange(final Remittance remittance) {

		final Map<String, Object> dataMap = new HashMap<>(1);
		dataMap.put("remittance", remittance);

		try {
			if (BooleanUtils.isTrue(remittance.getCreatingNewObject())) {

				final NotificationMacroResolver.NotificationMetaData placeHolders = notificationMacroResolver
						.from(NotificationType.REMITTANCE_RECONCILIATION, dataMap, null);
				mailSenderHelper.sendMailToUser(placeHolders.getSubject(), placeHolders.getRecipients(),
						placeHolders.getBody(), true, null, false);
				remittance.setReconciliationEmailSent(LocalDateTime.now());
				return;// Return since further flow fetches existing model
			}

			if (remittance.getReconciliationAwaitEmailSent() == null) {
				if (InvestorRemittanceStatus.Received != getExistingRemittanceById(remittance.getId()).getInvestorRemittanceStatus()
						&& InvestorRemittanceStatus.Received == remittance.getInvestorRemittanceStatus()) {
					// Awaiting reconciliation

					final NotificationMacroResolver.NotificationMetaData placeHolders = notificationMacroResolver.from(
							NotificationType.REMITTANCE_AWAITING_RECONCILIATION, dataMap,
							remittance.getUserPortfolio().getUser().getEmail());
					mailSenderHelper.sendMailToUser(placeHolders.getSubject(), placeHolders.getRecipients(),
							placeHolders.getBody(), true, null, false);
					remittance.setReconciliationAwaitEmailSent(LocalDateTime.now());
				}
			}

			// Investor Remittance issues
			if (remittance.getInvestorRemittanceIssueEmailSent() == null
					&& InvestorRemittanceStatus.Issues == remittance.getInvestorRemittanceStatus()
					&& !ValidationUtils.getInstance().isEmptyString(remittance.getRemarks())) {
				final NotificationMacroResolver.NotificationMetaData placeHolders = notificationMacroResolver.from(
						NotificationType.REMITTANCE_ISSUES, dataMap,
						remittance.getUserPortfolio().getUser().getEmail());
				mailSenderHelper.sendMailToUser(placeHolders.getSubject(), placeHolders.getRecipients(),
						remittance.getRemarks(), true, null, false);
				remittance.setInvestorRemittanceIssueEmailSent(LocalDateTime.now());
			}

			// Broker Funding completed
			if (remittance.getBrokerFundedEmailSent() == null) {
				if (BrokerFundingStatus.Completed != getExistingRemittanceById(remittance.getId()).getBrokerFundingStatus()
						&& BrokerFundingStatus.Completed == remittance.getBrokerFundingStatus()) {

					final NotificationMacroResolver.NotificationMetaData placeHolders = notificationMacroResolver.from(
							NotificationType.REMITTANCE_FUNDED, dataMap,
							remittance.getUserPortfolio().getUser().getEmail());
					mailSenderHelper.sendMailToUser(placeHolders.getSubject(), placeHolders.getRecipients(),
							placeHolders.getBody(), true, null, false);
					remittance.setBrokerFundedEmailSent(LocalDateTime.now());
				}
			}
		} catch (IOException e) {
			// Don't rethrow
			logger.error(e.getMessage(), e);
		}

	}

}