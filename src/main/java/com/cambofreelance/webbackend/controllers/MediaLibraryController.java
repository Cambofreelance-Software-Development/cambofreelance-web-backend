package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.MediaLibraryAddRequest;
import com.cambofreelance.webbackend.dto.request.MediaLibraryUpdateRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.MediaLibraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tenant-scoped Media Library endpoints. Files are stored per-tenant via schema isolation —
 * the Tenant-Id header is validated here but not forwarded to the service layer, following
 * the same pattern as CustomerController.
 */
@RestController
@RequestMapping("/me/media-library")
@RequiredArgsConstructor
public class MediaLibraryController {

    private final MediaLibraryService mediaLibraryService;

    @PostMapping
    @PreAuthorize("hasAuthority('media-library.upload')")
    public ResponseEntity<Object> add(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @Valid @RequestBody MediaLibraryAddRequest request
    ) {
        requireTenantId(tenantId);
        var result = mediaLibraryService.add(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('media-library.view')")
    public ResponseEntity<Object> list(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) String folder,
        @RequestParam(required = false) String mediaType,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        requireTenantId(tenantId);
        var result = mediaLibraryService.list(folder, mediaType, search, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('media-library.view')")
    public ResponseEntity<Object> getById(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String id
    ) {
        requireTenantId(tenantId);
        var result = mediaLibraryService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('media-library.update')")
    public ResponseEntity<Object> update(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String id,
        @RequestBody MediaLibraryUpdateRequest request
    ) {
        requireTenantId(tenantId);
        var result = mediaLibraryService.update(id, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('media-library.delete')")
    public ResponseEntity<Object> delete(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String id
    ) {
        requireTenantId(tenantId);
        mediaLibraryService.delete(id, userId);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException("NO_TENANT_ASSIGNED", "Your account is not assigned to a tenant");
        }
    }
}
