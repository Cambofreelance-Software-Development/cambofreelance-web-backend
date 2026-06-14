package com.cambofreelance.authenticationservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileRequest {

    @JsonProperty("username")
    private String username;

    @Email(message = "Email format is invalid")
    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;
}
