package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.CollectionAssignRequest;
import com.cambofreelance.webbackend.dto.request.CollectionNoteCreateRequest;
import com.cambofreelance.webbackend.dto.request.CollectionStatusUpdateRequest;
import com.cambofreelance.webbackend.dto.request.PromiseToPayCreateRequest;
import com.cambofreelance.webbackend.dto.request.PtpStatusUpdateRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.CollectionService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping("/sync")
    @PreAuthorize("hasAuthority('collections.manage')")
    public ResponseEntity<Object> sync(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        requireTenantId(tenantId);
        int count = collectionService.syncOverdueCases(userId);
        return ResponseEntity.ok(new MessageResponse(Map.of("synced", count), ErrorCode.SUCCESS));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('collections.view')")
    public ResponseEntity<Object> search(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) String collectionStatus,
        @RequestParam(required = false) String assignedOfficerId,
        @RequestParam(required = false) String currency,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        requireTenantId(tenantId);
        var result = collectionService.search(collectionStatus, assignedOfficerId, currency, page, size);
        return ResponseEntity.ok(new MessageResponse(result, ErrorCode.SUCCESS));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('collections.view')")
    public ResponseEntity<Object> getById(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String id
    ) {
        requireTenantId(tenantId);
        return ResponseEntity.ok(new MessageResponse(collectionService.getById(id), ErrorCode.SUCCESS));
    }

    @PostMapping("/{id}/refresh")
    @PreAuthorize("hasAuthority('collections.manage')")
    public ResponseEntity<Object> refresh(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String id
    ) {
        requireTenantId(tenantId);
        return ResponseEntity.ok(new MessageResponse(collectionService.refreshCase(id, userId), ErrorCode.SUCCESS));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('collections.manage')")
    public ResponseEntity<Object> updateStatus(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String id,
        @Valid @RequestBody CollectionStatusUpdateRequest request
    ) {
        requireTenantId(tenantId);
        return ResponseEntity.ok(new MessageResponse(
            collectionService.updateStatus(id, request, userId), ErrorCode.SUCCESS));
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAuthority('collections.manage')")
    public ResponseEntity<Object> assign(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String id,
        @RequestBody CollectionAssignRequest request
    ) {
        requireTenantId(tenantId);
        return ResponseEntity.ok(new MessageResponse(
            collectionService.assign(id, request, userId), ErrorCode.SUCCESS));
    }

    @GetMapping("/{id}/notes")
    @PreAuthorize("hasAuthority('collections.view')")
    public ResponseEntity<Object> getNotes(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String id
    ) {
        requireTenantId(tenantId);
        return ResponseEntity.ok(new MessageResponse(collectionService.getNotes(id), ErrorCode.SUCCESS));
    }

    @PostMapping("/{id}/notes")
    @PreAuthorize("hasAuthority('collections.manage')")
    public ResponseEntity<Object> addNote(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String id,
        @Valid @RequestBody CollectionNoteCreateRequest request
    ) {
        requireTenantId(tenantId);
        return new ResponseEntity<>(new MessageResponse(
            collectionService.addNote(id, request, userId), ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/promises")
    @PreAuthorize("hasAuthority('collections.view')")
    public ResponseEntity<Object> getPromises(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String id
    ) {
        requireTenantId(tenantId);
        return ResponseEntity.ok(new MessageResponse(collectionService.getPromises(id), ErrorCode.SUCCESS));
    }

    @PostMapping("/{id}/promises")
    @PreAuthorize("hasAuthority('collections.manage')")
    public ResponseEntity<Object> addPromise(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String id,
        @Valid @RequestBody PromiseToPayCreateRequest request
    ) {
        requireTenantId(tenantId);
        return new ResponseEntity<>(new MessageResponse(
            collectionService.addPromise(id, request, userId), ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/promises/{ptpId}/status")
    @PreAuthorize("hasAuthority('collections.manage')")
    public ResponseEntity<Object> updatePtpStatus(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String ptpId,
        @Valid @RequestBody PtpStatusUpdateRequest request
    ) {
        requireTenantId(tenantId);
        return ResponseEntity.ok(new MessageResponse(
            collectionService.updatePtpStatus(ptpId, request, userId), ErrorCode.SUCCESS));
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException("TENANT_ID_REQUIRED", "X-Tenant-ID header is required");
        }
    }
}
