package sg.activewealth.roboadvisor.trade.enums;


import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

public enum TradePosition implements ByteEnum {
  LONG(0,"Long"),
  SHORT(1,"Short");

  private byte value;
  private String label;

  TradePosition(int value,String label) {
    this.value = (byte) value;
    this.label = label;
  }

  @Override
  public byte getValue() {
    return value;
  }

  public String getLabel() {
    return label;
  }

  public void setValue(byte value) {
	this.value = value;
  }

  public void setLabel(String label) {	
	this.label = label;
  }
}
