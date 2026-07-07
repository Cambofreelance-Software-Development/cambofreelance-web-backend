package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.CategoryHardwareRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.CategoryHardwareService;
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
public class CategoryHardwareController {

    private final CategoryHardwareService categoryHardwareService;

    // ── Public ────────────────────────────────────────────────────────────────

    @GetMapping("/hardware-categories")
    public ResponseEntity<Object> publicList() {
        var result = categoryHardwareService.listAll();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin (CMS) — reuses hardware.* permissions ─────────────────────────────

    @GetMapping("/cms/hardware-categories")
    @PreAuthorize("hasAuthority('hardware.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean all,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        if (Boolean.TRUE.equals(all) || search == null && page == 0) {
            var result = categoryHardwareService.listAll();
            return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
        }
        var result = categoryHardwareService.search(search, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/hardware-categories/{id}")
    @PreAuthorize("hasAuthority('hardware.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = categoryHardwareService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/hardware-categories")
    @PreAuthorize("hasAuthority('hardware.create')")
    public ResponseEntity<Object> create(
        @Valid @RequestBody CategoryHardwareRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = categoryHardwareService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/hardware-categories/{id}")
    @PreAuthorize("hasAuthority('hardware.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @Valid @RequestBody CategoryHardwareRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = categoryHardwareService.update(id, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/hardware-categories/{id}")
    @PreAuthorize("hasAuthority('hardware.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        categoryHardwareService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
