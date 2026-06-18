package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.LoanApplicationCreateRequest;
import com.cambofreelance.webbackend.dto.request.LoanApplicationStatusActionRequest;
import com.cambofreelance.webbackend.dto.request.LoanApplicationUpdateRequest;
import com.cambofreelance.webbackend.dto.request.LoanDocumentUploadRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.LoanApplicationService;
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
 * Loan Application endpoints scoped to the caller's tenant. The tenant is always resolved from
 * the caller's own JWT-backed session (the Tenant-Id header), never a client-supplied value —
 * same pattern as {@link CustomerController}. The tenant id isn't passed into the service layer
 * because tenant-scoped data lives in the caller's own Postgres schema, routed automatically by
 * Hibernate's multi-tenant connection provider; the header is only checked here to fail fast
 * with a clear error if the caller has no tenant assigned.
 */
@RestController
@RequestMapping("/me/loan-applications")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @PostMapping
    @PreAuthorize("hasAuthority('loan-applications.create')")
    public ResponseEntity<Object> create(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @Valid @RequestBody LoanApplicationCreateRequest request
    ) {
        requireTenantId(tenantId);
        var result = loanApplicationService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('loan-applications.view')")
    public ResponseEntity<Object> search(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String applicationStatus,
        @RequestParam(required = false) String customerId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        requireTenantId(tenantId);
        var result = loanApplicationService.search(search, applicationStatus, customerId, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{loanApplicationId}")
    @PreAuthorize("hasAuthority('loan-applications.view')")
    public ResponseEntity<Object> getById(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        var result = loanApplicationService.getById(loanApplicationId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/{loanApplicationId}")
    @PreAuthorize("hasAuthority('loan-applications.update')")
    public ResponseEntity<Object> update(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String loanApplicationId,
        @Valid @RequestBody LoanApplicationUpdateRequest request
    ) {
        requireTenantId(tenantId);
        var result = loanApplicationService.update(loanApplicationId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{loanApplicationId}/submit")
    @PreAuthorize("hasAuthority('loan-applications.submit')")
    public ResponseEntity<Object> submit(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        var result = loanApplicationService.submit(loanApplicationId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{loanApplicationId}/approve")
    @PreAuthorize("hasAuthority('loan-applications.approve')")
    public ResponseEntity<Object> approve(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String loanApplicationId,
        @RequestBody LoanApplicationStatusActionRequest request
    ) {
        requireTenantId(tenantId);
        var result = loanApplicationService.approve(loanApplicationId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{loanApplicationId}/reject")
    @PreAuthorize("hasAuthority('loan-applications.reject')")
    public ResponseEntity<Object> reject(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String loanApplicationId,
        @RequestBody LoanApplicationStatusActionRequest request
    ) {
        requireTenantId(tenantId);
        var result = loanApplicationService.reject(loanApplicationId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Supporting documents ──────────────────────────────────────────────────

    @PostMapping("/{loanApplicationId}/documents")
    @PreAuthorize("hasAuthority('loan-applications.update')")
    public ResponseEntity<Object> uploadDocument(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String loanApplicationId,
        @Valid @RequestBody LoanDocumentUploadRequest request
    ) {
        requireTenantId(tenantId);
        var result = loanApplicationService.uploadDocument(loanApplicationId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping("/{loanApplicationId}/documents")
    @PreAuthorize("hasAuthority('loan-applications.view')")
    public ResponseEntity<Object> listDocuments(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        var result = loanApplicationService.listDocuments(loanApplicationId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{loanApplicationId}/documents/{documentId}")
    @PreAuthorize("hasAuthority('loan-applications.update')")
    public ResponseEntity<Object> deleteDocument(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String loanApplicationId,
        @PathVariable String documentId
    ) {
        requireTenantId(tenantId);
        loanApplicationService.deleteDocument(loanApplicationId, documentId, userId);
        return new ResponseEntity<>(new MessageResponse(null, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Repayment schedule ────────────────────────────────────────────────────

    @GetMapping("/{loanApplicationId}/repayment-schedule")
    @PreAuthorize("hasAuthority('loan-applications.view')")
    public ResponseEntity<Object> repaymentSchedule(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        var result = loanApplicationService.calculateRepaymentSchedule(loanApplicationId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException("NO_TENANT_ASSIGNED", "Your account is not assigned to a tenant");
        }
    }
}
