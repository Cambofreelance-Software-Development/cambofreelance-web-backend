package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.InventoryItemCreateRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.InventoryItemService;
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
@RequestMapping("/me/inventory/items")
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    @PostMapping
    @PreAuthorize("hasAuthority('inventory.items.create')")
    public ResponseEntity<Object> create(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @Valid @RequestBody InventoryItemCreateRequest request
    ) {
        requireTenantId(tenantId);
        var result = inventoryItemService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping("/{itemId}")
    @PreAuthorize("hasAuthority('inventory.items.view')")
    public ResponseEntity<Object> getById(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String itemId
    ) {
        requireTenantId(tenantId);
        var result = inventoryItemService.getById(itemId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('inventory.items.view')")
    public ResponseEntity<Object> search(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String variantId,
        @RequestParam(required = false) String warehouseId,
        @RequestParam(required = false) String itemStatus,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        requireTenantId(tenantId);
        var result = inventoryItemService.search(search, variantId, warehouseId, itemStatus, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{itemId}/timeline")
    @PreAuthorize("hasAuthority('inventory.items.view')")
    public ResponseEntity<Object> getTimeline(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String itemId
    ) {
        requireTenantId(tenantId);
        var result = inventoryItemService.getTimeline(itemId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Tenant context required");
        }
    }
}
