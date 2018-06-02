package sg.activewealth.roboadvisor.portfolio.dto;

import java.util.List;

import sg.activewealth.roboadvisor.infra.dto.AbstractDto;

public class UserProfileQuestionDto extends AbstractDto {

  private String question;
  private int questionId;
  private String questionCode;
  private List<UserProfileAnswerDto> answers;
  private List<UserProfileGoalDto> goals;
  private String title;
  private String description;
  
  public String getQuestion() {
    return question;
  }

  public void setQuestion(final String question) {
    this.question = question;
  }

  public List<UserProfileAnswerDto> getAnswers() {
    return answers;
  }
  
  public List<UserProfileGoalDto> getGoals() {
	return goals;
  }

  public void setAnswers(final List<UserProfileAnswerDto> answers) {
    this.answers = answers;
  }
  
  public void setGoals(final List<UserProfileGoalDto> goals){
	this.goals=goals;
  }

  public int getQuestionId() {
    return questionId;
  }

  public void setQuestionId(final int questionId) {
    this.questionId = questionId;
  }

  public String getQuestionCode() {
		return questionCode;
  }
	
  public void setQuestionCode(final String questionCode) {
		this.questionCode = questionCode;
  }

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

}
