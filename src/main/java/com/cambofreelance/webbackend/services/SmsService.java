package com.cambofreelance.webbackend.services;

public interface SmsService {

    /** Starts an SMS phone-number verification. The provider generates and delivers the code itself. */
    void sendVerification(String phoneNumber);

    /** Checks a user-entered code against the provider. Returns true only if the provider approves it. */
    boolean checkVerification(String phoneNumber, String code);
}
