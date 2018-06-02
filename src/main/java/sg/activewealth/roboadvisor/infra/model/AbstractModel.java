package sg.activewealth.roboadvisor.infra.model;

import java.time.LocalDateTime;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public abstract class AbstractModel {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Version
    @JsonIgnore
    // Added JsonIgnore since it should not be part of JSON response for REST API
    private Integer version;

    @JsonIgnore
    private String createdBy;

    @JsonIgnore
    private String updatedBy;

    @Column(nullable = false)
    @JsonIgnore
    private LocalDateTime createdOn;

    @JsonIgnore
    private LocalDateTime updatedOn;

    @Transient
    @JsonIgnore
    private Boolean creatingNewObject = false; // this is used for preSave &
                                               // postSave method
                                               // communications!

    public AbstractModel(String id) {
        this.id = id;
    }

    public AbstractModel() {
    }
    // helper methods

    @Override
    public boolean equals(Object obj) {
        if (getId() != null)
            return getId().equals(((AbstractModel) obj).getId());
        return false;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id != null && id.length() > 0)
            this.id = id;
        else
            this.id = null;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean getCreatingNewObject() {
        return creatingNewObject;
    }

    public void setCreatingNewObject(Boolean creatingNewObject) {
        this.creatingNewObject = creatingNewObject;
    }

    public static Comparator<AbstractModel> COMPARE_BY_CREATION_DATE = new Comparator<AbstractModel>() {
        public int compare(AbstractModel one, AbstractModel other) {
            return one.getCreatedOn().compareTo(other.getCreatedOn());
        }
    };

}