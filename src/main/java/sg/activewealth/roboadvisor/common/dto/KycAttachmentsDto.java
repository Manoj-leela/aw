package sg.activewealth.roboadvisor.common.dto;

import sg.activewealth.roboadvisor.infra.dto.AbstractDto;

public class KycAttachmentsDto extends AbstractDto {

public static final long UPLOAD_MAX_FILESIZE = 5048576; //5mb
	
	public static final Integer MAX_WIDTH_PX = 800;
	
	private String kycDoc1;
	
	private String kycDoc2;
	
	private String kycDoc3;
	
	private String kycContentType1;
	
	private String kycContentType2;
	
	private String kycContentType3;

	/**
	 * @return the kycDoc1
	 */
	public String getKycDoc1() {
		return kycDoc1;
	}

	/**
	 * @param kycDoc1 the kycDoc1 to set
	 */
	public void setKycDoc1(String kycDoc1) {
		this.kycDoc1 = kycDoc1;
	}

	/**
	 * @return the kycDoc2
	 */
	public String getKycDoc2() {
		return kycDoc2;
	}

	/**
	 * @param kycDoc2 the kycDoc2 to set
	 */
	public void setKycDoc2(String kycDoc2) {
		this.kycDoc2 = kycDoc2;
	}

	/**
	 * @return the kycDoc3
	 */
	public String getKycDoc3() {
		return kycDoc3;
	}

	/**
	 * @param kycDoc3 the kycDoc3 to set
	 */
	public void setKycDoc3(String kycDoc3) {
		this.kycDoc3 = kycDoc3;
	}

	/**
	 * @return the kycContentType1
	 */
	public String getKycContentType1() {
		return kycContentType1;
	}

	/**
	 * @param kycContentType1 the kycContentType1 to set
	 */
	public void setKycContentType1(String kycContentType1) {
		this.kycContentType1 = kycContentType1;
	}

	/**
	 * @return the kycContentType2
	 */
	public String getKycContentType2() {
		return kycContentType2;
	}

	/**
	 * @param kycContentType2 the kycContentType2 to set
	 */
	public void setKycContentType2(String kycContentType2) {
		this.kycContentType2 = kycContentType2;
	}

	/**
	 * @return the kycContentType3
	 */
	public String getKycContentType3() {
		return kycContentType3;
	}

	/**
	 * @param kycContentType3 the kycContentType3 to set
	 */
	public void setKycContentType3(String kycContentType3) {
		this.kycContentType3 = kycContentType3;
	}

	
	
}
