package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.dto.request.SubscriptionCheckoutRequest;
import com.cambofreelance.webbackend.dto.response.MySubscriptionResponse;
import com.cambofreelance.webbackend.dto.response.PaymentTransactionResponse;
import com.cambofreelance.webbackend.dto.response.SubscriptionCheckoutResponse;
import com.cambofreelance.webbackend.dto.response.SubscriptionResponse;
import com.cambofreelance.webbackend.entities.PaymentTransactionEntity;
import com.cambofreelance.webbackend.entities.PricingPlanEntity;
import com.cambofreelance.webbackend.entities.UserEntity;
import com.cambofreelance.webbackend.entities.UserSubscriptionEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.payway.PaywayClient;
import com.cambofreelance.webbackend.payway.PaywayTransactionStatus;
import com.cambofreelance.webbackend.repository.PaymentTransactionRepository;
import com.cambofreelance.webbackend.repository.PricingPlanRepository;
import com.cambofreelance.webbackend.repository.UserRepository;
import com.cambofreelance.webbackend.repository.UserSubscriptionRepository;
import com.cambofreelance.webbackend.services.SubscriptionService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final String TRAN_ID_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private final UserRepository userRepository;
    private final PricingPlanRepository planRepository;
    private final UserSubscriptionRepository subscriptionRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final com.cambofreelance.webbackend.repository.PaymentEventLogRepository eventLogRepository;
    private final com.cambofreelance.webbackend.repository.ClientRepository clientRepository;
    private final com.cambofreelance.webbackend.services.BillingService billingService;
    private final PaywayClient paywayClient;
    private final SecureRandom secureRandom = new SecureRandom();

    /** Public URL of this backend's /payway/callback endpoint (PayWay pushback target) */
    @Value("${payway.callback-url:}")
    private String callbackUrl;

    /** Frontend page the payer lands on after a successful payment */
    @Value("${payway.success-url:}")
    private String successUrl;

    /** Frontend page the payer lands on after cancelling */
    @Value("${payway.cancel-url:}")
    private String cancelUrl;

    // ── Checkout ────────────────────────────────────────────────────────────

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "SUBSCRIPTION")
    public SubscriptionCheckoutResponse createCheckout(String userId, SubscriptionCheckoutRequest request) {
        UserEntity user = userRepository.findById(userId)
            .filter(u -> Constants.STATUS_ACTIVE.equals(u.getStatus()))
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "User not found"));

        // Gate: account must be approved by an admin before it can subscribe/pay
        if (!Constants.APPROVAL_APPROVED.equals(user.getApprovalStatus())) {
            AppException ex = new AppException(ErrorCode.ACCOUNT_NOT_APPROVED, "Account is pending admin approval");
            ex.setHttpStatus(HttpStatus.FORBIDDEN);
            throw ex;
        }

        boolean renew = Boolean.TRUE.equals(request.getRenew());
        UserSubscriptionEntity activeSub = subscriptionRepository
            .findFirstByUserIdAndSubStatusAndExpiresAtAfterOrderByExpiresAtDesc(
                userId, Constants.SUB_ACTIVE, new Date())
            .orElse(null);
        if (activeSub != null && !renew) {
            throw new AppException(ErrorCode.SUBSCRIPTION_ALREADY_ACTIVE, "An active subscription already exists");
        }

        PricingPlanEntity plan = planRepository.findById(
                renew && activeSub != null ? activeSub.getPlanId() : request.getPlanId())
            .filter(p -> Constants.STATUS_ACTIVE.equals(p.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException(ErrorCode.PLAN_NOT_AVAILABLE, "Pricing plan not available");
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });

        String cycle = renew && activeSub != null
            ? activeSub.getBillingCycle()
            : (Constants.BILLING_YEARLY.equalsIgnoreCase(request.getBillingCycle())
                ? Constants.BILLING_YEARLY : Constants.BILLING_MONTHLY);
        BigDecimal amount = priceFor(plan, cycle);

        // Abandon any previous unpaid checkout attempts
        subscriptionRepository.findByUserIdAndSubStatus(userId, Constants.SUB_PENDING_PAYMENT)
            .forEach(s -> {
                s.setSubStatus(Constants.SUB_CANCELLED);
                s.setUpdatedBy(userId);
                s.setUpdatedAt(new Date());
                subscriptionRepository.save(s);
            });

        UserSubscriptionEntity sub;
        if (renew && activeSub != null) {
            // Renewal pays onto the existing subscription; activation extends expires_at.
            sub = activeSub;
        } else {
            sub = new UserSubscriptionEntity();
            sub.setId(UUID.randomUUID().toString());
            sub.setUserId(userId);
            sub.setPlanId(plan.getId());
            sub.setBillingCycle(cycle);
            sub.setPrice(amount);
            sub.setCurrency("USD");
            sub.setSubStatus(Constants.SUB_PENDING_PAYMENT);
            sub.setCreatedBy(userId);
            subscriptionRepository.save(sub);
        }

        PaymentTransactionEntity tx = new PaymentTransactionEntity();
        tx.setId(UUID.randomUUID().toString());
        tx.setTranId(newTranId());
        tx.setSubscriptionId(sub.getId());
        tx.setUserId(userId);
        tx.setAmount(amount);
        tx.setCurrency("USD");
        tx.setPaymentOption(request.getPaymentOption());
        tx.setPaymentStatus(Constants.PAY_PENDING);
        tx.setCreatedBy(userId);
        transactionRepository.save(tx);
        logEvent(tx, null, Constants.PAY_PENDING, Constants.PAY_SRC_CHECKOUT,
            (renew ? "Renewal checkout" : "New checkout") + " for plan " + plan.getName(), userId);

        // Onboarding: client is now at the payment step
        clientRepository.findByUserId(userId).ifPresent(c -> {
            if (!Constants.STEP_DONE.equals(c.getOnboardingStep())) {
                c.setOnboardingStep(Constants.STEP_PAYMENT);
                c.setUpdatedAt(new Date());
                clientRepository.save(c);
            }
        });

        String itemsJson = "[{\"name\":\"" + plan.getName().replace("\"", "")
            + " (" + cycle.toLowerCase() + ")\",\"quantity\":1,\"price\":"
            + amount.setScale(2, RoundingMode.HALF_UP).toPlainString() + "}]";

        Map<String, String> fields = paywayClient.buildPurchaseFields(
            tx.getTranId(), amount, "USD",
            user.getUsername(), null, user.getEmail(), user.getPhoneNumber(),
            itemsJson, request.getPaymentOption(),
            StringUtils.hasText(callbackUrl) ? callbackUrl : null,
            StringUtils.hasText(cancelUrl) ? cancelUrl : null,
            StringUtils.hasText(successUrl) ? successUrl : null,
            tx.getTranId());

        return SubscriptionCheckoutResponse.builder()
            .tranId(tx.getTranId())
            .subscriptionId(sub.getId())
            .checkoutUrl(paywayClient.checkoutUrl())
            .formFields(fields)
            .build();
    }

    // ── PayWay pushback ─────────────────────────────────────────────────────

    @Override
    @Transactional
    public void handlePaywayCallback(Map<String, String> params, String rawPayload) {
        String tranId = params.getOrDefault("tran_id", params.get("tranId"));
        if (!StringUtils.hasText(tranId)) {
            log.warn("[PayWay] callback without tran_id, ignoring. payload={}", rawPayload);
            return;
        }
        PaymentTransactionEntity tx = transactionRepository.findByTranId(tranId).orElse(null);
        if (tx == null) {
            log.warn("[PayWay] callback for unknown tran_id={}, ignoring", tranId);
            return;
        }
        tx.setRawCallback(rawPayload);
        verifyAndSettle(tx, Constants.PAY_SRC_CALLBACK, Constants.SYSTEM);
    }

    @Override
    @Transactional
    public PaymentTransactionResponse checkTransaction(String userId, String tranId) {
        PaymentTransactionEntity tx = transactionRepository.findByTranId(tranId)
            .filter(t -> t.getUserId().equals(userId))
            .orElseThrow(() -> {
                AppException ex = new AppException(ErrorCode.PAYMENT_NOT_FOUND, "Payment transaction not found");
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
        if (Constants.PAY_PENDING.equals(tx.getPaymentStatus())) {
            verifyAndSettle(tx, Constants.PAY_SRC_POLL, userId);
        }
        return toPaymentResponse(tx);
    }

    @Override
    @Transactional
    @Auditable(action = "MANUAL_VERIFY", module = "PAYMENT")
    public PaymentTransactionResponse manualVerify(String tranId, boolean approve, String note, String adminId) {
        PaymentTransactionEntity tx = transactionRepository.findByTranId(tranId)
            .orElseThrow(() -> {
                AppException ex = new AppException(ErrorCode.PAYMENT_NOT_FOUND, "Payment transaction not found");
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
        if (Constants.PAY_APPROVED.equals(tx.getPaymentStatus())) {
            return toPaymentResponse(tx); // already settled — idempotent
        }
        String from = tx.getPaymentStatus();
        tx.setPaymentStatus(approve ? Constants.PAY_APPROVED : Constants.PAY_DECLINED);
        tx.setVerifiedAt(new Date());
        tx.setVerifiedBy(adminId);
        tx.setVerifyMethod("MANUAL");
        tx.setVerifyNote(note);
        tx.setUpdatedAt(new Date());
        tx.setUpdatedBy(adminId);
        transactionRepository.save(tx);
        logEvent(tx, from, tx.getPaymentStatus(), Constants.PAY_SRC_MANUAL, note, adminId);

        if (approve) {
            billingService.issueInvoiceForPayment(tx);
            activateSubscription(tx);
            completeClientOnboarding(tx.getUserId());
        }
        return toPaymentResponse(tx);
    }

    @Override
    public List<com.cambofreelance.webbackend.dto.response.PaymentEventResponse> getPaymentLogs(String tranId) {
        PaymentTransactionEntity tx = transactionRepository.findByTranId(tranId)
            .orElseThrow(() -> {
                AppException ex = new AppException(ErrorCode.PAYMENT_NOT_FOUND, "Payment transaction not found");
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
        java.util.Map<String, String> actorNames = new java.util.HashMap<>();
        return eventLogRepository.findByTransactionIdOrderByCreatedAtAsc(tx.getId()).stream()
            .map(e -> com.cambofreelance.webbackend.dto.response.PaymentEventResponse.builder()
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
    }

    /** Resolves an event actor (userId or "SYS") to a display name, caching lookups. */
    private String resolveActorName(String actor, java.util.Map<String, String> cache) {
        if (actor == null || actor.isBlank()) return null;
        if (Constants.SYSTEM.equals(actor)) return "System";
        return cache.computeIfAbsent(actor, id ->
            userRepository.findById(id)
                .map(com.cambofreelance.webbackend.entities.UserEntity::getUsername)
                .orElse(id));
    }

    /**
     * Idempotent: never trusts the callback payload — always confirms with PayWay's
     * transaction-detail API before changing state, and never downgrades APPROVED.
     */
    private void verifyAndSettle(PaymentTransactionEntity tx, String source, String actor) {
        if (Constants.PAY_APPROVED.equals(tx.getPaymentStatus())) {
            return;
        }
        String from = tx.getPaymentStatus();
        PaywayTransactionStatus status = paywayClient.fetchTransactionDetail(tx.getTranId());
        tx.setVerifiedAt(new Date());
        tx.setUpdatedAt(new Date());
        tx.setUpdatedBy(Constants.SYSTEM);

        if (status.isApproved()) {
            tx.setPaymentStatus(Constants.PAY_APPROVED);
            tx.setApv(status.getApv());
            tx.setBankRef(status.getBankRef());
            tx.setVerifyMethod("AUTO");
            transactionRepository.save(tx);
            logEvent(tx, from, Constants.PAY_APPROVED, source, "Confirmed by PayWay (apv " + status.getApv() + ")", actor);
            billingService.issueInvoiceForPayment(tx);
            activateSubscription(tx);
            completeClientOnboarding(tx.getUserId());
        } else if (status.isDeclined() || status.isCancelled()) {
            tx.setPaymentStatus(status.isDeclined() ? Constants.PAY_DECLINED : Constants.PAY_CANCELLED);
            transactionRepository.save(tx);
            logEvent(tx, from, tx.getPaymentStatus(), source, "PayWay status: " + status.getPaymentStatus(), actor);
            subscriptionRepository.findById(tx.getSubscriptionId()).ifPresent(sub -> {
                if (Constants.SUB_PENDING_PAYMENT.equals(sub.getSubStatus())) {
                    sub.setSubStatus(Constants.SUB_CANCELLED);
                    sub.setUpdatedAt(new Date());
                    sub.setUpdatedBy(Constants.SYSTEM);
                    subscriptionRepository.save(sub);
                }
            });
        } else if (status.isRefunded()) {
            tx.setPaymentStatus(Constants.PAY_REFUNDED);
            transactionRepository.save(tx);
            logEvent(tx, from, Constants.PAY_REFUNDED, source, "PayWay reported refund", actor);
            billingService.markInvoiceRefunded(tx.getId());
        } else {
            // still pending on PayWay's side — keep ours pending too
            transactionRepository.save(tx);
        }
    }

    private void activateSubscription(PaymentTransactionEntity tx) {
        UserSubscriptionEntity sub = subscriptionRepository.findById(tx.getSubscriptionId()).orElse(null);
        if (sub == null) {
            return;
        }
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        // Renewal on a still-active subscription extends from its current expiry;
        // everything else (first activation, reactivation after expiry) starts now.
        boolean extend = Constants.SUB_ACTIVE.equals(sub.getSubStatus())
            && sub.getExpiresAt() != null && sub.getExpiresAt().after(now);
        cal.setTime(extend ? sub.getExpiresAt() : now);
        if (Constants.BILLING_YEARLY.equals(sub.getBillingCycle())) {
            cal.add(Calendar.YEAR, 1);
        } else {
            cal.add(Calendar.MONTH, 1);
        }
        if (!extend) {
            sub.setStartAt(now);
        }
        sub.setSubStatus(Constants.SUB_ACTIVE);
        sub.setExpiresAt(cal.getTime());
        sub.setUpdatedAt(now);
        sub.setUpdatedBy(Constants.SYSTEM);
        subscriptionRepository.save(sub);
        log.info("[Subscription] {} sub={} user={} until {}",
            extend ? "renewed" : "activated", sub.getId(), sub.getUserId(), sub.getExpiresAt());
    }

    private void completeClientOnboarding(String userId) {
        clientRepository.findByUserId(userId).ifPresent(c -> {
            if (!Constants.STEP_DONE.equals(c.getOnboardingStep())
                || Constants.CLIENT_PENDING.equals(c.getClientStatus())) {
                c.setOnboardingStep(Constants.STEP_DONE);
                if (Constants.CLIENT_PENDING.equals(c.getClientStatus())) {
                    c.setClientStatus(Constants.CLIENT_ACTIVE);
                }
                c.setUpdatedAt(new Date());
                c.setUpdatedBy(Constants.SYSTEM);
                clientRepository.save(c);
            }
        });
    }

    private void logEvent(PaymentTransactionEntity tx, String from, String to, String source, String note, String actor) {
        var e = new com.cambofreelance.webbackend.entities.PaymentEventLogEntity();
        e.setId(UUID.randomUUID().toString());
        e.setTransactionId(tx.getId());
        e.setFromStatus(from);
        e.setToStatus(to);
        e.setSource(source);
        e.setNote(note);
        e.setActor(actor);
        e.setCreatedAt(new Date());
        eventLogRepository.save(e);
    }

    // ── Queries ─────────────────────────────────────────────────────────────

    @Override
    public MySubscriptionResponse getMySubscription(String userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "User not found"));

        List<UserSubscriptionEntity> all = subscriptionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        Date now = new Date();
        SubscriptionResponse active = all.stream()
            .filter(s -> Constants.SUB_ACTIVE.equals(s.getSubStatus())
                && s.getExpiresAt() != null && s.getExpiresAt().after(now))
            .findFirst()
            .map(this::toSubscriptionResponse)
            .orElse(null);

        boolean approved = Constants.APPROVAL_APPROVED.equals(user.getApprovalStatus());
        return MySubscriptionResponse.builder()
            .approvalStatus(user.getApprovalStatus())
            .canSubscribe(approved && active == null)
            .activeSubscription(active)
            .history(all.stream().map(this::toSubscriptionResponse).toList())
            .build();
    }

    @Override
    public List<PaymentTransactionResponse> getMyPayments(String userId) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(this::toPaymentResponse)
            .toList();
    }

    @Override
    public Page<SubscriptionResponse> adminListSubscriptions(int page, int size) {
        return subscriptionRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size))
            .map(this::toSubscriptionResponse);
    }

    @Override
    public Page<PaymentTransactionResponse> adminListPayments(int page, int size) {
        return transactionRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size))
            .map(this::toPaymentResponse);
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private BigDecimal priceFor(PricingPlanEntity plan, String cycle) {
        if (Constants.BILLING_YEARLY.equals(cycle)) {
            // yearly = configured yearly price, or 12 months with the advertised 20% discount
            BigDecimal yearly = plan.getPriceYearly() != null
                ? plan.getPriceYearly()
                : plan.getPriceMonthly().multiply(BigDecimal.valueOf(12 * 0.8));
            return yearly.setScale(2, RoundingMode.HALF_UP);
        }
        return plan.getPriceMonthly().setScale(2, RoundingMode.HALF_UP);
    }

    /** PayWay tran_id is limited to 20 chars: S + yyMMddHHmmss + 4 random = 17 chars. */
    private String newTranId() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyMMddHHmmss");
        for (int attempt = 0; attempt < 5; attempt++) {
            StringBuilder sb = new StringBuilder("S").append(fmt.format(new Date()));
            for (int i = 0; i < 4; i++) {
                sb.append(TRAN_ID_CHARS.charAt(secureRandom.nextInt(TRAN_ID_CHARS.length())));
            }
            String candidate = sb.toString();
            if (transactionRepository.findByTranId(candidate).isEmpty()) {
                return candidate;
            }
        }
        throw new AppException(ErrorCode.GENERAL_ERROR, "Could not generate a unique transaction id");
    }

    private SubscriptionResponse toSubscriptionResponse(UserSubscriptionEntity s) {
        String planName = planRepository.findById(s.getPlanId())
            .map(PricingPlanEntity::getName).orElse(null);
        return SubscriptionResponse.builder()
            .id(s.getId())
            .userId(s.getUserId())
            .planId(s.getPlanId())
            .planName(planName)
            .billingCycle(s.getBillingCycle())
            .price(s.getPrice())
            .currency(s.getCurrency())
            .subStatus(s.getSubStatus())
            .startAt(s.getStartAt())
            .expiresAt(s.getExpiresAt())
            .createdAt(s.getCreatedAt())
            .build();
    }

    private PaymentTransactionResponse toPaymentResponse(PaymentTransactionEntity t) {
        String username = userRepository.findById(t.getUserId())
            .map(com.cambofreelance.webbackend.entities.UserEntity::getUsername)
            .orElse(null);
        return PaymentTransactionResponse.builder()
            .tranId(t.getTranId())
            .subscriptionId(t.getSubscriptionId())
            .userId(t.getUserId())
            .username(username)
            .amount(t.getAmount())
            .currency(t.getCurrency())
            .paymentOption(t.getPaymentOption())
            .paymentStatus(t.getPaymentStatus())
            .apv(t.getApv())
            .createdAt(t.getCreatedAt())
            .verifiedAt(t.getVerifiedAt())
            .build();
    }
}
