package sg.activewealth.roboadvisor.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AccountStatus implements ByteEnum {
	UnApproved(0,"UnApproved"),Approved(1,"Approved"),Rejected(2,"Rejected"),Locked(3,"Locked");

	private byte value;
	private String label;

	AccountStatus(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}
	
	public static AccountStatus get(String currentAccountStatusValue) {
		AccountStatus accountStatus = null;
		for (AccountStatus currentAccountStatus : AccountStatus.values()) {
			if (currentAccountStatus.label.equals(currentAccountStatusValue)) {
				accountStatus = currentAccountStatus;
				break;
			}
		}
		return accountStatus;
	}
	
	@JsonCreator
	public static AccountStatus get(Integer value) {
		AccountStatus accountStatus = null;
		for (AccountStatus accountStatus2 : AccountStatus.values()) {
			if (accountStatus2.value == value) {
				accountStatus = accountStatus2;
				break;
			}
		}
		return accountStatus;
	}
	@Override
	public byte getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
