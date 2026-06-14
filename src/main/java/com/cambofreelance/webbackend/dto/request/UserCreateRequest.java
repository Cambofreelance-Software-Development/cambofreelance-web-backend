package com.cambofreelance.webbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest extends BaseRequest {
    private Long id;
    private String username;
    private String phoneNumber;
    private String email;
    private String password;
    private String applicationType;
  private String userType;
}
