package com.cambofreelance.authenticationservice.controllers;

import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.constants.ErrorCode;
import com.cambofreelance.authenticationservice.dto.request.ForgotPasswordRequest;
import com.cambofreelance.authenticationservice.dto.request.OAuthRequest;
import com.cambofreelance.authenticationservice.dto.request.ResetPasswordRequest;
import com.cambofreelance.authenticationservice.dto.request.UserRegisterRequest;
import com.cambofreelance.authenticationservice.dto.response.OAuthResponse;
import com.cambofreelance.authenticationservice.logger.exceptions.MessageResponse;
import com.cambofreelance.authenticationservice.services.OAuthAuthenticator;
import com.cambofreelance.authenticationservice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final OAuthAuthenticator authenticator;
    private final UserService userService;

    @PostMapping("/token")
    public ResponseEntity<Object> oauthToken(@RequestBody OAuthRequest request,
        @RequestHeader(value = Constants.CLIENT_LANG, required = false) String userLang,
        HttpServletRequest httpRequest) {
        log.info("Request for token is {}", request);
        OAuthResponse response = authenticator.createToken(request, httpRequest);
        MessageResponse messageResponse = new MessageResponse(response, ErrorCode.LOGIN_SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserRegisterRequest request,
        @RequestHeader(value = Constants.CLIENT_LANG, required = false) String userLang) {
        log.info("Request for register user {}", request);
        userService.registerUser(request);
        MessageResponse messageResponse = new MessageResponse("", ErrorCode.LOGIN_SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String otp = userService.forgotPassword(request);
        // otp is null when email not found — return same response for security
        Object data = (otp != null) ? java.util.Map.of("otp", otp) : "";
        return new ResponseEntity<>(new MessageResponse(data, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return new ResponseEntity<>(new MessageResponse("Password reset successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(
        @RequestHeader(value = Constants.TOKEN_HEADER) String authorizationHeader,
        @RequestHeader(value = Constants.USER_ID) String userId,
        @RequestHeader(value = Constants.DEVICE_ID, required = false) String deviceId) {
        String accessToken = authorizationHeader.startsWith(Constants.BEARER + " ")
            ? authorizationHeader.substring(Constants.BEARER.length() + 1)
            : authorizationHeader;
        log.info("Logout request for userId={} deviceId={}", userId, deviceId);
        authenticator.revokeToken(accessToken, userId, deviceId);
        MessageResponse messageResponse = new MessageResponse("", ErrorCode.LOGIN_SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
