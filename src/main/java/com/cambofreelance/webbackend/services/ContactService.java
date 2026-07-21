package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.ContactRequest;
import com.cambofreelance.webbackend.dto.response.ContactMessageResponse;
import org.springframework.data.domain.Page;

public interface ContactService {

    void submit(ContactRequest request, String clientIp);

    Page<ContactMessageResponse> list(String search, Boolean isRead, int page, int size);

    long unreadCount();

    ContactMessageResponse markRead(String id, boolean read);

    String delete(String id);
}
