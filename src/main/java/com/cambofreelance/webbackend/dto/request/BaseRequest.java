package com.cambofreelance.webbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseRequest {
    private String userId;
    private String username;
    private String phoneNumber;
    private String email;
    private String applicationType;
    private String status;
    private String password;
    private String confirmPassword;
}
