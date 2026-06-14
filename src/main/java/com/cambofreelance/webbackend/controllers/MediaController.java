package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.MediaPresignRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.MediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/presign")
    @PreAuthorize("hasAuthority('media.upload')")
    public ResponseEntity<Object> presign(
        @Valid @RequestBody MediaPresignRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = mediaService.presign(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/confirm/{id}")
    @PreAuthorize("hasAuthority('media.upload')")
    public ResponseEntity<Object> confirm(@PathVariable String id) {
        var result = mediaService.confirmUpload(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('media.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String mediaType,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Page<?> result = mediaService.list(mediaType, search, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('media.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = mediaService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    /** Streams raw file bytes — whitelisted in SecurityConfig so no auth required. */
    @GetMapping("/{id}/view")
    public ResponseEntity<byte[]> view(@PathVariable String id) {
        return mediaService.viewFile(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('media.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        mediaService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
