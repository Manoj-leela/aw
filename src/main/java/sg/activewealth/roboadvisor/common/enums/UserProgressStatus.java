package sg.activewealth.roboadvisor.common.enums;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

public enum UserProgressStatus implements ByteEnum{
	
	RiskProfiling(0),
	ReferralCode(1),
	PortfolioRecommendation(2),
	LegalAgreements(3),
	Home(4);
	
    private byte value;

    private UserProgressStatus(int value) {
        this.value = (byte) value;
    }

    @Override
    public byte getValue() {
        return this.value;
    }

}

