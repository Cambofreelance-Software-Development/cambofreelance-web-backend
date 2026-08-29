package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.dto.response.NotificationResponse;
import com.cambofreelance.webbackend.entities.AdminNotificationEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.AdminNotificationRepository;
import com.cambofreelance.webbackend.services.NotificationService;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final AdminNotificationRepository notificationRepository;

    @Override
    public Page<NotificationResponse> list(String type, Boolean isRead, int page, int size) {
        return notificationRepository
            .findFiltered(isRead, type, PageRequest.of(page, size))
            .map(NotificationResponse::from);
    }

    @Override
    public long unreadCount() {
        return notificationRepository.countByIsReadFalseAndStatusNot(Constants.STATUS_DELETE);
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "NOTIFICATION", entityClass = AdminNotificationEntity.class)
    public NotificationResponse markRead(String id, boolean read) {
        AdminNotificationEntity entity = getActive(id);
        entity.setIsRead(read);
        entity.setUpdatedAt(new Date());
        return NotificationResponse.from(notificationRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "NOTIFICATION", entityClass = AdminNotificationEntity.class)
    public String delete(String id) {
        AdminNotificationEntity entity = getActive(id);
        entity.setStatus(Constants.STATUS_DELETE);
        entity.setUpdatedAt(new Date());
        notificationRepository.save(entity);
        return "Notification deleted.";
    }

    @Override
    @Transactional
    public void create(String type, String title, String message, String referenceId, String referenceType) {
        AdminNotificationEntity entity = new AdminNotificationEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setType(type);
        entity.setTitle(title);
        entity.setMessage(message);
        entity.setReferenceId(referenceId);
        entity.setReferenceType(referenceType);
        entity.setIsRead(false);
        entity.setCreatedBy(Constants.SYSTEM);
        notificationRepository.save(entity);
    }

    private AdminNotificationEntity getActive(String id) {
        return notificationRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND, "Notification not found: " + id));
    }
}
