package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.dto.request.TenantCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantUpdateRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.TenantService;
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
}
