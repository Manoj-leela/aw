package sg.activewealth.roboadvisor.portfolio.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Goal implements ByteEnum{
	Retirement(0,"Retirement"),ChildrenEducation(1,"Children's Education"),DepositForHouse(2,"Deposit for House"),Car(3,"Car"),
	GeneralInvesting(4,"General Investing");

	private byte value;
	
	private String label;
	
	private Goal(int value,String label){
		this.label = label;
		this.value = (byte) value;
	}
	
	public static Goal get(String goalValue) {
		Goal goal = null;
		for (Goal goalValues : Goal.values()) {
			if (goalValues.label.equals(goalValue)) {
				goal = goalValues;
				break;
			}
		}
		return goal;
	}
	
	@JsonCreator
	public static Goal get(Integer value) {
		Goal goal = null;
		for (Goal goalValue : Goal.values()) {
			if (goalValue.value == value) {
				goal = goalValue;
				break;
			}
		}
		return goal;
	}
	
	@Override
	public byte getValue() {
		return this.value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setValue(byte value) {
		this.value = value;
	}

}
