package sg.activewealth.roboadvisor.dealing.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity
@Table(name = "external_fund")
public class ExternalFund extends AbstractModel {

    private String name;

    public ExternalFund() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ExternalFund [name=");
        builder.append(name);
        builder.append("]");
        return builder.toString();
    }

}
