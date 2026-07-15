package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.FeatureTabRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.FeatureTabService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeatureTabController {

    private final FeatureTabService featureTabService;

    // ── Public ────────────────────────────────────────────────────────────────

    @GetMapping("/feature-tabs")
    public ResponseEntity<Object> publicList() {
        var result = featureTabService.listAll();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin (CMS) ───────────────────────────────────────────────────────────

    @GetMapping("/cms/feature-tabs")
    @PreAuthorize("hasAuthority('feature_tabs.view')")
    public ResponseEntity<Object> list() {
        var result = featureTabService.listAll();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/feature-tabs/{id}")
    @PreAuthorize("hasAuthority('feature_tabs.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = featureTabService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/feature-tabs")
    @PreAuthorize("hasAuthority('feature_tabs.create')")
    public ResponseEntity<Object> create(
        @Valid @RequestBody FeatureTabRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = featureTabService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/feature-tabs/{id}")
    @PreAuthorize("hasAuthority('feature_tabs.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @Valid @RequestBody FeatureTabRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = featureTabService.update(id, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/feature-tabs/{id}")
    @PreAuthorize("hasAuthority('feature_tabs.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        featureTabService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
