package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.dto.request.PackageCreateRequest;
import com.cambofreelance.webbackend.dto.request.PackageFeatureToggleRequest;
import com.cambofreelance.webbackend.dto.request.PackageUpdateRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.SubscriptionPackageService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/subscription-packages")
@RequiredArgsConstructor
public class SubscriptionPackageController {

    private final SubscriptionPackageService packageService;

    @GetMapping
    @PreAuthorize("hasAuthority('subscription-packages.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = packageService.list(search, status, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('subscription-packages.view')")
    public ResponseEntity<Object> listActive() {
        var result = packageService.listActive();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('subscription-packages.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = packageService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('subscription-packages.create')")
    public ResponseEntity<Object> create(@Valid @RequestBody PackageCreateRequest request) {
        var result = packageService.create(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('subscription-packages.update')")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody PackageUpdateRequest request) {
        var result = packageService.update(id, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('subscription-packages.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        packageService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Package deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/{id}/features")
    @PreAuthorize("hasAuthority('subscription-packages.update')")
    public ResponseEntity<Object> updateFeatures(
        @PathVariable String id,
        @Valid @RequestBody PackageFeatureToggleRequest request
    ) {
        var result = packageService.updateFeatures(id, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
