package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.TenantCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantRegistrationRequest;
import com.cambofreelance.webbackend.dto.request.TenantUpdateRequest;
import com.cambofreelance.webbackend.dto.request.TenantUserCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantUserUpdateRequest;
import com.cambofreelance.webbackend.dto.response.TenantDashboardResponse;
import com.cambofreelance.webbackend.dto.response.TenantRegistrationResponse;
import com.cambofreelance.webbackend.dto.response.TenantResponse;
import com.cambofreelance.webbackend.dto.response.UserProfileResponse;
import org.springframework.data.domain.Page;

public interface TenantService {

    Page<TenantResponse> list(String search, String status, String tenantType, int page, int size);

    TenantResponse getById(String id);

    TenantResponse create(TenantCreateRequest request);

    TenantResponse update(String id, TenantUpdateRequest request);

    void delete(String id);

    TenantResponse updateStatus(String id, String status);

    TenantDashboardResponse getDashboardStats();

    Page<UserProfileResponse> listTenantUsers(String tenantId, String search, String status, int page, int size);

    /** Requires a free seat under the tenant's package user limit. */
    UserProfileResponse assignUser(String tenantId, String userId);

    void removeUser(String tenantId, String userId);

    /** Creates a brand-new user scoped to the tenant; username is auto-prefixed "{tenantCode}.{username}". */
    UserProfileResponse createTenantUser(String tenantId, TenantUserCreateRequest request);

    UserProfileResponse updateTenantUser(String tenantId, String userId, TenantUserUpdateRequest request);

    /** status must be ACT (enable) or LEA (disable); disabling revokes all active sessions. */
    UserProfileResponse updateTenantUserStatus(String tenantId, String userId, String status);

    /** Throws if the tenant is suspended/expired/deleted. Tenant status is kept in sync by the Subscription module. */
    void assertActiveForLogin(String tenantId);

    /** Public self-service registration: creates a PEN tenant + PEN subscription + a new (unassigned) user. */
    TenantRegistrationResponse register(TenantRegistrationRequest request);

    Page<TenantResponse> listPending(int page, int size);

    /** Activates the tenant + its pending subscription, makes the requesting user its Tenant Admin, provisions the schema. */
    TenantResponse approve(String tenantId);

    TenantResponse reject(String tenantId, String reason);
}
