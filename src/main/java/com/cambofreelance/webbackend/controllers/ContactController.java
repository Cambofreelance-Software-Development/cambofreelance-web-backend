package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.dto.request.ContactRequest;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<Object> submitContact(@Valid @RequestBody ContactRequest request) {
        contactService.sendContactEmail(request);
        return new ResponseEntity<>(
            new MessageResponse("Your message has been sent successfully.", ErrorCode.SUCCESS),
            HttpStatus.OK
        );
    }
}
