package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Data;

@Data
public class PackageFeatureToggleRequest {

    /** Feature code -> enabled. Only the codes present are changed. */
    @NotNull
    private Map<String, Boolean> features;
}
