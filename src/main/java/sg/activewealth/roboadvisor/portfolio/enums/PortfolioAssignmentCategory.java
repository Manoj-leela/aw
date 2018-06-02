package sg.activewealth.roboadvisor.portfolio.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PortfolioAssignmentCategory implements ByteEnum {

	PublicBVI(0, "Public BVI"),
	PrivateBVI(1,"Private BVI");

	private byte value;
	private String label;

	PortfolioAssignmentCategory(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}
	
	@JsonCreator
	public static PortfolioAssignmentCategory get(String label) {
		PortfolioAssignmentCategory category = null;
		for (PortfolioAssignmentCategory category2 : PortfolioAssignmentCategory.values()) {
			if (category2.getLabel().equals(label)) {
				category = category2;
				break;
			}
		}
		return category;
	}

	public static PortfolioAssignmentCategory get(int value) {
		PortfolioAssignmentCategory category = null;
		for (PortfolioAssignmentCategory category2 : PortfolioAssignmentCategory.values()) {
			if (category2.getValue() == value) {
				category = category2;
				break;
			}
		}
		return category;
	}

	@Override
	public byte getValue() {
		return value;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

}
