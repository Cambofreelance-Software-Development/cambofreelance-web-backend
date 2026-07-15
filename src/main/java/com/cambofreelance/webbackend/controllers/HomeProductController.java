package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.HomeProductRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.HomeProductService;
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
public class HomeProductController {

    private final HomeProductService homeProductService;

    // ── Public ────────────────────────────────────────────────────────────────

    @GetMapping("/home-products")
    public ResponseEntity<Object> publicList() {
        var result = homeProductService.listAll();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin (CMS) ───────────────────────────────────────────────────────────

    @GetMapping("/cms/home-products")
    @PreAuthorize("hasAuthority('home_products.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean all,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        if (Boolean.TRUE.equals(all) || search == null && page == 0) {
            var result = homeProductService.listAll();
            return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
        }
        var result = homeProductService.search(search, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/home-products/{id}")
    @PreAuthorize("hasAuthority('home_products.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = homeProductService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/home-products")
    @PreAuthorize("hasAuthority('home_products.create')")
    public ResponseEntity<Object> create(
        @Valid @RequestBody HomeProductRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = homeProductService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/home-products/{id}")
    @PreAuthorize("hasAuthority('home_products.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @Valid @RequestBody HomeProductRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = homeProductService.update(id, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/home-products/{id}")
    @PreAuthorize("hasAuthority('home_products.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        homeProductService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
