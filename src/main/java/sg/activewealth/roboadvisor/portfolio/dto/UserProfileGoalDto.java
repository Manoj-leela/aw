package sg.activewealth.roboadvisor.portfolio.dto;

import sg.activewealth.roboadvisor.infra.dto.AbstractDto;
import sg.activewealth.roboadvisor.portfolio.enums.Goal;

public class UserProfileGoalDto extends AbstractDto{
	public Goal name;
	public String description;
	public String cost;
	public String imageUrl;
	
	public UserProfileGoalDto(Goal name,String description,String cost,String imageUrl){
		this.name=name;
		this.description=description;
		this.cost=cost;
		this.imageUrl = imageUrl;
	}
	
	public Goal getName() {
		return name;
	}

	public void setName(Goal name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(final String cost) {
		this.cost = cost;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
}
