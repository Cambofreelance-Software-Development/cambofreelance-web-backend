package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.request.TaxonomyDeleteRequest;
import com.cambofreelance.webbackend.dto.request.TaxonomyRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.TaxonomyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin CRUD for taxonomies. Paths (including the "/api" prefix, unusual in this backend)
 * are dictated by the existing frontend contract in
 * cambofreelance-web-frontend/src/modules/admin/taxonomy/core/actions.ts.
 */
@RestController
@RequestMapping("/api/taxonomies")
@RequiredArgsConstructor
public class TaxonomyController {

    private final TaxonomyService taxonomyService;

    @PostMapping
    @PreAuthorize("hasAuthority('taxonomy.view')")
    public ResponseEntity<Object> list(@RequestBody(required = false) BaseRequest request) {
        var result = taxonomyService.list(request != null ? request : new BaseRequest());
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('taxonomy.create')")
    public ResponseEntity<Object> create(@Valid @RequestBody TaxonomyRequest request) {
        var result = taxonomyService.create(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('taxonomy.update')")
    public ResponseEntity<Object> update(@Valid @RequestBody TaxonomyRequest request) {
        var result = taxonomyService.update(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('taxonomy.delete')")
    public ResponseEntity<Object> delete(@Valid @RequestBody TaxonomyDeleteRequest request) {
        taxonomyService.delete(request);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
