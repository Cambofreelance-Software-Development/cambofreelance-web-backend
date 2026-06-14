package com.cambofreelance.authenticationservice.services.impl;

import com.cambofreelance.authenticationservice.constants.SettingGroup;
import com.cambofreelance.authenticationservice.dto.request.ContactRequest;
import com.cambofreelance.authenticationservice.entities.CmsSettingEntity;
import com.cambofreelance.authenticationservice.repository.CmsSettingRepository;
import com.cambofreelance.authenticationservice.services.ContactService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final JavaMailSender mailSender;
    private final CmsSettingRepository cmsSettingRepository;

    @Override
    public void sendContactEmail(ContactRequest request) {
        String recipientEmail = loadSiteEmail();

        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(recipientEmail);
            helper.setReplyTo(request.getEmail());
            helper.setSubject("[Contact Form] " + request.getSubject());
            helper.setText(buildBody(request), false);

            mailSender.send(message);
            log.info("Contact email sent from {} to {}", request.getEmail(), recipientEmail);
        } catch (Exception e) {
            log.error("Failed to send contact email", e);
            throw new RuntimeException("Failed to send email. Please try again later.");
        }
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
