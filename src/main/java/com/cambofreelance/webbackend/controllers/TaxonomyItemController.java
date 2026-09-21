package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.request.TaxonomyItemDeleteRequest;
import com.cambofreelance.webbackend.dto.request.TaxonomyItemRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.TaxonomyItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin CRUD for taxonomy items, PLUS one public read-only endpoint used by public forms
 * (e.g. Partner Application) to populate <select> options — GET /taxonomy-items (no "/api"
 * prefix, no auth: see SecurityConfig's permitAll matcher).
 *
 * Admin paths are dictated by the existing frontend contract in
 * cambofreelance-web-frontend/src/modules/admin/taxonomy/core/actions.ts.
 */
@RestController
@RequiredArgsConstructor
public class TaxonomyItemController {

    private final TaxonomyItemService taxonomyItemService;

    // ── Public ────────────────────────────────────────────────────────────────

    @GetMapping("/taxonomy-items")
    public ResponseEntity<Object> publicList(
        @RequestParam String taxonomyCode,
        @RequestParam(required = false) String parentCode) {
        var result = taxonomyItemService.listPublicByCode(taxonomyCode, parentCode);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin ─────────────────────────────────────────────────────────────────

    @PostMapping("/api/taxonomies/taxonomy-items")
    @PreAuthorize("hasAuthority('taxonomy-item.view')")
    public ResponseEntity<Object> list(@RequestBody(required = false) BaseRequest request) {
        var result = taxonomyItemService.list(request != null ? request : new BaseRequest());
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/api/taxonomies/taxonomy-items/create")
    @PreAuthorize("hasAuthority('taxonomy-item.create')")
    public ResponseEntity<Object> create(@Valid @RequestBody TaxonomyItemRequest request) {
        var result = taxonomyItemService.create(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PostMapping("/api/taxonomies/taxonomy-items/update")
    @PreAuthorize("hasAuthority('taxonomy-item.update')")
    public ResponseEntity<Object> update(@Valid @RequestBody TaxonomyItemRequest request) {
        var result = taxonomyItemService.update(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/api/taxonomies/taxonomy-items/delete")
    @PreAuthorize("hasAuthority('taxonomy-item.delete')")
    public ResponseEntity<Object> delete(@Valid @RequestBody TaxonomyItemDeleteRequest request) {
        taxonomyItemService.delete(request);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
