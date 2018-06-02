package sg.activewealth.roboadvisor.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.portfolio.dao.UserPortfolioQuesAndAnsDao;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolioQuestionAndAnswer;

@Service
public class UserPortfolioQuesAndAnsService extends AbstractService<UserPortfolioQuestionAndAnswer> {

	public UserPortfolioQuesAndAnsService(Class<UserPortfolioQuestionAndAnswer> modelClass) {
		super(modelClass);
		// TODO Auto-generated constructor stub
	}
	
	public UserPortfolioQuesAndAnsService() {
		super(UserPortfolioQuestionAndAnswer.class);
	}

	@Autowired
	public void setDao(UserPortfolioQuesAndAnsDao userPortfolioQuesAndAnsDao){
		super.dao = userPortfolioQuesAndAnsDao;
	}
	
}
