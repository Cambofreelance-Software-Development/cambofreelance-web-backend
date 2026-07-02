package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.dto.request.TenantCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantRoleCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantRoleUpdateRequest;
import com.cambofreelance.webbackend.dto.request.TenantUpdateRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.TenantRoleService;
import com.cambofreelance.webbackend.services.TenantService;
import com.cambofreelance.webbackend.services.UsageMetricsService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;
    private final UsageMetricsService usageMetricsService;
    private final TenantRoleService tenantRoleService;

    @GetMapping
    @PreAuthorize("hasAuthority('tenants.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String tenantType,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = tenantService.list(search, status, tenantType, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('tenants.dashboard')")
    public ResponseEntity<Object> dashboard() {
        var result = tenantService.getDashboardStats();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('tenants.approve')")
    public ResponseEntity<Object> listPending(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = tenantService.listPending(page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('tenants.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = tenantService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('tenants.create')")
    public ResponseEntity<Object> create(@Valid @RequestBody TenantCreateRequest request) {
        var result = tenantService.create(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('tenants.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @RequestBody TenantUpdateRequest request
    ) {
        var result = tenantService.update(id, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('tenants.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        tenantService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Tenant deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('tenants.status')")
    public ResponseEntity<Object> updateStatus(
        @PathVariable String id,
        @RequestBody Map<String, String> body
    ) {
        var result = tenantService.updateStatus(id, body.getOrDefault("status", ""));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('tenants.approve')")
    public ResponseEntity<Object> approve(@PathVariable String id) {
        var result = tenantService.approve(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('tenants.approve')")
    public ResponseEntity<Object> reject(@PathVariable String id, @RequestBody Map<String, String> body) {
        var result = tenantService.reject(id, body.get("reason"));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{tenantId}/users")
    @PreAuthorize("hasAuthority('tenant-users.view')")
    public ResponseEntity<Object> listUsers(
        @PathVariable String tenantId,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = tenantService.listTenantUsers(tenantId, search, status, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{tenantId}/users/{userId}")
    @PreAuthorize("hasAuthority('tenant-users.assign')")
    public ResponseEntity<Object> assignUser(@PathVariable String tenantId, @PathVariable String userId) {
        var result = tenantService.assignUser(tenantId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{tenantId}/users/{userId}")
    @PreAuthorize("hasAuthority('tenant-users.remove')")
    public ResponseEntity<Object> removeUser(@PathVariable String tenantId, @PathVariable String userId) {
        tenantService.removeUser(tenantId, userId);
        return new ResponseEntity<>(
            new MessageResponse("User removed from tenant", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{tenantId}/usage")
    @PreAuthorize("hasAuthority('tenants.view')")
    public ResponseEntity<Object> usage(@PathVariable String tenantId) {
        var result = usageMetricsService.getUsageForTenant(tenantId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{tenantId}/roles")
    @PreAuthorize("hasAuthority('tenant-roles.view')")
    public ResponseEntity<Object> listRoles(@PathVariable String tenantId) {
        var result = tenantRoleService.listForTenant(tenantId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{tenantId}/roles")
    @PreAuthorize("hasAuthority('tenant-roles.manage')")
    public ResponseEntity<Object> createRole(
        @PathVariable String tenantId,
        @Valid @RequestBody TenantRoleCreateRequest request
    ) {
        var result = tenantRoleService.create(tenantId, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/{tenantId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('tenant-roles.manage')")
    public ResponseEntity<Object> updateRole(
        @PathVariable String tenantId,
        @PathVariable String roleId,
        @RequestBody TenantRoleUpdateRequest request
    ) {
        var result = tenantRoleService.update(tenantId, roleId, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{tenantId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('tenant-roles.manage')")
    public ResponseEntity<Object> deleteRole(@PathVariable String tenantId, @PathVariable String roleId) {
        tenantRoleService.delete(tenantId, roleId);
        return new ResponseEntity<>(
            new MessageResponse("Role deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
