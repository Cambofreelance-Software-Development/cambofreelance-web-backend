package com.cambofreelance.webbackend.dto.request;

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
}
