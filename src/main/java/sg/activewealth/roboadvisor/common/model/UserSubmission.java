package sg.activewealth.roboadvisor.common.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import sg.activewealth.roboadvisor.common.enums.UserSubmissionType;
import sg.activewealth.roboadvisor.infra.enums.EnumByteUserType;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity
@Table(name = "user_submission")
@TypeDefs({ @TypeDef(name = "type", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.UserSubmissionType") }), })
public class UserSubmission extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String status;
    private UserSubmissionType type;
    private String description;

    public UserSubmission() {
    }

    public UserSubmission(String id) {
        super(id);
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public UserSubmissionType getType() {
        return type;
    }

    public void setType(final UserSubmissionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
