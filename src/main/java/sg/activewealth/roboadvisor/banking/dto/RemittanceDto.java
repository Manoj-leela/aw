package sg.activewealth.roboadvisor.banking.dto;

import sg.activewealth.roboadvisor.infra.dto.AbstractDto;

public class RemittanceDto extends AbstractDto {

	public static final long UPLOAD_MAX_FILESIZE = 5048576; //5mb
	
	private String referenceNo;
	
	private String remittanceFile;
	
	private String remittanceFileContentType;
	
	public RemittanceDto() {
		super();
	}
	
	/**
	 * @return the referenceNo
	 */
	public String getReferenceNo() {
		return referenceNo;
	}

	/**
	 * @param referenceNo the referenceNo to set
	 */
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	/**
	 * @return the remittanceFile
	 */
	public String getRemittanceFile() {
		return remittanceFile;
	}

	/**
	 * @param remittanceFile the remittanceFile to set
	 */
	public void setRemittanceFile(String remittanceFile) {
		this.remittanceFile = remittanceFile;
	}

	/**
	 * @return the remittanceFileContentType
	 */
	public String getRemittanceFileContentType() {
		return remittanceFileContentType;
	}

	/**
	 * @param remittanceFileContentType the remittanceFileContentType to set
	 */
	public void setRemittanceFileContentType(String remittanceFileContentType) {
		this.remittanceFileContentType = remittanceFileContentType;
	}
	
	
}
