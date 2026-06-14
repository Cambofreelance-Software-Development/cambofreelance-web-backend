package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.ArticleWorkflowStatus;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.ArticleCreateRequest;
import com.cambofreelance.webbackend.dto.request.ArticleStatusRequest;
import com.cambofreelance.webbackend.dto.request.ArticleUpdateRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.ArticleService;
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
public class ArticleController {

    private final ArticleService articleService;

    // ── Admin endpoints (require authentication) ──────────────────────────────

    @PostMapping("/cms/articles")
    @PreAuthorize("hasAuthority('articles.create')")
    public ResponseEntity<Object> create(
        @Valid @RequestBody ArticleCreateRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = articleService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/articles/{id}")
    @PreAuthorize("hasAuthority('articles.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @Valid @RequestBody ArticleUpdateRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = articleService.update(id, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/articles/{id}")
    @PreAuthorize("hasAuthority('articles.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        articleService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/articles/{id}/copy")
    @PreAuthorize("hasAuthority('articles.create')")
    public ResponseEntity<Object> copy(
        @PathVariable String id,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = articleService.copy(id, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/articles/{id}/status")
    @PreAuthorize("hasAuthority('articles.publish')")
    public ResponseEntity<Object> updateStatus(
        @PathVariable String id,
        @Valid @RequestBody ArticleStatusRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = articleService.updateStatus(id, request.getStatus(), userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/articles")
    @PreAuthorize("hasAuthority('articles.view')")
    public ResponseEntity<Object> adminList(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String workflowStatus,
        @RequestParam(required = false) String authorId,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = articleService.list(type, workflowStatus, authorId, search, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/articles/{id}")
    @PreAuthorize("hasAuthority('articles.view')")
    public ResponseEntity<Object> adminGetById(@PathVariable String id) {
        var result = articleService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Public endpoints (no authentication required) ─────────────────────────

    @GetMapping("/articles")
    public ResponseEntity<Object> publicList(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = articleService.list(type, ArticleWorkflowStatus.PUBLISHED, null, search, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<Object> publicGetById(@PathVariable String id) {
        var result = articleService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/articles/slug/{slug}")
    public ResponseEntity<Object> publicGetBySlug(@PathVariable String slug) {
        var result = articleService.getBySlug(slug);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
