package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.PaymentCreateRequest;
import com.cambofreelance.webbackend.dto.request.PaymentReverseRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.PaymentService;
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
@RequestMapping("/me/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasAuthority('payments.create')")
    public ResponseEntity<Object> receivePayment(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @Valid @RequestBody PaymentCreateRequest request
    ) {
        requireTenantId(tenantId);
        var result = paymentService.receivePayment(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('payments.view')")
    public ResponseEntity<Object> search(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) String loanApplicationId,
        @RequestParam(required = false) String paymentStatus,
        @RequestParam(required = false) String paymentType,
        @RequestParam(required = false) String currency,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        requireTenantId(tenantId);
        var result = paymentService.search(loanApplicationId, paymentStatus, paymentType, currency, page, size);
        return ResponseEntity.ok(new MessageResponse(result, ErrorCode.SUCCESS));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('payments.view')")
    public ResponseEntity<Object> getById(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String id
    ) {
        requireTenantId(tenantId);
        var result = paymentService.getById(id);
        return ResponseEntity.ok(new MessageResponse(result, ErrorCode.SUCCESS));
    }

    @GetMapping("/by-loan/{loanApplicationId}")
    @PreAuthorize("hasAuthority('payments.view')")
    public ResponseEntity<Object> getByLoan(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        var result = paymentService.getByLoan(loanApplicationId);
        return ResponseEntity.ok(new MessageResponse(result, ErrorCode.SUCCESS));
    }

    @PostMapping("/{id}/reverse")
    @PreAuthorize("hasAuthority('payments.reverse')")
    public ResponseEntity<Object> reversePayment(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String id,
        @Valid @RequestBody PaymentReverseRequest request
    ) {
        requireTenantId(tenantId);
        var result = paymentService.reversePayment(id, request, userId);
        return ResponseEntity.ok(new MessageResponse(result, ErrorCode.SUCCESS));
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException("TENANT_ID_REQUIRED", "X-Tenant-ID header is required");
        }
    }
}
