package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AutoRenewToggleRequest {

    /** true = enable Card-on-File auto-renewal (requires an existing payment token); false always allowed */
    @NotNull
    private Boolean autoRenew;
}
