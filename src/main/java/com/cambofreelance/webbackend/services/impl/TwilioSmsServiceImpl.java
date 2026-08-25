package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.services.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "sms", name = "provider", havingValue = "twilio", matchIfMissing = true)
public class TwilioSmsServiceImpl implements SmsService {

    @Value("${sms.enabled:false}")
    private boolean enabled;

    @Value("${sms.twilio.account-sid:}")
    private String accountSid;

    @Value("${sms.twilio.auth-token:}")
    private String authToken;

    @Value("${sms.twilio.verify-service-sid:}")
    private String verifyServiceSid;

    @PostConstruct
    void init() {
        if (enabled) {
            Twilio.init(accountSid, authToken);
        }
    }

    @Override
    public void sendVerification(String phoneNumber) {
        if (!enabled) {
            log.info("[SMS disabled] Would start a Twilio Verify SMS check for {}", phoneNumber);
            return;
        }
        // Best-effort: failures are logged, not thrown, so a provider hiccup never aborts the caller's primary flow.
        try {
            Verification.creator(verifyServiceSid, phoneNumber, "sms").create();
            log.info("Twilio Verify SMS started for {}", phoneNumber);
        } catch (Exception e) {
            log.error("Failed to start Twilio Verify SMS for {}", phoneNumber, e);
        }
    }

    @Override
    public boolean checkVerification(String phoneNumber, String code) {
        if (!enabled) {
            // Dev mode: nothing was actually sent via Twilio, so there's no real code to check against.
            log.info("[SMS disabled] Rejecting phone OTP check for {} — no verification was sent", phoneNumber);
            return false;
        }
        try {
            VerificationCheck check = VerificationCheck.creator(verifyServiceSid)
                .setTo(phoneNumber)
                .setCode(code)
                .create();
            return "approved".equals(check.getStatus());
        } catch (Exception e) {
            log.warn("Twilio Verify check failed for {}: {}", phoneNumber, e.getMessage());
            return false;
        }
    }
}
