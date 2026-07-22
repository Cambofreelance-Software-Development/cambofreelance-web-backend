package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ManualVerifyRequest {

    /** true = mark APPROVED and activate; false = mark DECLINED */
    @NotNull
    private Boolean approve;

    private String note;
}
