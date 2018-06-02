package sg.activewealth.roboadvisor.portfolio.dto;

import sg.activewealth.roboadvisor.infra.dto.AbstractDto;

public class UserProfileAnswerDto extends AbstractDto {

	private String answerCode;
	private String answer;
	private String imageUrl;

	public UserProfileAnswerDto(final String answerCode, final String answer , final String imageUrl) {
		this.answerCode = answerCode;
		this.answer = answer;
		this.imageUrl = imageUrl;
	}
	
	public UserProfileAnswerDto(final String answerCode, final String answer) {
		this.answerCode = answerCode;
		this.answer = answer;
	}

	public String getAnswerCode() {
		return answerCode;
	}

	public void setAnswerCode(final String answerCode) {
		this.answerCode = answerCode;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(final String answer) {
		this.answer = answer;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
