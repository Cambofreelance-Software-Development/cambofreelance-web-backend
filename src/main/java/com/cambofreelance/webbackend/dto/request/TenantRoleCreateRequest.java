package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.Data;

@Data
public class TenantRoleCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private Set<String> permissions;
}
