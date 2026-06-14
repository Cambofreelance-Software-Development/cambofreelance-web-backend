package com.cambofreelance.authenticationservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    @NotBlank @Email
    private String email;

    @NotBlank(message = "OTP is required")
    private String otp;

    @NotBlank @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
