package sg.activewealth.roboadvisor.common.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.common.model.Agent;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Repository
public class AgentDao extends AbstractDao<Agent> {

	public Agent retrieveByAgentCode(String agentCode) {
		List<Agent> res = findByCriteria(
				DetachedCriteria.forClass(Agent.class).add(Restrictions.eq("agentCode", agentCode)), false);
		if (res.size() > 0) {
			return res.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Agent retrieveByMobileNumber(String mobileNumber) {
		DetachedCriteria agentCriteria = DetachedCriteria.forClass(Agent.class)
				.add(Restrictions.eq("mobileNumber", mobileNumber));
		List<Agent> agents = findByCriteria(agentCriteria);
		return agents.size() > 0 ? agents.get(0) : null;
	}

	public PagingDto<Agent> retrieveForListPage(PagingDto<Agent> pagingDto, String name, String agentCode,
			String mobileNumber) {
		DetachedCriteria agentCriteria = DetachedCriteria.forClass(Agent.class);
		if(!ValidationUtils.getInstance().isEmptyString(name)){
			agentCriteria.add(Restrictions.like("name", name, MatchMode.START));
		}
		if(!ValidationUtils.getInstance().isEmptyString(agentCode)){
			agentCriteria.add(Restrictions.like("agentCode", agentCode, MatchMode.START));
		}
		if(!ValidationUtils.getInstance().isEmptyString(mobileNumber)){
			agentCriteria.add(Restrictions.like("mobileNumber", mobileNumber, MatchMode.START));
		}
		agentCriteria.addOrder(Order.desc("createdOn"));
		return findByCriteria(agentCriteria, pagingDto, false);
	}
}
