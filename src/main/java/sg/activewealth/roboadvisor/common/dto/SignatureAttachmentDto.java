package sg.activewealth.roboadvisor.common.dto;

import sg.activewealth.roboadvisor.infra.dto.AbstractDto;

public class SignatureAttachmentDto extends AbstractDto {

public static final long UPLOAD_MAX_FILESIZE = 5048576; //5mb
	
	public static final Integer MAX_WIDTH_PX = 800;
	
	private String signatureFile;
	
	private String signatureContentType;

	/**
	 * @return the signatureFile
	 */
	public String getSignatureFile() {
		return signatureFile;
	}

	/**
	 * @param signatureFile the signatureFile to set
	 */
	public void setSignatureFile(String signatureFile) {
		this.signatureFile = signatureFile;
	}

	/**
	 * @return the signatureContentType
	 */
	public String getSignatureContentType() {
		return signatureContentType;
	}

	/**
	 * @param signatureContentType the signatureContentType to set
	 */
	public void setSignatureContentType(String signatureContentType) {
		this.signatureContentType = signatureContentType;
	}
	
}
