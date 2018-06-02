package sg.activewealth.roboadvisor.portfolio.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PortfolioFundingStatus implements ByteEnum {

    NotFundedYet(0, "Not Funded Yet"), Funded(1, "Funded"), Completed(2, "Completed");

    private byte value;
    private String label;

    private PortfolioFundingStatus(int value, String label) {
        this.value = (byte) value;
        this.label = label;
    }

    @JsonCreator
    public static PortfolioFundingStatus get(String label) {
        PortfolioFundingStatus portfolioFundingStatus = null;
        for (PortfolioFundingStatus portfolioFundingStatus2 : PortfolioFundingStatus.values()) {
            if (portfolioFundingStatus2.getLabel().equalsIgnoreCase(label)) {
                portfolioFundingStatus = portfolioFundingStatus2;
                break;
            }
        }
        return portfolioFundingStatus;
    }

    @JsonCreator
    public static PortfolioFundingStatus get(int value) {
        PortfolioFundingStatus portfolioFundingStatus = null;
        for (PortfolioFundingStatus portfolioFundingStatus2 : PortfolioFundingStatus.values()) {
            if (portfolioFundingStatus2.getValue() == value) {
                portfolioFundingStatus = portfolioFundingStatus2;
                break;
            }
        }
        return portfolioFundingStatus;
    }

    @Override
    public byte getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

}
