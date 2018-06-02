package sg.activewealth.roboadvisor.portfolio.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PortfolioStatus implements ByteEnum {
	
	Active(0,"Active"),
	Draft(1,"Draft");
	
    private byte value;
    private String label;

    private PortfolioStatus(int value,String label) {
        this.value = (byte) value;
        this.label = label;
    }
    
    @JsonCreator
    public static PortfolioStatus get(Integer getKey) {
    	PortfolioStatus UserPortfolioStatus2 = null;
		for (PortfolioStatus userProfileStatus : PortfolioStatus.values()) {
			if (userProfileStatus.value == getKey) {
				UserPortfolioStatus2 = userProfileStatus;
				break;
			}
		}
		return UserPortfolioStatus2;
	}
    
    public static PortfolioStatus get(String statusValue) {
    	PortfolioStatus status = null;
		for (PortfolioStatus statusValues : PortfolioStatus.values()) {
			if (statusValues.label.equals(statusValue)) {
				status = statusValues;
				break;
			}
		}
		return status;
	}

    /**
	 * @param value the value to set
	 */
	public void setValue(byte value) {
		this.value = value;
	}
	
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
    public byte getValue() {
        return this.value;
    }
    public String getLabel() {
    	return label;
    }
}
