package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.dto.request.HelpCenterCategoryRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.HelpCenterCategoryService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelpCenterCategoryController {

    private final HelpCenterCategoryService categoryService;

    // ── Admin endpoints (require authentication) ──────────────────────────────

    @GetMapping("/cms/help-center-categories")
    @PreAuthorize("hasAuthority('help-center-categories.view')")
    public ResponseEntity<Object> adminList(@RequestParam(required = false) String articleTypeId) {
        var result = categoryService.listActive(articleTypeId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/help-center-categories")
    @PreAuthorize("hasAuthority('help-center-categories.create')")
    public ResponseEntity<Object> create(@Valid @RequestBody HelpCenterCategoryRequest request) {
        var result = categoryService.create(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/help-center-categories/{id}")
    @PreAuthorize("hasAuthority('help-center-categories.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @Valid @RequestBody HelpCenterCategoryRequest request
    ) {
        var result = categoryService.update(id, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/help-center-categories/{id}")
    @PreAuthorize("hasAuthority('help-center-categories.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        categoryService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Public endpoint (no authentication required) ───────────────────────────

    @GetMapping("/help-center-categories")
    public ResponseEntity<Object> publicList(
        @RequestParam(required = false) String articleTypeId,
        @RequestParam(required = false) String articleTypeCode
    ) {
        var result = articleTypeCode != null
            ? categoryService.listActiveByArticleTypeCode(articleTypeCode)
            : categoryService.listActive(articleTypeId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
