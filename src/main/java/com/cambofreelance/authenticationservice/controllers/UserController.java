package com.cambofreelance.authenticationservice.controllers;

import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.dto.UserPreferencesDto;
import com.cambofreelance.authenticationservice.dto.request.AdminUserCreateRequest;
import com.cambofreelance.authenticationservice.dto.request.AdminUserUpdateRequest;
import com.cambofreelance.authenticationservice.dto.request.ChangePasswordRequest;
import com.cambofreelance.authenticationservice.dto.request.UpdateProfileRequest;
import com.cambofreelance.authenticationservice.dto.request.UserStatusRequest;
import com.cambofreelance.authenticationservice.logger.contants.ErrorCode;
import com.cambofreelance.authenticationservice.logger.exceptions.MessageResponse;
import com.cambofreelance.authenticationservice.services.SessionService;
import com.cambofreelance.authenticationservice.services.UserPreferenceService;
import com.cambofreelance.authenticationservice.services.UserService;
import jakarta.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;
    private final UserPreferenceService preferenceService;

    // ── User list & roles ────────────────────────────────────────────────────

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('users.view')")
    public ResponseEntity<Object> listUsers(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String roleId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        var result = userService.getUserList(search, status, roleId, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAnyAuthority('users.create', 'users.update', 'roles.view')")
    public ResponseEntity<Object> listRoles() {
        var roles = userService.getAllRoles();
        return new ResponseEntity<>(new MessageResponse(roles, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin CRUD ───────────────────────────────────────────────────────────

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('users.create')")
    public ResponseEntity<Object> createUser(
        @Valid @RequestBody AdminUserCreateRequest request
    ) {
        var result = userService.adminCreateUser(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('users.update')")
    public ResponseEntity<Object> updateUser(
        @PathVariable String userId,
        @Valid @RequestBody AdminUserUpdateRequest request
    ) {
        var result = userService.adminUpdateUser(userId, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('users.delete')")
    public ResponseEntity<Object> deleteUser(@PathVariable String userId) {
        userService.adminDeleteUser(userId);
        return new ResponseEntity<>(new MessageResponse("User deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/users/{userId}/status")
    @PreAuthorize("hasAuthority('users.status')")
    public ResponseEntity<Object> updateUserStatus(
        @PathVariable String userId,
        @Valid @RequestBody UserStatusRequest request
    ) {
        var result = userService.adminUpdateUserStatus(userId, request.getStatus());
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Current user permissions ─────────────────────────────────────────────

    @GetMapping("/user/permissions")
    public ResponseEntity<Object> getMyPermissions() {
        Set<String> codes = SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(a -> !a.startsWith("ROLE_"))
            .collect(Collectors.toSet());
        return new ResponseEntity<>(new MessageResponse(codes, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Current user preferences ─────────────────────────────────────────────

    @GetMapping("/user/preferences")
    public ResponseEntity<Object> getMyPreferences(
        @RequestHeader(value = Constants.USER_ID) String userId
    ) {
        var prefs = preferenceService.getPreferences(userId);
        return new ResponseEntity<>(new MessageResponse(prefs, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/user/preferences")
    public ResponseEntity<Object> saveMyPreferences(
        @RequestHeader(value = Constants.USER_ID) String userId,
        @RequestBody UserPreferencesDto dto
    ) {
        var saved = preferenceService.savePreferences(userId, dto);
        return new ResponseEntity<>(new MessageResponse(saved, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Current user profile ─────────────────────────────────────────────────

    @GetMapping("/user/profile")
    public ResponseEntity<Object> getProfile(
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var profile = userService.getUserProfile(userId);
        return new ResponseEntity<>(new MessageResponse(profile, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/user/profile")
    public ResponseEntity<Object> updateProfile(
        @RequestHeader(value = Constants.USER_ID) String userId,
        @Valid @RequestBody UpdateProfileRequest request
    ) {
        var updated = userService.updateProfile(userId, request);
        return new ResponseEntity<>(new MessageResponse(updated, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/user/password")
    public ResponseEntity<Object> changePassword(
        @RequestHeader(value = Constants.USER_ID) String userId,
        @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(userId, request);
        return new ResponseEntity<>(new MessageResponse("", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin session management ──────────────────────────────────────────────

    @GetMapping("/cms/users/{userId}/sessions")
    @PreAuthorize("hasAuthority('sessions.view')")
    public ResponseEntity<Object> adminGetUserSessions(@PathVariable String userId) {
        var sessions = sessionService.adminGetUserSessions(userId);
        return new ResponseEntity<>(new MessageResponse(sessions, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/users/{userId}/sessions/{sessionId}")
    @PreAuthorize("hasAuthority('sessions.revoke')")
    public ResponseEntity<Object> adminRevokeSession(
        @PathVariable String userId,
        @PathVariable String sessionId) {
        sessionService.adminRevokeSession(sessionId);
        return new ResponseEntity<>(new MessageResponse("Session revoked successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/users/{userId}/sessions")
    @PreAuthorize("hasAuthority('sessions.revoke')")
    public ResponseEntity<Object> adminRevokeAllSessions(@PathVariable String userId) {
        sessionService.adminRevokeAllSessions(userId);
        return new ResponseEntity<>(new MessageResponse("All sessions revoked successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
