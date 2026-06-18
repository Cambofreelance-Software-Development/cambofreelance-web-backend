package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.CustomerCreateRequest;
import com.cambofreelance.webbackend.dto.request.CustomerDocumentUploadRequest;
import com.cambofreelance.webbackend.dto.request.CustomerStatusActionRequest;
import com.cambofreelance.webbackend.dto.request.CustomerUpdateRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.CustomerService;
import jakarta.validation.Valid;
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
 * Self-service Customer Onboarding endpoints. The tenant is always resolved from the caller's
 * own JWT-backed session (the Tenant-Id header), never a client-supplied value — same pattern
 * as {@link MyTenantController}. The tenant id isn't passed into the service layer because
 * tenant-scoped data lives in the caller's own Postgres schema, routed automatically by
 * Hibernate's multi-tenant connection provider; the header is only checked here to fail fast
 * with a clear error if the caller has no tenant assigned.
 */
@RestController
@RequestMapping("/me/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @PreAuthorize("hasAuthority('customers.create')")
    public ResponseEntity<Object> create(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @Valid @RequestBody CustomerCreateRequest request
    ) {
        requireTenantId(tenantId);
        var result = customerService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('customers.view')")
    public ResponseEntity<Object> search(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String onboardingStatus,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        requireTenantId(tenantId);
        var result = customerService.search(search, onboardingStatus, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAuthority('customers.view')")
    public ResponseEntity<Object> getById(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String customerId
    ) {
        requireTenantId(tenantId);
        var result = customerService.getById(customerId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/{customerId}")
    @PreAuthorize("hasAuthority('customers.update')")
    public ResponseEntity<Object> update(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String customerId,
        @Valid @RequestBody CustomerUpdateRequest request
    ) {
        requireTenantId(tenantId);
        var result = customerService.update(customerId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{customerId}/documents")
    @PreAuthorize("hasAuthority('customer-documents.upload')")
    public ResponseEntity<Object> uploadDocument(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String customerId,
        @Valid @RequestBody CustomerDocumentUploadRequest request
    ) {
        requireTenantId(tenantId);
        var result = customerService.uploadDocument(customerId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}/documents")
    @PreAuthorize("hasAuthority('customer-documents.view')")
    public ResponseEntity<Object> listDocuments(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String customerId
    ) {
        requireTenantId(tenantId);
        var result = customerService.listDocuments(customerId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{customerId}/documents/{documentId}")
    @PreAuthorize("hasAuthority('customer-documents.delete')")
    public ResponseEntity<Object> deleteDocument(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String customerId,
        @PathVariable String documentId
    ) {
        requireTenantId(tenantId);
        customerService.deleteDocument(customerId, documentId, userId);
        return new ResponseEntity<>(new MessageResponse("Document deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{customerId}/submit")
    @PreAuthorize("hasAuthority('customers.submit')")
    public ResponseEntity<Object> submitForVerification(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String customerId
    ) {
        requireTenantId(tenantId);
        var result = customerService.submitForVerification(customerId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{customerId}/verify")
    @PreAuthorize("hasAuthority('customers.verify')")
    public ResponseEntity<Object> verify(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String customerId,
        @RequestBody CustomerStatusActionRequest request
    ) {
        requireTenantId(tenantId);
        var result = customerService.verify(customerId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{customerId}/approve")
    @PreAuthorize("hasAuthority('customers.approve')")
    public ResponseEntity<Object> approve(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String customerId,
        @RequestBody CustomerStatusActionRequest request
    ) {
        requireTenantId(tenantId);
        var result = customerService.approve(customerId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{customerId}/reject")
    @PreAuthorize("hasAuthority('customers.reject')")
    public ResponseEntity<Object> reject(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String customerId,
        @RequestBody CustomerStatusActionRequest request
    ) {
        requireTenantId(tenantId);
        var result = customerService.reject(customerId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/{customerId}/activate")
    @PreAuthorize("hasAuthority('customers.activate')")
    public ResponseEntity<Object> activate(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String customerId
    ) {
        requireTenantId(tenantId);
        var result = customerService.activate(customerId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/{customerId}/deactivate")
    @PreAuthorize("hasAuthority('customers.activate')")
    public ResponseEntity<Object> deactivate(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String customerId
    ) {
        requireTenantId(tenantId);
        var result = customerService.deactivate(customerId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    private String requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException("NO_TENANT_ASSIGNED", "Your account is not assigned to a tenant");
        }
        return tenantId;
    }
}
