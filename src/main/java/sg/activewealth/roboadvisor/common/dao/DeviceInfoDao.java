package sg.activewealth.roboadvisor.common.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.common.model.DeviceInfo;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;

import java.util.List;

@Repository
public class DeviceInfoDao extends AbstractDao<DeviceInfo>{
    public DeviceInfo retrieveByDeviceIdentifier(String identifier) {
        DetachedCriteria dc = DetachedCriteria.forClass(DeviceInfo.class);
        dc.add(Restrictions.eq("deviceIdentifier", identifier));
        List<DeviceInfo> result = findByCriteria (dc, false);
        if (result != null && result.size() > 0) return result.get(0);
        else return null;
    }

    public List<DeviceInfo> retrieveDevicesByUser(User user) {
        DetachedCriteria dc = DetachedCriteria.forClass(DeviceInfo.class);
        dc.add(Restrictions.eq("user", user));
        return findByCriteria (dc, false);
    }
}
