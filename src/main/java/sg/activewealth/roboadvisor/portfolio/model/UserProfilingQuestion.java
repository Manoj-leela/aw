package sg.activewealth.roboadvisor.portfolio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.portfolio.enums.Goal;

@Entity
@Table(name = "user_profiling_question")
public class UserProfilingQuestion extends AbstractModel {

	@Column(name = "goal")
	private Goal goal;

	@Column(name = "user_profiling_question_1")
	private String question1;

	@Column(name = "user_profiling_question_2")
	private String question2;

	@Column(name = "user_profiling_question_3")
	private String question3;

	@Column(name = "user_profiling_question_4")
	private String question4;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne
	@JoinColumn(name = "user_portfolio_id")
	private UserPortfolio userPortfolio;

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	/**
	 * @return the question1
	 */
	public String getQuestion1() {
		return question1;
	}

	/**
	 * @param question1
	 *            the question1 to set
	 */
	public void setQuestion1(String question1) {
		this.question1 = question1;
	}

	/**
	 * @return the question2
	 */
	public String getQuestion2() {
		return question2;
	}

	/**
	 * @param question2
	 *            the question2 to set
	 */
	public void setQuestion2(String question2) {
		this.question2 = question2;
	}

	/**
	 * @return the question3
	 */
	public String getQuestion3() {
		return question3;
	}

	/**
	 * @param question3
	 *            the question3 to set
	 */
	public void setQuestion3(String question3) {
		this.question3 = question3;
	}

	/**
	 * @return the question4
	 */
	public String getQuestion4() {
		return question4;
	}

	/**
	 * @param question4
	 *            the question4 to set
	 */
	public void setQuestion4(String question4) {
		this.question4 = question4;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the userPortfolio
	 */
	public UserPortfolio getUserPortfolio() {
		return userPortfolio;
	}

	/**
	 * @param userPortfolio
	 *            the userPortfolio to set
	 */
	public void setUserPortfolio(UserPortfolio userPortfolio) {
		this.userPortfolio = userPortfolio;
	}

}
