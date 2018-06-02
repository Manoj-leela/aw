package sg.activewealth.roboadvisor.infra.helper.notification;

public enum NotificationType {

  SIGN_UP("user_signup_subject.html", "user_signup_body.html", true),
  NEW_KYC_UPLOAD("upload_kyc_subject.html", "upload_kyc_body.html", true),
  BANK_DECLARATION("bank_declaration_subject.html", "bank_declaration_body.html", true),
  BANK_STATUS_COMPLETED("bank_status_completed_subject.html", "bank_status_completed_body.html", false),
  REMITTANCE_RECONCILIATION("remittance_reconciliation_subject.html", "remittance_reconciliation_body.html", true),
  REMITTANCE_AWAITING_RECONCILIATION("remittance_await_reconciliation_subject.html", "remittance_await_reconciliation_body.html", false),
  REMITTANCE_ISSUES("remittance_issues_subject.html", null, false),
  REMITTANCE_FUNDED("remittance_funded_subject.html", "remittance_funded_body.html", false),
  KYC_ISSUE("kyc_issue_subject.html", "kyc_issue_body.html", false),
  KYC_COMPLETED("kyc_completed_subject.html", "kyc_completed_body.html", false),
  BANK_DETAIL_SUBMISSION_ISSUES("bank_detail_issue_subject.html",null, false ),
  REDEMPTION_COMPLETED("redemption_completed_subject.html", "redemption_completed_body.html", false);	
	
  private String subjectTemplate;
  private String bodyTemplate;
  private boolean sendToAdmin;

  NotificationType(final String subjectTemplate, final String bodyTemplate, final boolean sendToAdmin) {

    this.subjectTemplate = subjectTemplate;
    this.bodyTemplate = bodyTemplate;
    this.sendToAdmin = sendToAdmin;
  }

  public String getSubjectTemplate() {
    return subjectTemplate;
  }

  public String getBodyTemplate() {
    return bodyTemplate;
  }

  public boolean isSendToAdmin() {
    return sendToAdmin;
  }
}
