package sg.activewealth.roboadvisor.common.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity
@Table(name = "agent")
public class Agent extends AbstractModel {

    public Agent() {
    }

    public Agent(String id) {
        super(id);
    }

    private String name;
    private String agentCode;
    private String mobileNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

}
