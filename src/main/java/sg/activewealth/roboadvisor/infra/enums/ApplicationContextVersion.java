package sg.activewealth.roboadvisor.infra.enums;

import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

public enum ApplicationContextVersion {
	V1_0(1.0),
	V1_1(1.1),
	V1_2(1.2);
	
	private Double value;
	
	private ApplicationContextVersion(Double value) {
		this.value = value;
	}
	
	public Double getValue() { return value; };
	
	public static ApplicationContextVersion get(String version) {
		if (ValidationUtils.getInstance().isEmptyString(version)) return V1_0;
		else if (version.startsWith("1.0")) return V1_0;
		else if (version.startsWith("1.1")) return V1_1;
		else if (version.startsWith("1.2")) return V1_2;
		return null; //will never get here!
	}
}
