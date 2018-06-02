package sg.activewealth.roboadvisor.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.common.dao.AgentDao;
import sg.activewealth.roboadvisor.common.model.Agent;
import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Service
public class AgentService extends AbstractService<Agent> {

	public AgentService() {
		super(Agent.class);
	}
	
	@Autowired
	public void setDao(AgentDao dao) {
		super.dao = dao;
	}
	
	@Override
	public ErrorsDto validateForSave(Agent model, ErrorsDto errors) throws ValidateException {
		super.validateForSave(model, errors);
		if (ValidationUtils.getInstance().isEmptyString(model.getName())) {
			errors.add(new ErrorDto("name", "error.required", "Name"));
		} 
		if (ValidationUtils.getInstance().isEmptyString(model.getAgentCode())) {
			errors.add(new ErrorDto("agentCode", "error.required", "Agent Code"));
		}
		if (ValidationUtils.getInstance().isEmptyString(model.getMobileNumber())) {
			errors.add(new ErrorDto("mobileNumber", "error.required", "Phone Number"));
		}
		Agent dbAgent = retrieveByAgentCode(model.getAgentCode());
		if(model.getCreatingNewObject()) {
			if(dbAgent !=null) {
				errors.add(new ErrorDto("agentCode", "error.duplicate.value","Agent" , "agenCode"));
			}
		} else {
			if(dbAgent != null && !dbAgent.getId().equals(model.getId())) {
				errors.add(new ErrorDto("agentCode", "error.duplicate.value","Agent" , "agenCode"));
			}
		}
		Agent agent2 = retrieveByMobileNumber(model.getMobileNumber());
		if(model.getCreatingNewObject()) {
			if(agent2 !=null) {
				errors.add(new ErrorDto("mobileNumber", "error.duplicate.value","Agent" , "Phone Number"));
			}
		} else {
			if(agent2 != null && !agent2.getId().equals(model.getId())) {
				errors.add(new ErrorDto("mobileNumber", "error.duplicate.value","Agent" , "Phone Number"));
			}
		}
		return errors;		
	}
	
	public Agent retrieveByAgentCode(String agentCode) {
		return ((AgentDao) dao).retrieveByAgentCode(agentCode);
	}
	
	public Agent retrieveByMobileNumber(String mobileNumber) {
		return ((AgentDao) dao).retrieveByMobileNumber(mobileNumber);
	}

	public PagingDto<Agent> retrieveForListPage(PagingDto<Agent> pagingDto, String name, String agentCode,
			String mobileNumber) {
		pagingDto = ((AgentDao) dao).retrieveForListPage(pagingDto,name,agentCode,mobileNumber);
		return pagingDto;
	}
	
}
