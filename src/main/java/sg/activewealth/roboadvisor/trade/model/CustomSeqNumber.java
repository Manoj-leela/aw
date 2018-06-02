package sg.activewealth.roboadvisor.trade.model;

import javax.persistence.Entity;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity(name = "custom_seq_number")
public class CustomSeqNumber extends AbstractModel {

    private Integer current;
    private String domain;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CustomSeqNumber [");
        if (current != null) {
            builder.append("current=");
            builder.append(current);
            builder.append(", ");
        }
        if (domain != null) {
            builder.append("domain=");
            builder.append(domain);
        }
        builder.append("]");
        return builder.toString();
    }
}
