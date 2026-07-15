package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.FaqRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.FaqService;
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
public class FaqController {

    private final FaqService faqService;

    // ── Public ────────────────────────────────────────────────────────────────

    @GetMapping("/faqs")
    public ResponseEntity<Object> publicList() {
        var result = faqService.listAll();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin (CMS) ───────────────────────────────────────────────────────────

    @GetMapping("/cms/faqs")
    @PreAuthorize("hasAuthority('faqs.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean all,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        if (Boolean.TRUE.equals(all) || search == null && page == 0) {
            var result = faqService.listAll();
            return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
        }
        var result = faqService.search(search, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/faqs/{id}")
    @PreAuthorize("hasAuthority('faqs.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = faqService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/faqs")
    @PreAuthorize("hasAuthority('faqs.create')")
    public ResponseEntity<Object> create(
        @Valid @RequestBody FaqRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = faqService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/faqs/{id}")
    @PreAuthorize("hasAuthority('faqs.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @Valid @RequestBody FaqRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = faqService.update(id, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/faqs/{id}")
    @PreAuthorize("hasAuthority('faqs.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        faqService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
