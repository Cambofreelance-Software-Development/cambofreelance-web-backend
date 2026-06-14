package com.cambofreelance.authenticationservice.controllers;

import com.cambofreelance.authenticationservice.dto.request.ArticleTypeRequest;
import com.cambofreelance.authenticationservice.logger.contants.ErrorCode;
import com.cambofreelance.authenticationservice.logger.exceptions.MessageResponse;
import com.cambofreelance.authenticationservice.services.ArticleTypeService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/article-types")
@RequiredArgsConstructor
public class ArticleTypeController {

    private final ArticleTypeService articleTypeService;

    @GetMapping
    @PreAuthorize("hasAuthority('article-types.view')")
    public ResponseEntity<Object> list() {
        var result = articleTypeService.listActive();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('article-types.create')")
    public ResponseEntity<Object> create(@Valid @RequestBody ArticleTypeRequest request) {
        var result = articleTypeService.create(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('article-types.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @Valid @RequestBody ArticleTypeRequest request
    ) {
        var result = articleTypeService.update(id, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('article-types.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        articleTypeService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
