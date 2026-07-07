package com.cambofreelance.webbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonInclude(value= Include.NON_NULL)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OAuthRequest {
  @JsonProperty("client_secret")
  private String clientSecret;
  @JsonProperty("client_id")
  private String clientId;
  @JsonProperty("scope")
  private String scope;
  @JsonProperty("application_type")
  private String applicationType;
  @JsonProperty("username")
  private String username;
  @JsonProperty("password")
  private String password;
  @JsonProperty("device_id")
  private String deviceId;
  @JsonProperty("grant_type")
  private String grantType;
  @JsonProperty("redirect_uri")
  private String redirectUri;
  @JsonProperty("refresh_token")
  private String refreshToken;

}
