package com.cambofreelance.webbackend.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenCacheDto implements Serializable {

    private String userId;
    private String username;
    private String deviceId;
    private String applicationId;
    private String userType;
    private String tenantId;
    private String status;

    /** Permission codes collected from all active roles at login time (e.g. "users.view"). */
    private Set<String> permissions;

    public Set<String> getPermissions() {
        return permissions == null ? Collections.emptySet() : permissions;
    }
}
