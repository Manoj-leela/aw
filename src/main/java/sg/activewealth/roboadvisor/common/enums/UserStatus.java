package sg.activewealth.roboadvisor.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserStatus implements ByteEnum {
	Active(0,"Active"), InActive(1,"InActive"), Delete(2,"Delete");

    private byte value;
    private String label;

    private UserStatus(int value,String label) {
        this.value = (byte) value;
        this.label = label;
    }

    @Override
    @JsonCreator
    public byte getValue() {
        return this.value;
    }
    public String getLabel() {
    	return label;
    }
}

