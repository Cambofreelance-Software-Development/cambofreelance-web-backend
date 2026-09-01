package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmtpSettingRequest {
    @NotBlank(message = "SMTP host is required")
    private String host;

    @Builder.Default
    private Integer port = 587;

    private String username;
    private String password;
    private String fromEmail;
    private String fromName;

    @Builder.Default
    private String encryption = "STARTTLS";

    @Builder.Default
    private Boolean auth = true;

    // Trusts the configured host's TLS cert even if validation fails (expired/self-signed).
    // Off by default — only meant as a stopgap until a bad cert is renewed.
    @Builder.Default
    private Boolean trustInvalidCert = false;
}
