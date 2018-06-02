package sg.activewealth.roboadvisor.infra.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import freemarker.log.Logger;

public enum ApplicationContext implements ByteEnum {

    WebRoot(1);

    private byte val;

    private ApplicationContext(int val) {
        this.val = (byte) val;
    }

    @Override
    public byte getValue() {
        return val;
    }

    @JsonCreator
    public static ApplicationContext fromValue(String val) {
        if (val != "") {
            return ApplicationContext.valueOf(val);
        }
        return null;
    }

}
