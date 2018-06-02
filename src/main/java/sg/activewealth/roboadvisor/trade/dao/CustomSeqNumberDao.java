package sg.activewealth.roboadvisor.trade.dao;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.trade.model.CustomSeqNumber;

@Repository
public class CustomSeqNumberDao extends AbstractDao<CustomSeqNumber> {

	@Override
	protected void initProxies(CustomSeqNumber entity, boolean fullInit) {
		super.initProxies(entity, fullInit);
	}

	@SuppressWarnings("unchecked")
	public CustomSeqNumber retrieveNextSeqByDomain(String domain) {
		DetachedCriteria c = DetachedCriteria.forClass(CustomSeqNumber.class).add(
				Restrictions.eq("domain", domain));
		
		List<CustomSeqNumber> customSeqNumbers = findByCriteria(c);
		int current;
		CustomSeqNumber model; 
		//TODO Handle the Order Number and InstrumentRateReqId limit beyond the specified Numbers
		if(customSeqNumbers == null || customSeqNumbers.size() == 0) {
			current =  1000;
			if (domain.equalsIgnoreCase("OrderId")) {
				current = 10000;
			} else if (domain.equalsIgnoreCase("InstrumentRateReqId")) {
				current = 1000000;
			} 
			model = new CustomSeqNumber();
			model.setDomain(domain);
		} else {
			model = customSeqNumbers.get(0);
			current = model.getCurrent();
			if(domain.equalsIgnoreCase("OrderId") && current == 99999){
	            current =  9999;
            } else if(domain.equalsIgnoreCase("InstrumentRateReqId") && current == 999999){
            	current = 10001;
            }
			current++;
		}
		model.setCurrent(current);
		return model;
	}
}
