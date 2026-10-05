package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Admin manual entry of a subscription's POS tenant access details. Blank/absent clears the field. */
@Data
public class PosAccessUpdateRequest {

    @Size(max = 255)
    private String backendUrl;

    @Size(max = 255)
    private String emenuUrl;

    @Size(max = 32)
    private String clientCode;

    @Size(max = 128)
    private String rootUser;

    @Size(max = 255)
    private String rootPassword;

    /** Optional — link the subscription to an existing SOP POS registration (enables PATCH sync).
     *  Blank/absent leaves the current registration id untouched. */
    @Size(max = 64)
    private String posRegistrationId;

    /** Optional explicit link mode: MANUAL (never auto-sync) / PLATFORM (sync from this platform;
     *  without a registration id the next sync provisions a new tenant). */
    @Pattern(regexp = "PLATFORM|MANUAL", message = "POS link mode must be PLATFORM or MANUAL")
    private String posLinkMode;
}
