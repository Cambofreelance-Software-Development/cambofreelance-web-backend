package com.cambofreelance.webbackend.dto.request;

import java.util.Set;
import lombok.Data;

@Data
public class TenantRoleUpdateRequest {

    private String name;

    private String description;

    private Set<String> permissions;
}
