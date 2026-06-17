package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.TenantRoleCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantRoleUpdateRequest;
import com.cambofreelance.webbackend.dto.response.TenantRoleResponse;
import java.util.List;

public interface TenantRoleService {

    List<TenantRoleResponse> listForTenant(String tenantId);

    TenantRoleResponse create(String tenantId, TenantRoleCreateRequest request);

    TenantRoleResponse update(String tenantId, String roleId, TenantRoleUpdateRequest request);

    void delete(String tenantId, String roleId);

    void assignToUser(String tenantId, String userId, String roleId);

    void removeFromUser(String tenantId, String userId, String roleId);

    List<TenantRoleResponse> seedDefaultRoles(String tenantId);
}
