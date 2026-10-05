package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Client-management (SOP POS) credentials. A blank apiKey keeps the stored one; a blank baseUrl falls back to config. */
@Data
public class SopPosSettingRequest {

    private Boolean enabled = Boolean.FALSE;

    @Size(max = 255)
    @Pattern(regexp = "^$|^https?://\\S+$", message = "Base URL must start with http:// or https://")
    private String baseUrl;

    @Size(max = 255)
    private String apiKey;
}
