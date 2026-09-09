package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.constants.PartnerApplicationStatus;
import com.cambofreelance.webbackend.constants.PartnerTier;
import com.cambofreelance.webbackend.dto.request.PartnerApplicationRequest;
import com.cambofreelance.webbackend.dto.request.PartnerPayoutRequest;
import com.cambofreelance.webbackend.dto.request.PartnerReviewRequest;
import com.cambofreelance.webbackend.dto.response.AdminReferredClientResponse;
import com.cambofreelance.webbackend.dto.response.PartnerAdminDetailResponse;
import com.cambofreelance.webbackend.dto.response.PartnerApplicationResponse;
import com.cambofreelance.webbackend.dto.response.PartnerPayoutResponse;
import com.cambofreelance.webbackend.dto.response.PartnerPortalResponse;
import com.cambofreelance.webbackend.dto.response.PartnerReferredClientDetailResponse;
import com.cambofreelance.webbackend.entities.ClientEntity;
import com.cambofreelance.webbackend.entities.PricingPlanEntity;
import com.cambofreelance.webbackend.entities.PartnerApplicationEntity;
import com.cambofreelance.webbackend.entities.PartnerPayoutEntity;
import com.cambofreelance.webbackend.entities.UserEntity;
import com.cambofreelance.webbackend.entities.UserSubscriptionEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.ClientRepository;
import com.cambofreelance.webbackend.repository.PartnerApplicationRepository;
import com.cambofreelance.webbackend.repository.PartnerPayoutRepository;
import com.cambofreelance.webbackend.repository.PaymentTransactionRepository;
import com.cambofreelance.webbackend.repository.PricingPlanRepository;
import com.cambofreelance.webbackend.repository.UserRepository;
import com.cambofreelance.webbackend.repository.UserSubscriptionRepository;
import com.cambofreelance.webbackend.services.NotificationService;
import com.cambofreelance.webbackend.services.PartnerService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerApplicationRepository applicationRepository;
    private final PartnerPayoutRepository payoutRepository;
    private final UserRepository userRepository;
    private final UserSubscriptionRepository subscriptionRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final PricingPlanRepository planRepository;
    private final ClientRepository clientRepository;
    private final NotificationService notificationService;

    private static final int REFERRED_CLIENTS_LIMIT = 50;

    @Value("${app.frontend-url:https://soppossytem.cambofreelance.com}")
    private String frontendUrl;

    // ── Applicant ───────────────────────────────────────────────────────────

    @Override
    public PartnerApplicationResponse getMyApplication(String userId) {
        return applicationRepository.findByUserIdAndStatus(userId, Constants.STATUS_ACTIVE)
            .map(PartnerApplicationResponse::from)
            .orElse(null);
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "PARTNER", entityClass = PartnerApplicationEntity.class)
    public PartnerApplicationResponse submitApplication(String userId, PartnerApplicationRequest request) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "User not found"));

        if (!Constants.APPROVAL_APPROVED.equals(user.getApprovalStatus())) {
            AppException ex = new AppException(ErrorCode.ACCOUNT_NOT_APPROVED, "Account is not approved");
            ex.setHttpStatus(HttpStatus.FORBIDDEN);
            throw ex;
        }
        if (!Boolean.TRUE.equals(request.getAgreementAccepted())) {
            throw new AppException(ErrorCode.PARTNER_AGREEMENT_REQUIRED, "Partner agreement must be accepted");
        }

        Optional<PartnerApplicationEntity> existing =
            applicationRepository.findByUserIdAndStatus(userId, Constants.STATUS_ACTIVE);
        PartnerApplicationEntity app;
        if (existing.isPresent()) {
            app = existing.get();
            if (!PartnerApplicationStatus.isReapplyable(app.getAppStatus())) {
                throw new AppException(ErrorCode.PARTNER_APPLICATION_EXISTS,
                    "You already have a partner application in progress");
            }
            // Re-apply in place — keep the id and the partner ref already issued.
            app.setReviewedBy(null);
            app.setReviewedAt(null);
            app.setReviewNote(null);
            app.setActivatedAt(null);
        } else {
            app = new PartnerApplicationEntity();
            app.setId(UUID.randomUUID().toString());
            app.setUserId(userId);
            app.setStatus(Constants.STATUS_ACTIVE);
            app.setCreatedBy(userId);
        }
        if (!StringUtils.hasText(app.getPartnerRef())) {
            app.setPartnerRef(generatePartnerRef());
        }

        applyForm(app, request);
        app.setAgreementAccepted(Boolean.TRUE);
        app.setPayoutChannel(StringUtils.hasText(app.getPayoutChannel()) ? app.getPayoutChannel() : "ABA");
        app.setAppStatus(PartnerApplicationStatus.SUBMITTED);
        app.setSubmittedAt(new Date());
        app.setUpdatedBy(userId);
        app.setUpdatedAt(new Date());
        applicationRepository.save(app);

        notificationService.create(
            Constants.NOTIF_TYPE_PARTNER_APPLICATION,
            "New partner application",
            app.getPartnerRef() + " — " + app.getCompanyName() + " (" + user.getUsername() + ")",
            app.getId(), Constants.NOTIF_REF_PARTNER);

        return PartnerApplicationResponse.from(app);
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "PARTNER", entityClass = PartnerApplicationEntity.class)
    public PartnerApplicationResponse updateApplication(String userId, PartnerApplicationRequest request) {
        PartnerApplicationEntity app = applicationRepository
            .findByUserIdAndStatus(userId, Constants.STATUS_ACTIVE)
            .orElseThrow(() -> notFound());

        if (!PartnerApplicationStatus.isEditable(app.getAppStatus())) {
            throw new AppException(ErrorCode.PARTNER_APPLICATION_NOT_EDITABLE,
                "This application can no longer be edited");
        }

        applyForm(app, request);
        if (Boolean.TRUE.equals(request.getAgreementAccepted())) {
            app.setAgreementAccepted(Boolean.TRUE);
        }
        app.setUpdatedBy(userId);
        app.setUpdatedAt(new Date());
        applicationRepository.save(app);
        return PartnerApplicationResponse.from(app);
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "PARTNER", entityClass = PartnerApplicationEntity.class)
    public PartnerApplicationResponse withdrawApplication(String userId) {
        PartnerApplicationEntity app = applicationRepository
            .findByUserIdAndStatus(userId, Constants.STATUS_ACTIVE)
            .orElseThrow(() -> notFound());

        if (!PartnerApplicationStatus.canTransition(app.getAppStatus(), PartnerApplicationStatus.WITHDRAWN)) {
            throw new AppException(ErrorCode.PARTNER_INVALID_STATE,
                "Cannot withdraw an application in state " + app.getAppStatus());
        }
        app.setAppStatus(PartnerApplicationStatus.WITHDRAWN);
        app.setUpdatedBy(userId);
        app.setUpdatedAt(new Date());
        applicationRepository.save(app);
        return PartnerApplicationResponse.from(app);
    }

    @Override
    public PartnerPortalResponse getMyPortal(String userId) {
        PartnerApplicationEntity app = applicationRepository
            .findByUserIdAndStatus(userId, Constants.STATUS_ACTIVE)
            .filter(a -> PartnerApplicationStatus.APPROVED.equals(a.getAppStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException(ErrorCode.NOT_ACTIVE_PARTNER, "Not an active partner");
                ex.setHttpStatus(HttpStatus.FORBIDDEN);
                return ex;
            });

        Metrics m = computeMetrics(userId, app.getId());

        return PartnerPortalResponse.builder()
            .partnerRef(app.getPartnerRef())
            .appStatus(app.getAppStatus())
            .tier(m.tier)
            .commissionRate(m.rate)
            .referralLink(frontendUrl + "/en/register?ref=" + app.getPartnerRef())
            .payoutChannel(app.getPayoutChannel())
            .activatedAt(app.getActivatedAt())
            .stats(m.stats)
            .referredClients(buildReferredClients(userId, m.rate))
            .build();
    }

    @Override
    public PartnerReferredClientDetailResponse getReferredClientDetail(String partnerId, String clientUserId) {
        UserEntity client = userRepository.findById(clientUserId)
            .filter(u -> partnerId.equals(u.getReferredBy()))
            .orElseThrow(() -> {
                AppException ex = new AppException(ErrorCode.PARTNER_CLIENT_NOT_FOUND, "Referred client not found");
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });

        ClientEntity company = clientRepository.findByUserId(clientUserId).orElse(null);
        List<UserSubscriptionEntity> subs = subscriptionRepository.findByUserIdOrderByCreatedAtDesc(clientUserId);
        UserSubscriptionEntity latest = subs.isEmpty() ? null : subs.get(0);
        PricingPlanEntity plan = latest == null ? null : planRepository.findById(latest.getPlanId()).orElse(null);

        long activeCount = subscriptionRepository
            .countDistinctUsersByReferrerIdAndSubStatus(partnerId, Constants.SUB_ACTIVE);
        BigDecimal rate = PartnerTier.rateOf(PartnerTier.tierFor(activeCount));
        BigDecimal gross = nz(transactionRepository
            .sumAmountByReferrerIdAndUserIdAndPaymentStatus(partnerId, clientUserId, Constants.PAY_APPROVED))
            .setScale(2, RoundingMode.HALF_UP);
        BigDecimal commission = gross.multiply(rate).setScale(2, RoundingMode.HALF_UP);

        return PartnerReferredClientDetailResponse.builder()
            .userId(client.getUserId())
            .username(client.getUsername())
            .email(client.getEmail())
            .phoneNumber(client.getPhoneNumber())
            .joinedDate(client.getCreatedAt())
            .companyName(company != null ? company.getCompanyName() : null)
            .companyEmail(company != null ? company.getCompanyEmail() : null)
            .companyPhone(company != null ? company.getCompanyPhone() : null)
            .address(company != null ? company.getAddress() : null)
            .city(company != null ? company.getCity() : null)
            .country(company != null ? company.getCountry() : null)
            .businessType(company != null ? company.getBusinessType() : null)
            .logoUrl(company != null ? company.getLogoUrl() : null)
            .clientStatus(company != null ? company.getClientStatus() : null)
            .planId(latest != null ? latest.getPlanId() : null)
            .planName(plan != null ? plan.getName() : null)
            .billingCycle(latest != null ? latest.getBillingCycle() : null)
            .price(latest != null ? latest.getPrice() : null)
            .currency(latest != null ? latest.getCurrency() : null)
            .subStatus(latest != null ? latest.getSubStatus() : null)
            .startAt(latest != null ? latest.getStartAt() : null)
            .expiresAt(latest != null ? latest.getExpiresAt() : null)
            .autoRenew(latest != null && Boolean.TRUE.equals(latest.getAutoRenew()))
            .totalPaid(gross)
            .commissionRate(rate)
            .commission(commission)
            .build();
    }

    // ── Admin ───────────────────────────────────────────────────────────────

    @Override
    public Page<PartnerApplicationResponse> adminList(String appStatus, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<PartnerApplicationEntity> result = StringUtils.hasText(appStatus)
            ? applicationRepository.findByAppStatusAndStatusOrderByCreatedAtDesc(
                appStatus.trim().toUpperCase(), Constants.STATUS_ACTIVE, pageable)
            : applicationRepository.findByStatusOrderByCreatedAtDesc(Constants.STATUS_ACTIVE, pageable);
        return result.map(PartnerApplicationResponse::from);
    }

    @Override
    public PartnerAdminDetailResponse adminGet(String id) {
        PartnerApplicationEntity app = applicationRepository
            .findByIdAndStatus(id, Constants.STATUS_ACTIVE)
            .orElseThrow(() -> notFound());

        Metrics m = computeMetrics(app.getUserId(), app.getId());
        List<PartnerPayoutResponse> payouts = payoutRepository
            .findByApplicationIdAndStatusOrderByPaidAtDesc(app.getId(), Constants.STATUS_ACTIVE)
            .stream().map(PartnerPayoutResponse::from).toList();

        return PartnerAdminDetailResponse.builder()
            .application(PartnerApplicationResponse.from(app))
            .tier(m.tier)
            .commissionRate(m.rate)
            .stats(m.stats)
            .payouts(payouts)
            .build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "PARTNER", entityClass = PartnerApplicationEntity.class)
    public PartnerApplicationResponse adminReview(String id, PartnerReviewRequest request, String adminId) {
        PartnerApplicationEntity app = applicationRepository
            .findByIdAndStatus(id, Constants.STATUS_ACTIVE)
            .orElseThrow(() -> notFound());

        String decision = request.getDecision() == null ? "" : request.getDecision().trim().toUpperCase();
        boolean allowed = PartnerApplicationStatus.UNDER_REVIEW.equals(decision)
            || PartnerApplicationStatus.DEMO_SCHEDULED.equals(decision)
            || PartnerApplicationStatus.APPROVED.equals(decision)
            || PartnerApplicationStatus.REJECTED.equals(decision);
        if (!allowed || !PartnerApplicationStatus.canTransition(app.getAppStatus(), decision)) {
            throw new AppException(ErrorCode.PARTNER_INVALID_STATE,
                "Cannot move application from " + app.getAppStatus() + " to " + decision);
        }

        Date now = new Date();
        app.setAppStatus(decision);
        app.setReviewedBy(adminId);
        app.setReviewedAt(now);
        app.setReviewNote(request.getNote());
        app.setUpdatedBy(adminId);
        app.setUpdatedAt(now);

        if (PartnerApplicationStatus.APPROVED.equals(decision)) {
            app.setActivatedAt(now);
            UserEntity user = userRepository.findById(app.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "Partner user not found"));
            // The partner ref becomes the user's referral code so the existing attribution
            // pipeline (registration -> subscription.referrerId -> payment.referrerId) credits
            // every downstream payment to this partner with no extra wiring.
            user.setReferralCode(app.getPartnerRef());
            user.setUpdatedBy(adminId);
            user.setUpdatedAt(now);
            userRepository.save(user);
            log.info("Partner {} approved — referral code of user {} set to {}",
                app.getPartnerRef(), user.getUserId(), app.getPartnerRef());
        }

        applicationRepository.save(app);
        return PartnerApplicationResponse.from(app);
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "PARTNER", entityClass = PartnerPayoutEntity.class)
    public PartnerPayoutResponse recordPayout(String applicationId, PartnerPayoutRequest request, String adminId) {
        PartnerApplicationEntity app = applicationRepository
            .findByIdAndStatus(applicationId, Constants.STATUS_ACTIVE)
            .orElseThrow(() -> notFound());
        if (!PartnerApplicationStatus.APPROVED.equals(app.getAppStatus())) {
            AppException ex = new AppException(ErrorCode.NOT_ACTIVE_PARTNER,
                "Payouts can only be recorded for an active partner");
            ex.setHttpStatus(HttpStatus.FORBIDDEN);
            throw ex;
        }

        PartnerPayoutEntity payout = new PartnerPayoutEntity();
        payout.setId(UUID.randomUUID().toString());
        payout.setApplicationId(app.getId());
        payout.setAmount(request.getAmount());
        payout.setChannel(StringUtils.hasText(request.getChannel())
            ? request.getChannel().trim() : app.getPayoutChannel());
        payout.setReference(request.getReference());
        payout.setNote(request.getNote());
        payout.setPaidAt(new Date());
        payout.setStatus(Constants.STATUS_ACTIVE);
        payout.setCreatedBy(adminId);
        payoutRepository.save(payout);
        return PartnerPayoutResponse.from(payout);
    }

    @Override
    public List<PartnerPayoutResponse> listPayouts(String applicationId) {
        applicationRepository.findByIdAndStatus(applicationId, Constants.STATUS_ACTIVE)
            .orElseThrow(() -> notFound());
        return payoutRepository
            .findByApplicationIdAndStatusOrderByPaidAtDesc(applicationId, Constants.STATUS_ACTIVE)
            .stream().map(PartnerPayoutResponse::from).toList();
    }

    @Override
    public Page<AdminReferredClientResponse> adminListAllReferredClients(
        String applicationId, String search, String subStatus, int page, int size) {
        // Scoped to one partner (the "show all clients under this partner" drill-down) when
        // applicationId is given; otherwise the flat directory across every APPROVED partner.
        List<PartnerApplicationEntity> partners = StringUtils.hasText(applicationId)
            ? List.of(applicationRepository.findByIdAndStatus(applicationId, Constants.STATUS_ACTIVE)
                .orElseThrow(() -> notFound()))
            : applicationRepository
                .findByAppStatusAndStatusOrderByCreatedAtDesc(
                    PartnerApplicationStatus.APPROVED, Constants.STATUS_ACTIVE, Pageable.unpaged())
                .getContent();

        String q = StringUtils.hasText(search) ? search.trim().toLowerCase() : null;
        String statusFilter = StringUtils.hasText(subStatus) ? subStatus.trim().toUpperCase() : null;

        List<AdminReferredClientResponse> all = new ArrayList<>();
        for (PartnerApplicationEntity app : partners) {
            long activeCount = subscriptionRepository
                .countDistinctUsersByReferrerIdAndSubStatus(app.getUserId(), Constants.SUB_ACTIVE);
            BigDecimal rate = PartnerTier.rateOf(PartnerTier.tierFor(activeCount));

            for (PartnerPortalResponse.ReferredClient c : buildReferredClients(app.getUserId(), rate)) {
                boolean matchesSearch = q == null
                    || containsIgnoreCase(c.getUsername(), q)
                    || containsIgnoreCase(c.getCompanyName(), q)
                    || containsIgnoreCase(app.getPartnerRef(), q)
                    || containsIgnoreCase(app.getCompanyName(), q);
                boolean matchesStatus = statusFilter == null || statusFilter.equals(c.getSubStatus());
                if (!matchesSearch || !matchesStatus) continue;

                all.add(AdminReferredClientResponse.builder()
                    .partnerApplicationId(app.getId())
                    .partnerRef(app.getPartnerRef())
                    .partnerCompanyName(app.getCompanyName())
                    .client(c)
                    .build());
            }
        }

        int from = Math.min(page * size, all.size());
        int to = Math.min(from + size, all.size());
        return new PageImpl<>(all.subList(from, to), PageRequest.of(page, size), all.size());
    }

    @Override
    public PartnerReferredClientDetailResponse adminGetReferredClientDetail(String clientUserId) {
        String partnerId = userRepository.findById(clientUserId)
            .map(UserEntity::getReferredBy)
            .filter(StringUtils::hasText)
            .orElseThrow(() -> {
                AppException ex = new AppException(ErrorCode.PARTNER_CLIENT_NOT_FOUND, "Referred client not found");
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
        return getReferredClientDetail(partnerId, clientUserId);
    }

    private static boolean containsIgnoreCase(String haystack, String needleLower) {
        return haystack != null && haystack.toLowerCase().contains(needleLower);
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private void applyForm(PartnerApplicationEntity app, PartnerApplicationRequest r) {
        app.setPartnerName(trimToNull(r.getPartnerName()));
        app.setCompanyName(trimToNull(r.getCompanyName()));
        app.setPartnerType(trimToNull(r.getPartnerType()));
        app.setBusinessType(trimToNull(r.getBusinessType()));
        app.setEmployeeCount(trimToNull(r.getEmployeeCount()));
        app.setBusinessRegistrationNo(trimToNull(r.getBusinessRegistrationNo()));
        app.setWebsite(trimToNull(r.getWebsite()));
        app.setCountry(trimToNull(r.getCountry()));
        app.setCity(trimToNull(r.getCity()));
        app.setBusinessAddress(trimToNull(r.getBusinessAddress()));
        app.setLogoUrl(trimToNull(r.getLogoUrl()));
        app.setNotes(trimToNull(r.getNotes()));
    }

    private static String trimToNull(String s) {
        return StringUtils.hasText(s) ? s.trim() : null;
    }

    private AppException notFound() {
        AppException ex = new AppException(ErrorCode.PARTNER_APPLICATION_NOT_FOUND, "Partner application not found");
        ex.setHttpStatus(HttpStatus.NOT_FOUND);
        return ex;
    }

    /** PTR-YYYY-NNNN, retrying on the (unlikely) collision — mirrors SubscriptionServiceImpl.newTranId(). */
    private String generatePartnerRef() {
        String prefix = "PTR-" + Year.now().getValue() + "-";
        long base = applicationRepository.countByPartnerRefStartingWith(prefix);
        for (int attempt = 1; attempt <= 5; attempt++) {
            String candidate = prefix + String.format("%04d", base + attempt);
            if (!applicationRepository.existsByPartnerRef(candidate)) {
                return candidate;
            }
        }
        throw new AppException(ErrorCode.GENERAL_ERROR, "Could not generate a unique partner ref");
    }

    private Metrics computeMetrics(String userId, String applicationId) {
        long totalReferred = userRepository.countByReferredBy(userId);
        long activeSubs = subscriptionRepository
            .countDistinctUsersByReferrerIdAndSubStatus(userId, Constants.SUB_ACTIVE);

        String tier = PartnerTier.tierFor(activeSubs);
        BigDecimal rate = PartnerTier.rateOf(tier);

        BigDecimal gross = nz(transactionRepository
            .sumAmountByReferrerIdAndPaymentStatus(userId, Constants.PAY_APPROVED));
        BigDecimal earned = gross.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal paidOut = nz(payoutRepository.sumPaid(applicationId, Constants.STATUS_ACTIVE))
            .setScale(2, RoundingMode.HALF_UP);
        BigDecimal pending = earned.subtract(paidOut).max(BigDecimal.ZERO);

        BigDecimal conversion = totalReferred == 0 ? BigDecimal.ZERO
            : BigDecimal.valueOf(activeSubs)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalReferred), 1, RoundingMode.HALF_UP);

        PartnerPortalResponse.Stats stats = PartnerPortalResponse.Stats.builder()
            .totalReferredClients(totalReferred)
            .activeSubscriptions(activeSubs)
            .conversionRate(conversion)
            .commissionEarned(earned)
            .totalPaidOut(paidOut)
            .pendingPayout(pending)
            .build();

        return new Metrics(tier, rate, stats);
    }

    private List<PartnerPortalResponse.ReferredClient> buildReferredClients(String userId, BigDecimal rate) {
        Page<UserEntity> referred = userRepository.findByReferredByOrderByCreatedAtDesc(
            userId, PageRequest.of(0, REFERRED_CLIENTS_LIMIT, Sort.by("createdAt").descending()));

        List<PartnerPortalResponse.ReferredClient> out = new ArrayList<>();
        for (UserEntity u : referred.getContent()) {
            ClientEntity client = clientRepository.findByUserId(u.getUserId()).orElse(null);
            List<UserSubscriptionEntity> subs =
                subscriptionRepository.findByUserIdOrderByCreatedAtDesc(u.getUserId());
            UserSubscriptionEntity latest = subs.isEmpty() ? null : subs.get(0);
            String planName = latest == null ? null
                : planRepository.findById(latest.getPlanId())
                    .map(p -> p.getName()).orElse(null);

            BigDecimal clientGross = nz(transactionRepository
                .sumAmountByReferrerIdAndUserIdAndPaymentStatus(userId, u.getUserId(), Constants.PAY_APPROVED));

            out.add(PartnerPortalResponse.ReferredClient.builder()
                .userId(u.getUserId())
                .username(u.getUsername())
                .companyName(client != null ? client.getCompanyName() : null)
                .city(client != null ? client.getCity() : null)
                .businessType(client != null ? client.getBusinessType() : null)
                .joinedDate(u.getCreatedAt())
                .planName(planName)
                .subStatus(latest != null ? latest.getSubStatus() : null)
                .commission(clientGross.multiply(rate).setScale(2, RoundingMode.HALF_UP))
                .build());
        }
        return out;
    }

    private static BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private record Metrics(String tier, BigDecimal rate, PartnerPortalResponse.Stats stats) {}
}
