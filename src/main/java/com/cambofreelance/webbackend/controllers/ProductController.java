package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.ProductCreateRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.ProductService;
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

@RestController
@RequestMapping("/me/inventory/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('inventory.products.create')")
    public ResponseEntity<Object> create(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @Valid @RequestBody ProductCreateRequest request
    ) {
        requireTenantId(tenantId);
        var result = productService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('inventory.products.update')")
    public ResponseEntity<Object> update(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String productId,
        @Valid @RequestBody ProductCreateRequest request
    ) {
        requireTenantId(tenantId);
        var result = productService.update(productId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('inventory.products.view')")
    public ResponseEntity<Object> search(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String categoryId,
        @RequestParam(required = false) String productType,
        @RequestParam(required = false) String trackingType,
        @RequestParam(required = false) String catalogStatus,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        requireTenantId(tenantId);
        var result = productService.search(search, categoryId, productType, trackingType, catalogStatus, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAuthority('inventory.products.view')")
    public ResponseEntity<Object> getById(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String productId
    ) {
        requireTenantId(tenantId);
        var result = productService.getById(productId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{productId}/detail")
    @PreAuthorize("hasAuthority('inventory.products.view')")
    public ResponseEntity<Object> getDetail(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String productId
    ) {
        requireTenantId(tenantId);
        var result = productService.getDetail(productId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{productId}/variants")
    @PreAuthorize("hasAuthority('inventory.products.view')")
    public ResponseEntity<Object> getVariants(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String productId
    ) {
        requireTenantId(tenantId);
        var result = productService.getVariants(productId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('inventory.products.delete')")
    public ResponseEntity<Object> delete(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String productId
    ) {
        requireTenantId(tenantId);
        productService.delete(productId, userId);
        return new ResponseEntity<>(new MessageResponse("Product deactivated", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/generate-sku")
    @PreAuthorize("hasAuthority('inventory.products.view')")
    public ResponseEntity<Object> generateSku(
        @RequestParam(required = false) String categoryId,
        @RequestParam(required = false) String brand,
        @RequestParam(required = false) String model
    ) {
        String sku = productService.generateSku(categoryId, brand, model);
        return new ResponseEntity<>(new MessageResponse(sku, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Tenant context required");
        }
    }
}
