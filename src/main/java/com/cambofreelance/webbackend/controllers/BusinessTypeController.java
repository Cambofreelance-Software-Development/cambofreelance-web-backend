package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.BusinessTypeGroupRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.BusinessTypeService;
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
public class BusinessTypeController {

    private final BusinessTypeService businessTypeService;

    // ── Public ────────────────────────────────────────────────────────────────

    @GetMapping("/business-types")
    public ResponseEntity<Object> publicList() {
        var result = businessTypeService.listAll();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin (CMS) ───────────────────────────────────────────────────────────

    @GetMapping("/cms/business-types")
    @PreAuthorize("hasAuthority('business_types.view')")
    public ResponseEntity<Object> list() {
        var result = businessTypeService.listAll();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/business-types/{id}")
    @PreAuthorize("hasAuthority('business_types.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = businessTypeService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/business-types")
    @PreAuthorize("hasAuthority('business_types.create')")
    public ResponseEntity<Object> create(
        @Valid @RequestBody BusinessTypeGroupRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = businessTypeService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/business-types/{id}")
    @PreAuthorize("hasAuthority('business_types.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @Valid @RequestBody BusinessTypeGroupRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = businessTypeService.update(id, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/business-types/{id}")
    @PreAuthorize("hasAuthority('business_types.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        businessTypeService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
