package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.dto.request.ContactRequest;

public interface ContactService {
    void sendContactEmail(ContactRequest request);
}
