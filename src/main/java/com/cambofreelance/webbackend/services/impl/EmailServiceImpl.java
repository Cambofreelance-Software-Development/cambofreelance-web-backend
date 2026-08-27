package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.services.EmailService;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationOtp(String to, String otp) {
        sendOrThrow(to, "Verify your email - SOPPOS",
            "Your email verification code is: " + otp + "\n\n"
                + "This code expires in 15 minutes. If you didn't request this, you can ignore this email.");
    }

    @Override
    public void sendPasswordResetOtp(String to, String otp) {
        sendOrThrow(to, "Reset your password - SOPPOS",
            "Your password reset code is: " + otp + "\n\n"
                + "This code expires in 15 minutes. If you didn't request this, you can ignore this email.");
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

    // The OTP itself is the primary flow, so a swallowed SMTP failure here would mean the
    // caller reports success while the user never receives a usable code — must surface as an error.
    private void sendOrThrow(String to, String subject, String body) {
        try {
            doSend(to, subject, body);
        } catch (Exception e) {
            log.error("Failed to send email to {} ({})", to, subject, e);
            AppException ex = new AppException(ErrorCode.OTP_EMAIL_SEND_FAILED,
                "Failed to send the verification email. Please try again later.");
            ex.setHttpStatus(HttpStatus.BAD_GATEWAY);
            throw ex;
        }
    }

    private void doSend(String to, String subject, String body) throws Exception {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, false);
        mailSender.send(message);
        log.info("Email sent to {} ({})", to, subject);
    }
}
