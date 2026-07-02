package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.SubscriptionCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantBrandingUpdateRequest;
import com.cambofreelance.webbackend.dto.request.TenantRoleCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantRoleUpdateRequest;
import com.cambofreelance.webbackend.dto.request.TenantUpdateRequest;
import com.cambofreelance.webbackend.dto.request.TenantUserCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantUserUpdateRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.InvoiceService;
import com.cambofreelance.webbackend.services.SubscriptionService;
import com.cambofreelance.webbackend.services.TenantRoleService;
import com.cambofreelance.webbackend.services.TenantService;
import com.cambofreelance.webbackend.services.UsageMetricsService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Self-service endpoints for Tenant Admins. The tenant is always resolved from the
 * caller's own JWT-backed session (the Tenant-Id header set by AuthTokenFilter), never
 * from a client-supplied path parameter — so a Tenant Admin can only ever act on their
 * own tenant. Cross-tenant administration stays on the /cms/tenants/** endpoints.
 */
@RestController
@RequestMapping("/me/tenant")
@RequiredArgsConstructor
public class MyTenantController {

    private final TenantService tenantService;
    private final SubscriptionService subscriptionService;
    private final UsageMetricsService usageMetricsService;
    private final TenantRoleService tenantRoleService;
    private final InvoiceService invoiceService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> myTenant(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        var result = tenantService.getById(requireTenantId(tenantId));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    /** Name + logo only — broader tenant fields (company info, type) stay on the /cms/tenants/** admin path. */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('my-tenant.users.manage', 'my-tenant.subscription.view', 'my-tenant.usage.view', 'my-tenant.roles.manage')")
    public ResponseEntity<Object> updateMyTenant(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @Valid @RequestBody TenantBrandingUpdateRequest request
    ) {
        TenantUpdateRequest updateRequest = new TenantUpdateRequest();
        updateRequest.setName(request.getName());
        updateRequest.setLogoUrl(request.getLogoUrl());
        var result = tenantService.update(requireTenantId(tenantId), updateRequest);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('my-tenant.users.manage')")
    public ResponseEntity<Object> listUsers(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = tenantService.listTenantUsers(requireTenantId(tenantId), search, status, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('my-tenant.users.manage')")
    public ResponseEntity<Object> assignUser(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String userId
    ) {
        var result = tenantService.assignUser(requireTenantId(tenantId), userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('my-tenant.users.manage')")
    public ResponseEntity<Object> removeUser(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String userId
    ) {
        tenantService.removeUser(requireTenantId(tenantId), userId);
        return new ResponseEntity<>(
            new MessageResponse("User removed from tenant", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('my-tenant.users.manage')")
    public ResponseEntity<Object> createUser(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @Valid @RequestBody TenantUserCreateRequest request
    ) {
        var result = tenantService.createTenantUser(requireTenantId(tenantId), request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('my-tenant.users.manage')")
    public ResponseEntity<Object> updateUser(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String userId,
        @RequestBody TenantUserUpdateRequest request
    ) {
        var result = tenantService.updateTenantUser(requireTenantId(tenantId), userId, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/users/{userId}/status")
    @PreAuthorize("hasAuthority('my-tenant.users.manage')")
    public ResponseEntity<Object> updateUserStatus(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String userId,
        @RequestBody Map<String, String> body
    ) {
        var result = tenantService.updateTenantUserStatus(requireTenantId(tenantId), userId, body.getOrDefault("status", ""));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/subscription")
    @PreAuthorize("hasAuthority('my-tenant.subscription.view')")
    public ResponseEntity<Object> subscription(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        var result = subscriptionService.getCurrentForTenant(requireTenantId(tenantId));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/subscription/upgrade")
    @PreAuthorize("hasAuthority('my-tenant.subscription.upgrade')")
    public ResponseEntity<Object> upgrade(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @Valid @RequestBody SubscriptionCreateRequest request
    ) {
        var result = subscriptionService.upgrade(requireTenantId(tenantId), request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/subscription/history")
    @PreAuthorize("hasAuthority('my-tenant.subscription.view')")
    public ResponseEntity<Object> subscriptionHistory(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        var result = subscriptionService.listHistoryForTenant(requireTenantId(tenantId));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/invoices")
    @PreAuthorize("hasAuthority('my-tenant.subscription.view')")
    public ResponseEntity<Object> invoices(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        var result = invoiceService.listForTenant(requireTenantId(tenantId));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/usage")
    @PreAuthorize("hasAuthority('my-tenant.usage.view')")
    public ResponseEntity<Object> usage(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        var result = usageMetricsService.getUsageForTenant(requireTenantId(tenantId));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('my-tenant.roles.manage')")
    public ResponseEntity<Object> listRoles(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        var result = tenantRoleService.listForTenant(requireTenantId(tenantId));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('my-tenant.roles.manage')")
    public ResponseEntity<Object> createRole(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @Valid @RequestBody TenantRoleCreateRequest request
    ) {
        var result = tenantRoleService.create(requireTenantId(tenantId), request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/roles/{roleId}")
    @PreAuthorize("hasAuthority('my-tenant.roles.manage')")
    public ResponseEntity<Object> updateRole(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String roleId,
        @RequestBody TenantRoleUpdateRequest request
    ) {
        var result = tenantRoleService.update(requireTenantId(tenantId), roleId, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/roles/{roleId}")
    @PreAuthorize("hasAuthority('my-tenant.roles.manage')")
    public ResponseEntity<Object> deleteRole(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String roleId
    ) {
        tenantRoleService.delete(requireTenantId(tenantId), roleId);
        return new ResponseEntity<>(new MessageResponse("Role deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/roles/seed-defaults")
    @PreAuthorize("hasAuthority('my-tenant.roles.manage')")
    public ResponseEntity<Object> seedDefaultRoles(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        var result = tenantRoleService.seedDefaultRoles(requireTenantId(tenantId));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PostMapping("/users/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('my-tenant.roles.manage')")
    public ResponseEntity<Object> assignRole(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String userId,
        @PathVariable String roleId
    ) {
        tenantRoleService.assignToUser(requireTenantId(tenantId), userId, roleId);
        return new ResponseEntity<>(new MessageResponse("Role assigned", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('my-tenant.roles.manage')")
    public ResponseEntity<Object> removeRole(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String userId,
        @PathVariable String roleId
    ) {
        tenantRoleService.removeFromUser(requireTenantId(tenantId), userId, roleId);
        return new ResponseEntity<>(new MessageResponse("Role removed", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    private String requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException("NO_TENANT_ASSIGNED", "Your account is not assigned to a tenant");
        }
        return tenantId;
    }
}
