package com.cambofreelance.webbackend.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TenantDashboardResponse {

    private long totalTenants;
    private long activeTenants;
    private long suspendedTenants;
    private long expiredTenants;
    private long totalAssignedUsers;
    private List<TenantResponse> tenantsExpiringSoon;
    private List<TenantResponse> recentTenants;
}
