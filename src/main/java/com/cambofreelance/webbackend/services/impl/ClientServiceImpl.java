package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.caches.EmailVerifyCache;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.dto.request.CompanyInfoRequest;
import com.cambofreelance.webbackend.dto.response.ClientDetailResponse;
import com.cambofreelance.webbackend.dto.response.ClientResponse;
import com.cambofreelance.webbackend.dto.response.InvoiceResponse;
import com.cambofreelance.webbackend.dto.response.PaymentEventResponse;
import com.cambofreelance.webbackend.entities.ClientEntity;
import com.cambofreelance.webbackend.entities.PaymentTransactionEntity;
import com.cambofreelance.webbackend.entities.UserEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.ClientRepository;
import com.cambofreelance.webbackend.repository.InvoiceRepository;
import com.cambofreelance.webbackend.repository.PaymentEventLogRepository;
import com.cambofreelance.webbackend.repository.PaymentTransactionRepository;
import com.cambofreelance.webbackend.repository.UserRepository;
import com.cambofreelance.webbackend.repository.UserSubscriptionRepository;
import com.cambofreelance.webbackend.services.BillingService;
import com.cambofreelance.webbackend.services.ClientService;
import com.cambofreelance.webbackend.services.EmailService;
import com.cambofreelance.webbackend.services.SubscriptionService;
import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final UserSubscriptionRepository subscriptionRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final PaymentEventLogRepository eventLogRepository;
    private final InvoiceRepository invoiceRepository;
    private final EmailVerifyCache emailVerifyCache;
    private final EmailService emailService;
    private final SubscriptionService subscriptionService;
    private final BillingService billingService;
    private final SecureRandom secureRandom = new SecureRandom();

    // ── Client self-service / onboarding ────────────────────────────────────

    @Override
    @Transactional
    public ClientResponse getMyClient(String userId) {
        return toResponse(getOrCreate(userId));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "CLIENT")
    public ClientResponse updateCompanyInfo(String userId, CompanyInfoRequest r) {
        ClientEntity c = getOrCreate(userId);
        c.setCompanyName(r.getCompanyName());
        c.setCompanyEmail(r.getCompanyEmail());
        c.setCompanyPhone(r.getCompanyPhone());
        c.setAddress(r.getAddress());
        c.setCity(r.getCity());
        c.setCountry(r.getCountry());
        c.setBusinessType(r.getBusinessType());
        if (StringUtils.hasText(r.getLogoUrl())) {
            c.setLogoUrl(r.getLogoUrl());
        }
        if (Constants.STEP_COMPANY_INFO.equals(c.getOnboardingStep())) {
            c.setOnboardingStep(Constants.STEP_SELECT_PACKAGE);
        }
        c.setUpdatedBy(userId);
        c.setUpdatedAt(new Date());
        clientRepository.save(c);
        return toResponse(c);
    }

    @Override
    public void sendVerifyEmailOtp(String userId) {
        UserEntity user = requireUser(userId);
        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        emailVerifyCache.store(userId, otp);
        emailService.sendVerificationOtp(user.getEmail(), otp);
        log.info("[EmailVerify] OTP sent to {} ({})", user.getUsername(), user.getEmail());
    }

    @Override
    @Transactional
    public ClientResponse confirmVerifyEmail(String userId, String otp) {
        String cached = emailVerifyCache.get(userId);
        if (cached == null || !cached.equals(otp)) {
            throw new AppException(ErrorCode.INVALID_OTP, "Invalid or expired verification code");
        }
        emailVerifyCache.delete(userId);
        ClientEntity c = getOrCreate(userId);
        c.setEmailVerified(true);
        if (Constants.STEP_VERIFY_EMAIL.equals(c.getOnboardingStep())) {
            c.setOnboardingStep(Constants.STEP_COMPANY_INFO);
        }
        c.setUpdatedBy(userId);
        c.setUpdatedAt(new Date());
        clientRepository.save(c);
        return toResponse(c);
    }

    @Override
    @Transactional
    public void markOnboardingComplete(String userId) {
        clientRepository.findByUserId(userId).ifPresent(c -> {
            c.setOnboardingStep(Constants.STEP_DONE);
            if (Constants.CLIENT_PENDING.equals(c.getClientStatus())) {
                c.setClientStatus(Constants.CLIENT_ACTIVE);
            }
            c.setUpdatedAt(new Date());
            c.setUpdatedBy(Constants.SYSTEM);
            clientRepository.save(c);
        });
    }

    // ── Admin ───────────────────────────────────────────────────────────────

    @Override
    public Page<ClientResponse> adminList(String search, String clientStatus, int page, int size) {
        String s = StringUtils.hasText(search) ? "%" + search.toLowerCase() + "%" : null;
        String st = StringUtils.hasText(clientStatus) ? clientStatus : null;
        return clientRepository.search(s, st, PageRequest.of(page, size)).map(this::toResponse);
    }

    @Override
    public ClientDetailResponse adminDetail(String clientId) {
        ClientEntity c = clientRepository.findById(clientId)
            .orElseThrow(() -> {
                AppException ex = new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "Client not found");
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });

        List<PaymentTransactionEntity> txs = transactionRepository.findByUserIdOrderByCreatedAtDesc(c.getUserId());
        java.util.Map<String, String> actorNames = new java.util.HashMap<>();
        List<PaymentEventResponse> activity = txs.isEmpty() ? List.of()
            : eventLogRepository.findTop20ByTransactionIdInOrderByCreatedAtDesc(
                    txs.stream().map(PaymentTransactionEntity::getId).toList())
                .stream()
                .map(e -> PaymentEventResponse.builder()
                    .transactionId(e.getTransactionId())
                    .fromStatus(e.getFromStatus())
                    .toStatus(e.getToStatus())
                    .source(e.getSource())
                    .note(e.getNote())
                    .actor(e.getActor())
                    .actorName(resolveActorName(e.getActor(), actorNames))
                    .createdAt(e.getCreatedAt())
                    .build())
                .toList();

        List<InvoiceResponse> invoices = billingService.listMyInvoices(c.getUserId());

        return ClientDetailResponse.builder()
            .client(toResponse(c))
            .subscriptions(subscriptionService.getMySubscription(c.getUserId()).getHistory())
            .payments(subscriptionService.getMyPayments(c.getUserId()))
            .invoices(invoices)
            .activity(activity)
            .build();
    }

    @Override
    @Transactional
    @Auditable(action = "STATUS_CHANGE", module = "CLIENT", entityClass = ClientEntity.class)
    public ClientResponse adminUpdateStatus(String clientId, String clientStatus, String adminId) {
        if (!List.of(Constants.CLIENT_PENDING, Constants.CLIENT_ACTIVE,
                Constants.CLIENT_SUSPENDED, Constants.CLIENT_CLOSED).contains(clientStatus)) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Invalid client status value");
        }
        ClientEntity c = clientRepository.findById(clientId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "Client not found"));
        c.setClientStatus(clientStatus);
        c.setUpdatedBy(adminId);
        c.setUpdatedAt(new Date());
        clientRepository.save(c);
        return toResponse(c);
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "CLIENT", entityClass = ClientEntity.class)
    public ClientResponse adminUpdateCompanyInfo(String clientId, CompanyInfoRequest r, String adminId) {
        ClientEntity c = clientRepository.findById(clientId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "Client not found"));
        c.setCompanyName(r.getCompanyName());
        c.setCompanyEmail(r.getCompanyEmail());
        c.setCompanyPhone(r.getCompanyPhone());
        c.setAddress(r.getAddress());
        c.setCity(r.getCity());
        c.setCountry(r.getCountry());
        c.setBusinessType(r.getBusinessType());
        if (StringUtils.hasText(r.getLogoUrl())) {
            c.setLogoUrl(r.getLogoUrl());
        }
        // Admin edits are a correction/maintenance action, not an onboarding step —
        // unlike the client's own updateCompanyInfo(), this never advances onboardingStep.
        c.setUpdatedBy(adminId);
        c.setUpdatedAt(new Date());
        clientRepository.save(c);
        return toResponse(c);
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private ClientEntity getOrCreate(String userId) {
        UserEntity user = requireUser(userId);
        ClientEntity c = clientRepository.findByUserId(userId).orElseGet(() -> {
            ClientEntity created = new ClientEntity();
            created.setId(UUID.randomUUID().toString());
            created.setUserId(userId);
            created.setCompanyEmail(user.getEmail());
            created.setEmailVerified(false);
            created.setClientStatus(Constants.CLIENT_PENDING);
            created.setOnboardingStep(Constants.STEP_VERIFY_EMAIL);
            created.setCreatedBy(userId);
            return created;
        });
        // Phone OTP at registration is already proof of a real contact channel — don't make
        // the user prove it again with a second (email) OTP before onboarding. Covers both
        // brand-new clients and ones stuck on this step from before phone OTP existed.
        if (Constants.STEP_VERIFY_EMAIL.equals(c.getOnboardingStep()) && Boolean.TRUE.equals(user.getPhoneVerified())) {
            c.setEmailVerified(true);
            c.setOnboardingStep(Constants.STEP_COMPANY_INFO);
        }
        return clientRepository.save(c);
    }

    /** Resolves an event actor (userId or "SYS") to a display name, caching lookups. */
    private String resolveActorName(String actor, java.util.Map<String, String> cache) {
        if (actor == null || actor.isBlank()) return null;
        if (Constants.SYSTEM.equals(actor)) return "System";
        return cache.computeIfAbsent(actor, id ->
            userRepository.findById(id).map(UserEntity::getUsername).orElse(id));
    }

    private UserEntity requireUser(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "User not found"));
    }

    private ClientResponse toResponse(ClientEntity c) {
        UserEntity user = userRepository.findById(c.getUserId()).orElse(null);
        return ClientResponse.builder()
            .id(c.getId())
            .userId(c.getUserId())
            .username(user != null ? user.getUsername() : null)
            .email(user != null ? user.getEmail() : null)
            .phoneNumber(user != null ? user.getPhoneNumber() : null)
            .approvalStatus(user != null ? user.getApprovalStatus() : null)
            .companyName(c.getCompanyName())
            .companyEmail(c.getCompanyEmail())
            .companyPhone(c.getCompanyPhone())
            .address(c.getAddress())
            .city(c.getCity())
            .country(c.getCountry())
            .businessType(c.getBusinessType())
            .logoUrl(c.getLogoUrl())
            .emailVerified(Boolean.TRUE.equals(c.getEmailVerified()))
            .clientStatus(c.getClientStatus())
            .onboardingStep(c.getOnboardingStep())
            .createdAt(c.getCreatedAt())
            .build();
    }
}
