package sg.activewealth.roboadvisor.common.dto;

import sg.activewealth.roboadvisor.common.enums.AgentOTPStatus;
import sg.activewealth.roboadvisor.common.enums.BankDetailsStatus;
import sg.activewealth.roboadvisor.common.enums.DeclarationStatus;
import sg.activewealth.roboadvisor.common.enums.KycStatus;
import sg.activewealth.roboadvisor.infra.dto.AbstractDto;

public class UserStatusDto extends AbstractDto {

	private String status = "Pendding unique code";

	private AgentOTPStatus agentOtp = AgentOTPStatus.NotYetSent;

	private KycStatus kyc = KycStatus.NotSubmitted;

	private BankDetailsStatus bankDetails = BankDetailsStatus.NotSubmitted;

	private DeclarationStatus declaration = DeclarationStatus.NotAccepted;

	public UserStatusDto() {
		super();
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the agentOtp
	 */
	public AgentOTPStatus getAgentOtp() {
		return agentOtp;
	}

	/**
	 * @param agentOtp
	 *            the agentOtpStatus to set
	 */
	public void setAgentOtp(AgentOTPStatus agentOtp) {
		if (AgentOTPStatus.Completed.equals(agentOtp)) {
			this.status = "Pending KYC, bank details and declarations";
		}
		this.agentOtp = agentOtp;
	}

	/**
	 * @return the kyc
	 */
	public KycStatus getKyc() {
		return kyc;
	}

	/**
	 * @param kyc
	 *            the kycStatus to set
	 */
	public void setKyc(KycStatus kyc) {
		if (KycStatus.Completed.equals(kyc) && !BankDetailsStatus.Completed.equals(this.bankDetails)) {
			this.status = "Pending bank details and Declarations";
		} 
		this.kyc = kyc;
	}

	/**
	 * @return the bankDetailsStatus
	 */
	public BankDetailsStatus getBankDetails() {
		return bankDetails;
	}

	/**
	 * @param bankDetails
	 *            the bankDetailsStatus to set
	 */
	public void setBankDetails(BankDetailsStatus bankDetails) {
		if (BankDetailsStatus.Completed.equals(bankDetails)) {
			this.status = "Pending fund transfer";
		}
		this.bankDetails = bankDetails;
	}

	/**
	 * @return the declarationStatus
	 */
	public DeclarationStatus getDeclaration() {
		return declaration;
	}

	/**
	 * @param declaration
	 *            the declarationStatus to set
	 */
	public void setDeclaration(DeclarationStatus declaration) {
		this.declaration = declaration;
	}

}
