package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.caches.PhoneOtpCache;
import com.cambofreelance.webbackend.services.SmsService;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * PlasGate's REST API only sends text — unlike Twilio Verify, it has no concept of a
 * verification session. So this implementation owns the OTP lifecycle itself: generate,
 * cache in {@link PhoneOtpCache}, text it as plain SMS content, then compare on check.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "sms", name = "provider", havingValue = "plasgate")
public class PlasgateSmsServiceImpl implements SmsService {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final WebClient webClient;
    private final PhoneOtpCache phoneOtpCache;

    @Value("${sms.enabled:false}")
    private boolean enabled;

    /** Full send-message endpoint URL (PlasGate's docs call it "/rest/send" — configured whole, not appended). */
    @Value("${sms.plasgate.base-url:https://cloudapi.plasgate.com/rest/send}")
    private String baseUrl;

    @Value("${sms.plasgate.private-key:}")
    private String privateKey;

    @Value("${sms.plasgate.secret:}")
    private String secret;

    @Value("${sms.plasgate.sender:}")
    private String sender;

    @Override
    public void sendVerification(String phoneNumber) {
        if (!enabled) {
            log.info("[SMS disabled] Would send a PlasGate SMS OTP to {}", phoneNumber);
            return;
        }
        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
        phoneOtpCache.store(phoneNumber, otp);
        // Best-effort: failures are logged, not thrown, so a provider hiccup never aborts the caller's primary flow.
        try {
            Map<String, String> body = Map.of(
                "sender", sender,
                "to", normalize(phoneNumber),
                "content", "Your CamboFreelance verification code is " + otp
            );
            webClient.post()
                .uri(baseUrl + "?private_key=" + privateKey)
                .header("X-Secret", secret)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();
            log.info("PlasGate SMS OTP sent to {}", phoneNumber);
        } catch (Exception e) {
            log.error("Failed to send PlasGate SMS OTP to {}", phoneNumber, e);
        }
    }

    @Override
    public boolean checkVerification(String phoneNumber, String code) {
        if (!enabled) {
            // Dev mode: nothing was actually sent via PlasGate, so there's no real code to check against.
            log.info("[SMS disabled] Rejecting phone OTP check for {} — no verification was sent", phoneNumber);
            return false;
        }
        String stored = phoneOtpCache.get(phoneNumber);
        if (stored == null || !stored.equals(code)) {
            return false;
        }
        phoneOtpCache.delete(phoneNumber);
        return true;
    }

    /** PlasGate expects bare digits (e.g. "85512345678"), not a leading '+'. */
    private static String normalize(String phoneNumber) {
        return phoneNumber == null ? null : phoneNumber.replaceFirst("^\\+", "");
    }
}
