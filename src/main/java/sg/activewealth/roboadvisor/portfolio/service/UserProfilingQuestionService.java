package sg.activewealth.roboadvisor.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.portfolio.dao.UserProfilingQuestionDao;
import sg.activewealth.roboadvisor.portfolio.model.UserProfilingQuestion;

@Service
public class UserProfilingQuestionService extends AbstractService<UserProfilingQuestion> {

	public UserProfilingQuestionService() {
		super(UserProfilingQuestion.class);
	}

	public UserProfilingQuestionService(Class<UserProfilingQuestion> modelClass) {
		super(modelClass);
	}

	@Autowired
	public void setDao(UserProfilingQuestionDao dao) {
		super.dao = dao;
	}

}
