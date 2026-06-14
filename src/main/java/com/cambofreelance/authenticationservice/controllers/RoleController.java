package com.cambofreelance.authenticationservice.controllers;

import com.cambofreelance.authenticationservice.dto.request.RoleCloneRequest;
import com.cambofreelance.authenticationservice.dto.request.RoleRequest;
import com.cambofreelance.authenticationservice.logger.contants.ErrorCode;
import com.cambofreelance.authenticationservice.logger.exceptions.MessageResponse;
import com.cambofreelance.authenticationservice.services.RoleService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/cms/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('roles.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Integer level,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = roleService.list(search, status, level, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('roles.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = roleService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('roles.create')")
    public ResponseEntity<Object> create(@Valid @RequestBody RoleRequest request) {
        var result = roleService.create(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('roles.update')")
    public ResponseEntity<Object> update(
        @PathVariable String id,
        @Valid @RequestBody RoleRequest request
    ) {
        var result = roleService.update(id, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('roles.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        roleService.delete(id);
        return new ResponseEntity<>(
            new MessageResponse("Role deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('roles.status')")
    public ResponseEntity<Object> toggleStatus(
        @PathVariable String id,
        @RequestBody Map<String, String> body
    ) {
        var result = roleService.toggleStatus(id, body.getOrDefault("status", ""));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{id}/clone")
    @PreAuthorize("hasAuthority('roles.clone')")
    public ResponseEntity<Object> clone(
        @PathVariable String id,
        @Valid @RequestBody RoleCloneRequest request
    ) {
        var result = roleService.clone(id, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('roles.permissions')")
    public ResponseEntity<Object> assignPermissions(
        @PathVariable String id,
        @RequestBody List<String> permissionIds
    ) {
        var result = roleService.assignPermissions(id, permissionIds);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('roles.view')")
    public ResponseEntity<Object> listPermissions() {
        var result = roleService.listPermissions();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
