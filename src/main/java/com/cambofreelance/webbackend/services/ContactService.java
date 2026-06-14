package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.ContactRequest;

public interface ContactService {
    void sendContactEmail(ContactRequest request);
}
