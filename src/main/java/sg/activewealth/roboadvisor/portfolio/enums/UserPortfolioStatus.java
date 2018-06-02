package sg.activewealth.roboadvisor.portfolio.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserPortfolioStatus implements ByteEnum {
	
	NotAssigned(0,"Not Assigned"),
	Assigned(1,"Assigned"),
	Funded(2,"Funded"),
	NotExecuted(3,"Not Executed"), 
	PartiallyExecuted(4,"Partially Executed"),
	Executed(5,"Executed"),
	CloseRequested(6,"Close Requested"),
	ReadyForClose(7,"Ready For Close"),
	PartiallyClosed(8,"Partially Closed"),
	Closed(9,"Closed"),
	AssignedForLater(10,"Assigned For Later"),
	AssignedForReplace(11,"Assigned For Replace"),
	ReadyForRebalance(12, "Ready for Rebalance"),
	ClosedAndReleased(13,"Closed and Released");

    private byte value;
    private String label;

    private UserPortfolioStatus(int value,String label) {
        this.value = (byte) value;
        this.label = label;
    }
    
    @JsonCreator
    public static UserPortfolioStatus get(Integer getKey) {
        UserPortfolioStatus UserPortfolioStatus2 = null;
        for (UserPortfolioStatus userProfileStatus : UserPortfolioStatus.values()) {
            if (userProfileStatus.value == getKey) {
                UserPortfolioStatus2 = userProfileStatus;
                break;
            }
        }
        return UserPortfolioStatus2;
    }

    public static UserPortfolioStatus get(String statusValue) {
        UserPortfolioStatus status = null;
        for (UserPortfolioStatus statusValues : UserPortfolioStatus.values()) {
            if (statusValues.label.equalsIgnoreCase(statusValue)) {
                status = statusValues;
                break;
            }
        }
        return status;
    }

    @JsonCreator
    public static UserPortfolioStatus fromString(String statusValue) {
        UserPortfolioStatus status = null;
        for (UserPortfolioStatus statusValues : UserPortfolioStatus.values()) {
            if (statusValues.name().equalsIgnoreCase(statusValue)) {
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
