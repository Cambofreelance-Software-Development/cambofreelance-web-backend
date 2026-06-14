package com.cambofreelance.authenticationservice.controllers;

import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.logger.contants.ErrorCode;
import com.cambofreelance.authenticationservice.logger.exceptions.MessageResponse;
import com.cambofreelance.authenticationservice.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public ResponseEntity<Object> getMySessions(
        @RequestHeader(value = Constants.USER_ID) String userId,
        @RequestHeader(value = Constants.DEVICE_ID, required = false) String deviceId) {
        var sessions = sessionService.getMyActiveSessions(userId, deviceId);
        return new ResponseEntity<>(new MessageResponse(sessions, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Object> revokeMySession(
        @PathVariable String sessionId,
        @RequestHeader(value = Constants.USER_ID) String userId) {
        sessionService.revokeMySession(sessionId, userId);
        return new ResponseEntity<>(new MessageResponse("Session revoked successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> revokeAllOtherSessions(
        @RequestHeader(value = Constants.USER_ID) String userId,
        @RequestHeader(value = Constants.DEVICE_ID, required = false) String deviceId) {
        sessionService.revokeAllOtherSessions(userId, deviceId);
        return new ResponseEntity<>(new MessageResponse("All other sessions revoked successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
