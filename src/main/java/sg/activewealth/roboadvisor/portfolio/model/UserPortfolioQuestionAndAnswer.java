package sg.activewealth.roboadvisor.portfolio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity
@Table(name = "user_portfolio_question_and_answer")
public class UserPortfolioQuestionAndAnswer extends AbstractModel{

	public UserPortfolioQuestionAndAnswer() {
		super();
	}

	public UserPortfolioQuestionAndAnswer(String id) {
		super(id);
	}
	
	/**
	 * @param id
	 * @param question
	 * @param answer
	 */
	public UserPortfolioQuestionAndAnswer(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "user_portfolio_id")
	@JsonBackReference
	private UserPortfolio userPortfolio;
	
	@Column(name = "question")
	private String question;
	
	@Column(name = "answer")
	private String answer;

	public UserPortfolio getUserPortfolio() {
		return userPortfolio;
	}

	public void setUserPortfolio(UserPortfolio userPortfolio) {
		this.userPortfolio = userPortfolio;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
