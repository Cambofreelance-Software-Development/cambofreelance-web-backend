package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.entities.CmsSettingEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CmsSettingRepository;
import com.cambofreelance.webbackend.services.EmailService;
import jakarta.mail.internet.InternetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender defaultMailSender;
    private final CmsSettingRepository settingRepository;

    @Override
    public void sendVerificationOtp(String to, String otp) {
        sendOrThrow(to, "Verify your email - SOPPOS",
            "Your email verification code is: " + otp + "\n\n"
                + "This code expires in 15 minutes. If you didn't request this, you can ignore this email.",
            ErrorCode.OTP_EMAIL_SEND_FAILED, "Failed to send the verification email. Please try again later.");
    }

    @Override
    public void sendPasswordResetOtp(String to, String otp) {
        sendOrThrow(to, "Reset your password - SOPPOS",
            "Your password reset code is: " + otp + "\n\n"
                + "This code expires in 15 minutes. If you didn't request this, you can ignore this email.",
            ErrorCode.OTP_EMAIL_SEND_FAILED, "Failed to send the verification email. Please try again later.");
    }

    @Override
    public void sendAutoRenewFailed(String to, String planName, Date expiresAt, int attempt, int maxAttempts) {
        send(to, "Auto-renew payment failed - SOPPOS",
            "We couldn't charge your card on file to renew your " + planName + " plan (attempt " + attempt
                + " of " + maxAttempts + ").\n\n"
                + "We'll try again before your subscription expires on " + fmt(expiresAt) + ". "
                + "You can also renew manually from your account at any time.");
    }

    @Override
    public void sendAutoRenewGaveUp(String to, String planName, Date expiresAt) {
        send(to, "Auto-renew turned off - SOPPOS",
            "We were unable to charge your card on file to renew your " + planName + " plan after "
                + "several attempts, so auto-renew has been turned off.\n\n"
                + "Your subscription remains active until " + fmt(expiresAt) + ". Please renew manually "
                + "before then to avoid any interruption.");
    }

    @Override
    public void sendAutoRenewSuccess(String to, String planName, Date newExpiresAt) {
        send(to, "Auto-renew successful - SOPPOS",
            "Your " + planName + " plan was automatically renewed. "
                + "Your subscription is now active until " + fmt(newExpiresAt) + ".");
    }

    @Override
    public void sendSubscriptionExpiringAlert(List<String> to, String customerUsername, String customerEmail,
            String planName, Date expiresAt, long daysRemaining) {
        if (to == null || to.isEmpty()) {
            return;
        }
        String dayWord = daysRemaining == 1 ? "day" : "days";
        send(to.toArray(new String[0]), "Subscription expiring in " + daysRemaining + " " + dayWord + " - SOPPOS",
            "Customer " + customerUsername + " (" + customerEmail + ") is subscribed to the " + planName
                + " plan, which expires on " + fmt(expiresAt) + ".\n\n"
                + "Consider reaching out to help them renew before it lapses.");
    }

    @Override
    public void sendTestEmail(String to) {
        sendOrThrow(to, "Test email - SOPPOS",
            "This is a test email from the CamboFreelance admin dashboard to confirm outgoing "
                + "email is configured correctly. If you received this, SMTP is working.",
            ErrorCode.TEST_EMAIL_SEND_FAILED, "Failed to send the test email. Check the SMTP configuration.");
    }

    private static String fmt(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    // Best-effort: failures are logged, not thrown, so an SMTP hiccup never aborts the caller's primary flow.
    private void send(String to, String subject, String body) {
        try {
            doSend(to, subject, body);
        } catch (Exception e) {
            log.error("Failed to send email to {} ({})", to, subject, e);
        }
    }

    // Same as send(String, ...) above, but for a notification going to multiple recipients at once
    // (e.g. every ADMIN/SUPER_ADMIN) — one SMTP round-trip instead of one send per recipient.
    private void send(String[] to, String subject, String body) {
        try {
            doSend(to, subject, body);
        } catch (Exception e) {
            log.error("Failed to send email to {} ({})", String.join(",", to), subject, e);
        }
    }

    // Shared by any caller whose primary flow depends on the email actually arriving (OTP, the
    // admin test-email check) — a swallowed SMTP failure here would mean the caller reports
    // success while the recipient never gets anything, so it must surface as an error instead.
    private void sendOrThrow(String to, String subject, String body, String errorCode, String errorMessage) {
        try {
            doSend(to, subject, body);
        } catch (Exception e) {
            log.error("Failed to send email to {} ({})", to, subject, e);
            AppException ex = new AppException(errorCode, errorMessage);
            ex.setHttpStatus(HttpStatus.BAD_GATEWAY);
            throw ex;
        }
    }

    private void doSend(String to, String subject, String body) throws Exception {
        JavaMailSender sender = getMailSender();
        var message = sender.createMimeMessage();
        var helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, false);
        applyFromAddress(helper);
        sender.send(message);
        log.info("Email sent to {} ({})", to, subject);
    }

    private void doSend(String[] to, String subject, String body) throws Exception {
        JavaMailSender sender = getMailSender();
        var message = sender.createMimeMessage();
        var helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, false);
        applyFromAddress(helper);
        sender.send(message);
        log.info("Email sent to {} ({})", String.join(",", to), subject);
    }

    private JavaMailSender getMailSender() {
        var hostOpt = settingRepository.findBySettingKey("smtp_host");
        if (hostOpt.isPresent() && hostOpt.get().getSettingValue() != null && !hostOpt.get().getSettingValue().isBlank()) {
            String host = hostOpt.get().getSettingValue().trim();
            String portStr = settingRepository.findBySettingKey("smtp_port").map(CmsSettingEntity::getSettingValue).orElse("587");
            int port = 587;
            try {
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException ignored) {}

            String username = settingRepository.findBySettingKey("smtp_username").map(CmsSettingEntity::getSettingValue).orElse("");
            String password = settingRepository.findBySettingKey("smtp_password").map(CmsSettingEntity::getSettingValue).orElse("");
            String encryption = settingRepository.findBySettingKey("smtp_encryption").map(CmsSettingEntity::getSettingValue).orElse("STARTTLS");
            String authStr = settingRepository.findBySettingKey("smtp_auth").map(CmsSettingEntity::getSettingValue).orElse("true");

            JavaMailSenderImpl impl = new JavaMailSenderImpl();
            impl.setHost(host);
            impl.setPort(port);
            if (!username.isBlank()) {
                impl.setUsername(username);
            }
            if (!password.isBlank()) {
                impl.setPassword(password);
            }
            impl.setDefaultEncoding("UTF-8");

            Properties props = impl.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", authStr);
            if ("SSL".equalsIgnoreCase(encryption) || "TLS".equalsIgnoreCase(encryption)) {
                props.put("mail.smtp.ssl.enable", "true");
            } else if ("STARTTLS".equalsIgnoreCase(encryption)) {
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.starttls.required", "true");
            } else {
                props.put("mail.smtp.starttls.enable", "false");
                props.put("mail.smtp.ssl.enable", "false");
            }
            props.put("mail.smtp.connectiontimeout", "10000");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.writetimeout", "10000");
            return impl;
        }
        return defaultMailSender;
    }

    private void applyFromAddress(MimeMessageHelper helper) throws Exception {
        String fromEmail = settingRepository.findBySettingKey("smtp_from_email")
            .map(CmsSettingEntity::getSettingValue)
            .filter(s -> !s.isBlank())
            .orElse(null);
        String fromName = settingRepository.findBySettingKey("smtp_from_name")
            .map(CmsSettingEntity::getSettingValue)
            .filter(s -> !s.isBlank())
            .orElse("SOPPOS");

        if (fromEmail != null) {
            helper.setFrom(new InternetAddress(fromEmail, fromName, "UTF-8"));
        }
    }
}
