package sg.activewealth.roboadvisor.trade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.trade.dao.IBReqTrackerDao;
import sg.activewealth.roboadvisor.trade.model.IBReqTracker;

@Service
public class IBReqTrackerService extends AbstractService<IBReqTracker> {

	public IBReqTrackerService() {
		super(IBReqTracker.class);
	}

	@Autowired
	public void setDao(IBReqTrackerDao dao) {
		super.dao = dao;
	}

	public IBReqTracker retrieveByReqId(int reqId) {
		return ((IBReqTrackerDao) dao).retrieveByReqId(reqId);
	}
}
