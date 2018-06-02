package sg.activewealth.roboadvisor.portfolio.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserRiskProfile implements ByteEnum {
	Aggressive(0, "Aggressive"), Conservative(1, "Conservative"), Balanced(3, "Balanced");

	private byte value;

	private String key;

	private UserRiskProfile(int value, String key) {
		this.key = key;
		this.value = (byte) value;
	}

	public static UserRiskProfile getByString(String currentUserRiskProfileValue) {
		UserRiskProfile userRiskProfile = null;
		for (UserRiskProfile currentUserRiskProfile : sg.activewealth.roboadvisor.portfolio.enums.UserRiskProfile.values()) {
			if (currentUserRiskProfile.key.equals(currentUserRiskProfileValue)) {
				userRiskProfile = currentUserRiskProfile;
				break;
			}
		}
		return userRiskProfile;
	}
	
	@JsonCreator
	public static UserRiskProfile get(Integer getKey) {
		UserRiskProfile userRiskProfile = null;
		for (UserRiskProfile curentUserRiskProfile : sg.activewealth.roboadvisor.portfolio.enums.UserRiskProfile.values()) {
			if (curentUserRiskProfile.value == getKey) {
				userRiskProfile = curentUserRiskProfile;
				break;
			}
		}
		return userRiskProfile;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
