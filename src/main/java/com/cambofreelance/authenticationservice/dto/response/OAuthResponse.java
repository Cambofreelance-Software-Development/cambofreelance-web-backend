package com.cambofreelance.authenticationservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value= Include.NON_NULL)
public class OAuthResponse {
  @JsonProperty("access_token")
  private String token;
  @JsonProperty("refresh_token")
  private String refreshToken;
  @JsonProperty("expires_in")
  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date expiresIn;
  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("scope")
  private List<String> scope;
  @JsonProperty("is_new_device")
  private boolean newDevice;

}
