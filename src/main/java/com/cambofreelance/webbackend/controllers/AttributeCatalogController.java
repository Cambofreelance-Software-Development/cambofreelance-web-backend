package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.AttributeCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me/inventory/attributes")
@RequiredArgsConstructor
public class AttributeCatalogController {

    private final AttributeCatalogService attributeCatalogService;

    @GetMapping
    @PreAuthorize("hasAuthority('inventory.attributes.view')")
    public ResponseEntity<Object> getByCategory(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) String category
    ) {
        requireTenantId(tenantId);
        var result = attributeCatalogService.getAttributesByCategory(category);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/variants")
    @PreAuthorize("hasAuthority('inventory.attributes.view')")
    public ResponseEntity<Object> getVariantAttributes(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        requireTenantId(tenantId);
        var result = attributeCatalogService.getVariantAttributes();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Tenant context required");
        }
    }
}
