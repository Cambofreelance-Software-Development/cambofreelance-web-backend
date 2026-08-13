package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.CategoryProductRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.CategoryProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryProductController {

    private final CategoryProductService categoryProductService;

    // ── Public ────────────────────────────────────────────────────────────────

    @GetMapping("/product-categories")
    public ResponseEntity<Object> publicList() {
        var result = categoryProductService.listAll();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin (CMS) — reuses product.* permissions ──────────────────────────────

    @GetMapping("/cms/product-categories")
    @PreAuthorize("hasAuthority('product.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean all,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        if (Boolean.TRUE.equals(all) || search == null && page == 0) {
            var result = categoryProductService.listAll();
            return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
        }
        var result = categoryProductService.search(search, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/product-categories/{id}")
    @PreAuthorize("hasAuthority('product.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = categoryProductService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/product-categories")
    @PreAuthorize("hasAuthority('product.create')")
    public ResponseEntity<Object> create(
        @Valid @RequestBody CategoryProductRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = categoryProductService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/product-categories/{id}")
    @PreAuthorize("hasAuthority('product.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @Valid @RequestBody CategoryProductRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = categoryProductService.update(id, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/product-categories/{id}")
    @PreAuthorize("hasAuthority('product.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        categoryProductService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
