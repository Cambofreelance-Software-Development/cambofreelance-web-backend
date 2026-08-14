package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.BusinessTypeCatalogRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.BusinessTypeCatalogService;
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
public class BusinessTypeCatalogController {

    private final BusinessTypeCatalogService businessTypeCatalogService;

    // ── Public ────────────────────────────────────────────────────────────────

    @GetMapping("/business-type-catalog")
    public ResponseEntity<Object> publicList(@RequestParam(required = false) String categoryId) {
        var result = businessTypeCatalogService.listPublic(categoryId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/business-type-catalog/{id}")
    public ResponseEntity<Object> publicGetById(@PathVariable String id) {
        var result = businessTypeCatalogService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin (CMS) ───────────────────────────────────────────────────────────

    @GetMapping("/cms/business-type-catalog")
    @PreAuthorize("hasAuthority('business_type_catalog.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean all,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        if (Boolean.TRUE.equals(all) || search == null && page == 0) {
            var result = businessTypeCatalogService.listAll();
            return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
        }
        var result = businessTypeCatalogService.search(search, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/business-type-catalog/{id}")
    @PreAuthorize("hasAuthority('business_type_catalog.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = businessTypeCatalogService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/business-type-catalog")
    @PreAuthorize("hasAuthority('business_type_catalog.create')")
    public ResponseEntity<Object> create(
        @Valid @RequestBody BusinessTypeCatalogRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = businessTypeCatalogService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/business-type-catalog/{id}")
    @PreAuthorize("hasAuthority('business_type_catalog.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @Valid @RequestBody BusinessTypeCatalogRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = businessTypeCatalogService.update(id, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/business-type-catalog/{id}")
    @PreAuthorize("hasAuthority('business_type_catalog.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        businessTypeCatalogService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
