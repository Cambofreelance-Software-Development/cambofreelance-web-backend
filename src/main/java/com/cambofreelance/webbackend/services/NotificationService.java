package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;

public interface NotificationService {

    Page<NotificationResponse> list(String type, Boolean isRead, int page, int size);

    long unreadCount();

    NotificationResponse markRead(String id, boolean read);

    String delete(String id);

    /** Raises a new admin-facing in-app notification. Used by other services/jobs. */
    void create(String type, String title, String message, String referenceId, String referenceType);
}
