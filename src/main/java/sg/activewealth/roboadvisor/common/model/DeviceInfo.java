package sg.activewealth.roboadvisor.common.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity
@Table(name = "device_info")
public class DeviceInfo extends AbstractModel {

    private String deviceIdentifier;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
