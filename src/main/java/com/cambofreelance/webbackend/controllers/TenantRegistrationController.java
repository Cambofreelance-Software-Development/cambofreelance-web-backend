package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.dto.request.TenantRegistrationRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Public self-service tenant registration — no authentication required. */
@RestController
@RequiredArgsConstructor
public class TenantRegistrationController {

    private final TenantService tenantService;

    @PostMapping("/tenants/register")
    public ResponseEntity<Object> register(@Valid @RequestBody TenantRegistrationRequest request) {
        var result = tenantService.register(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }
}
