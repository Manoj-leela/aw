package sg.activewealth.roboadvisor.common.dto;

import java.time.LocalDateTime;

public class ActivityDto {

	private String label;
	
	private LocalDateTime dateLabel;
	
	private String field1;
	
	private String field2;

	public ActivityDto(String label, LocalDateTime dateLabel, String field1, String field2) {
		super();
		this.label = label;
		this.dateLabel = dateLabel;
		this.field1 = field1;
		this.field2 = field2;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public LocalDateTime getDateLabel() {
		return dateLabel;
	}

	public void setDateLabel(LocalDateTime dateLabel) {
		this.dateLabel = dateLabel;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}
}
