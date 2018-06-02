package sg.activewealth.roboadvisor.common.dto;

import sg.activewealth.roboadvisor.infra.dto.AbstractDto;

public class UserMobileVerificationDto extends AbstractDto {

  //User's phone number
  private String phone;

  //RequestId of Nexmo
  private String token;

  //OTP code
  private String code;

  public String getPhone() {
    return phone;
  }

  public void setPhone(final String phone) {
    this.phone = phone;
  }

  public String getToken() {
    return token;
  }

  public void setToken(final String token) {
    this.token = token;
  }

  public String getCode() {
    return code;
  }

  public void setCode(final String code) {
    this.code = code;
  }
}
