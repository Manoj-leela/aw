package sg.activewealth.roboadvisor.common.dao;

import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.common.model.BrokerTransaction;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;

@Repository
public class BrokerTransactionDao extends AbstractDao<BrokerTransaction> {

	@Override
	protected void initProxies(BrokerTransaction entity, boolean fullInit) {
		super.initProxies(entity, fullInit);
	}
	
}
