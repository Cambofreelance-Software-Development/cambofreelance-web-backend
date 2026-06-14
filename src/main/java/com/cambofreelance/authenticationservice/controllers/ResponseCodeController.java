package com.cambofreelance.authenticationservice.controllers;

import com.cambofreelance.authenticationservice.dto.request.ResponseCodeRequest;
import com.cambofreelance.authenticationservice.logger.contants.ErrorCode;
import com.cambofreelance.authenticationservice.logger.exceptions.MessageResponse;
import com.cambofreelance.authenticationservice.services.ResponseCodeService;
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
@RequestMapping("/cms/response-codes")
@RequiredArgsConstructor
public class ResponseCodeController {

    private final ResponseCodeService responseCodeService;

    @GetMapping
    @PreAuthorize("hasAuthority('response-codes.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String serviceType,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = responseCodeService.list(search, type, status, serviceType, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('response-codes.view')")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        var result = responseCodeService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('response-codes.create')")
    public ResponseEntity<Object> create(@Valid @RequestBody ResponseCodeRequest request) {
        var result = responseCodeService.create(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('response-codes.update')")
    public ResponseEntity<Object> update(
        @PathVariable Long id,
        @Valid @RequestBody ResponseCodeRequest request
    ) {
        var result = responseCodeService.update(id, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('response-codes.delete')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        responseCodeService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/reload-cache")
    @PreAuthorize("hasAuthority('response-codes.manage')")
    public ResponseEntity<Object> reloadCache() {
        responseCodeService.reloadCache();
        return new ResponseEntity<>(
            new MessageResponse("Cache reloaded successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
