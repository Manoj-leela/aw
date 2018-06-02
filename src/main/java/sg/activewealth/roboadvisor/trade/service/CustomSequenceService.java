package sg.activewealth.roboadvisor.trade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.trade.dao.CustomSeqNumberDao;
import sg.activewealth.roboadvisor.trade.model.CustomSeqNumber;


@Service
public class CustomSequenceService extends AbstractService<CustomSeqNumber>{

	public CustomSequenceService() {
		super(CustomSeqNumber.class);
	}

    @Autowired
    public void setDao(CustomSeqNumberDao dao) {
        super.dao = dao;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int retrieveNextSeqByDomain(String domain) {
        CustomSeqNumber customSeqNumber = ((CustomSeqNumberDao) dao).retrieveNextSeqByDomain(domain);
        save(customSeqNumber);
        return customSeqNumber.getCurrent();
    }
}
