package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyRegisterOtpRequest {

    @NotBlank(message = "User id is required")
    private String userId;

    /** PHONE or EMAIL */
    @NotBlank(message = "Channel is required")
    private String channel;

    @NotBlank(message = "OTP is required")
    private String otp;
}
