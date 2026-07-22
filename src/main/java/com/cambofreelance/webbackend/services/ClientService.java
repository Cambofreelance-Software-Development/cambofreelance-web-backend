package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.CompanyInfoRequest;
import com.cambofreelance.webbackend.dto.response.ClientDetailResponse;
import com.cambofreelance.webbackend.dto.response.ClientResponse;
import org.springframework.data.domain.Page;

public interface ClientService {

    /** Returns (creating on first access) the caller's client profile with onboarding state. */
    ClientResponse getMyClient(String userId);

    ClientResponse updateCompanyInfo(String userId, CompanyInfoRequest request);

    /** Dev mode: returns the OTP directly (no SMTP configured). */
    String sendVerifyEmailOtp(String userId);

    ClientResponse confirmVerifyEmail(String userId, String otp);

    /** Called when a payment is approved — advances onboarding to DONE and activates the client. */
    void markOnboardingComplete(String userId);

    Page<ClientResponse> adminList(String search, String clientStatus, int page, int size);

    ClientDetailResponse adminDetail(String clientId);

    ClientResponse adminUpdateStatus(String clientId, String clientStatus, String adminId);
}
