package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.LoanDisbursementContractRequest;
import com.cambofreelance.webbackend.dto.request.LoanDisbursementCreateRequest;
import com.cambofreelance.webbackend.dto.request.LoanDisbursementDisburseRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.LoanDisbursementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me/loan-disbursements")
@RequiredArgsConstructor
public class LoanDisbursementController {

    private final LoanDisbursementService loanDisbursementService;

    @PostMapping
    @PreAuthorize("hasAuthority('loan-disbursements.create')")
    public ResponseEntity<Object> create(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @Valid @RequestBody LoanDisbursementCreateRequest request
    ) {
        requireTenantId(tenantId);
        var result = loanDisbursementService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('loan-disbursements.view')")
    public ResponseEntity<Object> search(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String disbursementStatus,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        requireTenantId(tenantId);
        var result = loanDisbursementService.search(search, disbursementStatus, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{disbursementId}")
    @PreAuthorize("hasAuthority('loan-disbursements.view')")
    public ResponseEntity<Object> getById(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String disbursementId
    ) {
        requireTenantId(tenantId);
        var result = loanDisbursementService.getById(disbursementId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/by-loan/{loanApplicationId}")
    @PreAuthorize("hasAuthority('loan-disbursements.view')")
    public ResponseEntity<Object> getByLoanApplication(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        var result = loanDisbursementService.getByLoanApplicationId(loanApplicationId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{disbursementId}/contract")
    @PreAuthorize("hasAuthority('loan-disbursements.upload-contract')")
    public ResponseEntity<Object> uploadContract(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String disbursementId,
        @Valid @RequestBody LoanDisbursementContractRequest request
    ) {
        requireTenantId(tenantId);
        var result = loanDisbursementService.uploadContract(disbursementId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{disbursementId}/disburse")
    @PreAuthorize("hasAuthority('loan-disbursements.disburse')")
    public ResponseEntity<Object> disburse(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String disbursementId,
        @Valid @RequestBody LoanDisbursementDisburseRequest request
    ) {
        requireTenantId(tenantId);
        var result = loanDisbursementService.disburse(disbursementId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException("NO_TENANT_ASSIGNED", "Your account is not assigned to a tenant");
        }
    }
}
