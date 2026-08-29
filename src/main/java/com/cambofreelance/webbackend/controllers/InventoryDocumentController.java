package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.InventoryDocumentUploadRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.InventoryDocumentService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me/inventory/documents")
@RequiredArgsConstructor
public class InventoryDocumentController {

    private final InventoryDocumentService inventoryDocumentService;

    @PostMapping
    @PreAuthorize("hasAuthority('inventory.docs.upload')")
    public ResponseEntity<Object> upload(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @Valid @RequestBody InventoryDocumentUploadRequest request
    ) {
        requireTenantId(tenantId);
        var result = inventoryDocumentService.upload(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('inventory.docs.view')")
    public ResponseEntity<Object> getByOwner(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam String ownerType,
        @RequestParam String ownerId
    ) {
        requireTenantId(tenantId);
        var result = inventoryDocumentService.getByOwner(ownerType, ownerId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{documentId}/verify")
    @PreAuthorize("hasAuthority('inventory.docs.verify')")
    public ResponseEntity<Object> verify(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String documentId
    ) {
        requireTenantId(tenantId);
        var result = inventoryDocumentService.verify(documentId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{documentId}")
    @PreAuthorize("hasAuthority('inventory.docs.delete')")
    public ResponseEntity<Object> delete(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String documentId
    ) {
        requireTenantId(tenantId);
        inventoryDocumentService.delete(documentId, userId);
        return new ResponseEntity<>(new MessageResponse("Document removed", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Tenant context required");
        }
    }
}
