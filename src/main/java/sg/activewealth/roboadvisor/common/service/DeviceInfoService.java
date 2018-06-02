package sg.activewealth.roboadvisor.common.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

import sg.activewealth.roboadvisor.common.dao.DeviceInfoDao;
import sg.activewealth.roboadvisor.common.model.DeviceInfo;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Service
public class DeviceInfoService extends AbstractService<DeviceInfo> {
    public DeviceInfoService() {
        super(DeviceInfo.class);
    }

    @Autowired
    public void setDao(DeviceInfoDao dao) {
        super.dao = dao;
    }

    @Autowired
    private UserService userService;

    public DeviceInfo retrieveByDeviceIdentifier(String identifier) {
        return ((DeviceInfoDao) dao).retrieveByDeviceIdentifier(identifier);
    }

    public List<DeviceInfo> sendNotificationToDevice(String userId, String message) {
        User user = userService.retrieve(userId, false);
        List<DeviceInfo> deviceInfoList = ((DeviceInfoDao) dao).retrieveDevicesByUser(user);

        ApnsService service = APNS.newService().withCert(this.getClass().getClassLoader().getResource(propertiesHelper.iOSPushNotificationCertification).getFile(), propertiesHelper.iOSPushNotificationPassword).withSandboxDestination().build();
        List<DeviceInfo> sent = new ArrayList<>();

        for (DeviceInfo deviceInfo : deviceInfoList) {
            String payload = APNS.newPayload().alertBody(message).build();

            if (!ValidationUtils.getInstance().isEmptyString(deviceInfo.getDeviceIdentifier()) && !ValidationUtils.getInstance().isEmptyString(payload)) {

                try {
                    service.push(deviceInfo.getDeviceIdentifier(), payload);
                    sent.add(deviceInfo);
                } catch (Exception ex) {
                    logger.trace(ex);
                }
            }
        }

        return sent;
    }
}
