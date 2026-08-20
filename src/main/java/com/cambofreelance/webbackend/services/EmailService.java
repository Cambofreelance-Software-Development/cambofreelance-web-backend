package com.cambofreelance.webbackend.services;

import java.util.Date;

public interface EmailService {

    /** Sends the email-verification OTP shown during client onboarding. */
    void sendVerificationOtp(String to, String otp);

    /** Sends the password-reset OTP. */
    void sendPasswordResetOtp(String to, String otp);

    /** An auto-renew charge attempt failed but the job will retry before {@code expiresAt}. */
    void sendAutoRenewFailed(String to, String planName, Date expiresAt, int attempt, int maxAttempts);

    /** Auto-renew gave up after repeated failures and was turned off; the subscription will lapse at expiresAt. */
    void sendAutoRenewGaveUp(String to, String planName, Date expiresAt);

    /** An auto-renew charge succeeded. Unreachable until PaywayClient#chargeStoredToken is implemented. */
    void sendAutoRenewSuccess(String to, String planName, Date newExpiresAt);
}
