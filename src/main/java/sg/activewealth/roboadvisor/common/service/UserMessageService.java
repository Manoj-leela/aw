package sg.activewealth.roboadvisor.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.common.dao.UserMessageDao;
import sg.activewealth.roboadvisor.common.model.UserMessage;
import sg.activewealth.roboadvisor.infra.service.AbstractService;

@Service
public class UserMessageService extends AbstractService<UserMessage> {

	public UserMessageService() {
		super(UserMessage.class);
	}

	@Autowired
	public void setDao(UserMessageDao dao) {
		super.dao = dao;
	}
}
