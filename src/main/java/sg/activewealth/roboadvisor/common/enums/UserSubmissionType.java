package sg.activewealth.roboadvisor.common.enums;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

public enum UserSubmissionType implements ByteEnum {

  Kyc(0,"Kyc"),
  BankDetail(1,"BankDetail"),
  Remittance(2,"Remittance"),
  Redemption(3,"Redemption");

  private byte value;
  private String label;

  UserSubmissionType(int value,String label) {
    this.value = (byte) value;
    this.label = label;
  }

  @Override
  public byte getValue() {
    return this.value;
  }

  public String getLabel() {
	  return label;
  }
}
