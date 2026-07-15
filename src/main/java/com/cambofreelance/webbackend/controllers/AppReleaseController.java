package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.AppReleaseRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.AppReleaseService;
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
public class AppReleaseController {

    private final AppReleaseService appReleaseService;

    // ── Public ────────────────────────────────────────────────────────────────

    @GetMapping("/app-releases")
    public ResponseEntity<Object> publicList() {
        var result = appReleaseService.listAll();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/app-releases/latest")
    public ResponseEntity<Object> latest(@RequestParam String platform) {
        var result = appReleaseService.latestByPlatform(platform);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin (CMS) ───────────────────────────────────────────────────────────

    @GetMapping("/cms/app-releases")
    @PreAuthorize("hasAuthority('app_releases.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String platform,
        @RequestParam(required = false) Boolean all,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        if (Boolean.TRUE.equals(all)) {
            var result = appReleaseService.listAll();
            return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
        }
        var result = appReleaseService.search(search, platform, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/app-releases/{id}")
    @PreAuthorize("hasAuthority('app_releases.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = appReleaseService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/app-releases")
    @PreAuthorize("hasAuthority('app_releases.create')")
    public ResponseEntity<Object> create(
        @Valid @RequestBody AppReleaseRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = appReleaseService.create(request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/app-releases/{id}")
    @PreAuthorize("hasAuthority('app_releases.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @Valid @RequestBody AppReleaseRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var result = appReleaseService.update(id, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/app-releases/{id}")
    @PreAuthorize("hasAuthority('app_releases.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        appReleaseService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
