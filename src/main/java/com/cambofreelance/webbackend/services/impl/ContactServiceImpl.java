package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.SettingGroup;
import com.cambofreelance.webbackend.dto.request.ContactRequest;
import com.cambofreelance.webbackend.dto.response.ContactMessageResponse;
import com.cambofreelance.webbackend.entities.CmsSettingEntity;
import com.cambofreelance.webbackend.entities.ContactMessageEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CmsSettingRepository;
import com.cambofreelance.webbackend.repository.ContactMessageRepository;
import com.cambofreelance.webbackend.services.ContactService;
import com.cambofreelance.webbackend.utils.ContactRateLimiter;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final JavaMailSender mailSender;
    private final CmsSettingRepository cmsSettingRepository;
    private final ContactMessageRepository contactMessageRepository;
    private final ContactRateLimiter rateLimiter;

    @Override
    @Transactional
    public void submit(ContactRequest request, String clientIp) {
        if (!rateLimiter.tryAcquire(clientIp)) {
            AppException ex = new AppException("TOO_MANY_REQUESTS",
                "Too many messages sent. Please try again later.");
            ex.setHttpStatus(HttpStatus.TOO_MANY_REQUESTS);
            throw ex;
        }

        ContactMessageEntity entity = new ContactMessageEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setFullName(request.getFullName().trim());
        entity.setEmail(request.getEmail().trim());
        entity.setSubject(request.getSubject().trim());
        entity.setMessage(request.getMessage().trim());
        entity.setIpAddress(clientIp);
        entity.setIsRead(false);
        entity.setCreatedBy(Constants.SYSTEM);
        contactMessageRepository.save(entity);

        // The message is persisted; email notification is best-effort only.
        try {
            sendContactEmail(request);
        } catch (Exception e) {
            log.error("Contact email notification failed (message {} is stored)", entity.getId(), e);
        }
    }

    @Override
    public Page<ContactMessageResponse> list(String search, Boolean isRead, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.toLowerCase().trim() + "%"
            : null;
        return contactMessageRepository
            .findFiltered(searchFilter, isRead, PageRequest.of(page, size))
            .map(ContactMessageResponse::from);
    }

    @Override
    public long unreadCount() {
        return contactMessageRepository.countByIsReadFalseAndStatusNot(Constants.STATUS_DELETE);
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "CONTACT_MESSAGE", entityClass = ContactMessageEntity.class)
    public ContactMessageResponse markRead(String id, boolean read) {
        ContactMessageEntity entity = getActive(id);
        entity.setIsRead(read);
        entity.setUpdatedAt(new Date());
        return ContactMessageResponse.from(contactMessageRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "CONTACT_MESSAGE", entityClass = ContactMessageEntity.class)
    public String delete(String id) {
        ContactMessageEntity entity = getActive(id);
        entity.setStatus(Constants.STATUS_DELETE);
        entity.setUpdatedAt(new Date());
        contactMessageRepository.save(entity);
        return "Contact message deleted.";
    }

    private ContactMessageEntity getActive(String id) {
        return contactMessageRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("NOT_FOUND", "Contact message not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }

    private void sendContactEmail(ContactRequest request) throws Exception {
        String recipientEmail = loadSiteEmail();
        if (!StringUtils.hasText(recipientEmail)) {
            log.warn("No site_email configured; skipping contact email notification");
            return;
        }

        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setReplyTo(request.getEmail());
        helper.setSubject("[Contact Form] " + request.getSubject());
        helper.setText(buildBody(request), false);

        mailSender.send(message);
        log.info("Contact email sent from {} to {}", request.getEmail(), recipientEmail);
    }

    private String loadSiteEmail() {
        List<CmsSettingEntity> rows = cmsSettingRepository.findAllBySettingGroup(SettingGroup.GENERAL);
        Map<String, String> settings = rows.stream()
            .collect(Collectors.toMap(CmsSettingEntity::getSettingKey, CmsSettingEntity::getSettingValue));
        return settings.getOrDefault("site_email", "");
    }

    private String buildBody(ContactRequest req) {
        return "New contact form submission\n\n" +
               "Name:    " + req.getFullName() + "\n" +
               "Email:   " + req.getEmail() + "\n" +
               "Subject: " + req.getSubject() + "\n\n" +
               "Message:\n" + req.getMessage();
    }
}
