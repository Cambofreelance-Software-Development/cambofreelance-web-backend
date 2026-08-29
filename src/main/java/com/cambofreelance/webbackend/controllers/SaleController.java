package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.FinancingSubmitRequest;
import com.cambofreelance.webbackend.dto.request.SaleCreateRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me/inventory/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    @PreAuthorize("hasAuthority('inventory.sales.create')")
    public ResponseEntity<Object> create(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @Valid @RequestBody SaleCreateRequest request
    ) {
        requireTenantId(tenantId);
        var result = saleService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping("/{saleId}")
    @PreAuthorize("hasAuthority('inventory.sales.view')")
    public ResponseEntity<Object> getById(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String saleId
    ) {
        requireTenantId(tenantId);
        var result = saleService.getById(saleId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{saleId}/detail")
    @PreAuthorize("hasAuthority('inventory.sales.view')")
    public ResponseEntity<Object> getDetail(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String saleId
    ) {
        requireTenantId(tenantId);
        var result = saleService.getDetail(saleId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('inventory.sales.view')")
    public ResponseEntity<Object> search(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String saleStatus,
        @RequestParam(required = false) String paymentType,
        @RequestParam(required = false) String customerId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        requireTenantId(tenantId);
        var result = saleService.search(search, saleStatus, paymentType, customerId, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{saleId}/reserve")
    @PreAuthorize("hasAuthority('inventory.reserve.create')")
    public ResponseEntity<Object> reserve(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String saleId,
        @RequestParam String inventoryItemId
    ) {
        requireTenantId(tenantId);
        var result = saleService.reserveUnit(saleId, inventoryItemId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{saleId}/submit-financing")
    @PreAuthorize("hasAuthority('inventory.financing.submit')")
    public ResponseEntity<Object> submitFinancing(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String saleId,
        @Valid @RequestBody FinancingSubmitRequest request
    ) {
        requireTenantId(tenantId);
        var result = saleService.submitFinancing(saleId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{saleId}/confirm")
    @PreAuthorize("hasAuthority('inventory.sales.confirm')")
    public ResponseEntity<Object> confirm(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String saleId
    ) {
        requireTenantId(tenantId);
        var result = saleService.confirmSale(saleId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{saleId}/deliver")
    @PreAuthorize("hasAuthority('inventory.sales.deliver')")
    public ResponseEntity<Object> deliver(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String saleId
    ) {
        requireTenantId(tenantId);
        var result = saleService.deliverUnit(saleId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{saleId}/cancel")
    @PreAuthorize("hasAuthority('inventory.sales.cancel')")
    public ResponseEntity<Object> cancel(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String saleId
    ) {
        requireTenantId(tenantId);
        var result = saleService.cancelSale(saleId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Tenant context required");
        }
    }
}
