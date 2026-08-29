package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.FinancingApprovalRequest;
import com.cambofreelance.webbackend.dto.request.FinancingRejectionRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me/inventory/financing")
@RequiredArgsConstructor
public class FinancingController {

    private final SaleService saleService;

    @PostMapping("/{financingId}/approve")
    @PreAuthorize("hasAuthority('inventory.financing.approve')")
    public ResponseEntity<Object> approve(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String financingId,
        @RequestBody(required = false) FinancingApprovalRequest request
    ) {
        requireTenantId(tenantId);
        var result = saleService.approveFinancing(financingId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{financingId}/reject")
    @PreAuthorize("hasAuthority('inventory.financing.reject')")
    public ResponseEntity<Object> reject(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String financingId,
        @Valid @RequestBody FinancingRejectionRequest request
    ) {
        requireTenantId(tenantId);
        var result = saleService.rejectFinancing(financingId, request.getRejectionReason(), userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Tenant context required");
        }
    }
}
