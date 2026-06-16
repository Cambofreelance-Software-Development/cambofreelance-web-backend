package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.SubscriptionPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Public plan listing for the tenant self-registration page — no authentication required. */
@RestController
@RequiredArgsConstructor
public class PublicSubscriptionPackageController {

    private final SubscriptionPackageService packageService;

    @GetMapping("/packages/active")
    public ResponseEntity<Object> listActive() {
        var result = packageService.listActive();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
