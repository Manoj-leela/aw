package sg.activewealth.roboadvisor.infra.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MobilePlatform implements ByteEnum {
	
	Android(1),
	iOS(3);

	private byte val;
	
	private MobilePlatform(int val) {
		this.val = (byte) val;
	}

	@Override
	public byte getValue() {
		return val;
	}
	
	@JsonCreator
	public static MobilePlatform fromValue(String val) {
		if (val != "")
			return MobilePlatform.valueOf(val);
		return null;
	}
	
}
