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
import com.cambofreelance.webbackend.payway.PaywayPurchaseResult;
import com.cambofreelance.webbackend.payway.PaywayTransactionStatus;
import com.cambofreelance.webbackend.repository.PaymentTransactionRepository;
import com.cambofreelance.webbackend.repository.PricingPlanRepository;
import com.cambofreelance.webbackend.repository.UserRepository;
import com.cambofreelance.webbackend.repository.UserSubscriptionRepository;
import com.cambofreelance.webbackend.services.EmailService;
import com.cambofreelance.webbackend.services.NotificationService;
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

    /** How many days before expiry the auto-renew job starts attempting a stored-token charge. */
    private static final int AUTO_RENEW_LEAD_DAYS = 3;

    /** Consecutive auto-renew failures before giving up and turning auto_renew off. */
    private static final int AUTO_RENEW_MAX_FAILURES = 3;

    /** Don't re-attempt a subscription the job already tried within this window. */
    private static final long AUTO_RENEW_RETRY_HOURS = 20;

    /** Days-before-expiry marks the reminder job checks, most urgent first. */
    private static final int[] EXPIRY_NOTICE_THRESHOLDS = {1, 3, 7};

    private final UserRepository userRepository;
    private final PricingPlanRepository planRepository;
    private final UserSubscriptionRepository subscriptionRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final com.cambofreelance.webbackend.repository.PaymentEventLogRepository eventLogRepository;
    private final com.cambofreelance.webbackend.repository.ClientRepository clientRepository;
    private final com.cambofreelance.webbackend.services.BillingService billingService;
    private final PaywayClient paywayClient;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final SecureRandom secureRandom = new SecureRandom();

    /** ABA has not enabled Card-on-File for this merchant yet — flip only once confirmed. */
    @Value("${payway.cof-enabled:false}")
    private boolean cofEnabled;

    /** Public URL of this backend's /payway/callback endpoint (PayWay pushback target) */
    @Value("${payway.callback-url:}")
    private String callbackUrl;

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

        PricingPlanEntity plan = planRepository.findById(request.getPlanId())
            .filter(p -> Constants.STATUS_ACTIVE.equals(p.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException(ErrorCode.PLAN_NOT_AVAILABLE, "Pricing plan not available");
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });

        // Annual billing only — new subscriptions always bill yearly regardless of what the
        // client sends. Renewals inherit the existing subscription's cycle.
        String cycle = activeSub != null ? activeSub.getBillingCycle() : Constants.BILLING_YEARLY;

        // Switching to a different (higher) plan while still active is a mid-cycle upgrade:
        // charge only the prorated difference and keep the current expiry — don't extend it.
        boolean planChange = activeSub != null && !activeSub.getPlanId().equals(plan.getId());
        BigDecimal amount;
        BigDecimal creditApplied = BigDecimal.ZERO;
        Long remainingDays = null;
        if (planChange) {
            ProrationResult proration = computeUpgradeProration(activeSub, plan, cycle);
            amount = proration.amount();
            creditApplied = proration.credit();
            remainingDays = proration.remainingDays();
        } else {
            amount = priceFor(plan, cycle);
        }

        // $0 plans never touch the payment gateway — activate immediately instead.
        boolean freePlan = amount.compareTo(BigDecimal.ZERO) == 0;

        // Abandon any previous unpaid checkout attempts
        subscriptionRepository.findByUserIdAndSubStatus(userId, Constants.SUB_PENDING_PAYMENT)
            .forEach(s -> {
                s.setSubStatus(Constants.SUB_CANCELLED);
                s.setUpdatedBy(userId);
                s.setUpdatedAt(new Date());
                subscriptionRepository.save(s);
            });

        // Auto-renew opt-in only makes sense when starting a brand-new, paid subscription —
        // renewals and upgrades pay onto an existing sub whose auto-renew state (if any) is
        // unchanged here and managed instead via setAutoRenew()/adminSetAutoRenew(); a $0 plan
        // never gets charged, so there's no card to keep on file.
        boolean requestLifetimeToken = activeSub == null && !freePlan && Boolean.TRUE.equals(request.getAutoRenew());
        if (requestLifetimeToken) {
            requireCofEnabled();
        }

        UserSubscriptionEntity sub;
        if (activeSub != null) {
            // Renewal/upgrade pays onto the existing subscription; activation applies the change.
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
            sub.setAutoRenew(requestLifetimeToken);
            sub.setReferrerId(user.getReferredBy());
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
        tx.setPaymentOption(freePlan ? null : request.getPaymentOption());
        tx.setPaymentStatus(freePlan ? Constants.PAY_APPROVED : Constants.PAY_PENDING);
        tx.setTargetPlanId(plan.getId());
        tx.setProrated(planChange);
        tx.setInitiatedBy(Constants.PAY_INITIATED_USER);
        tx.setReferrerId(sub.getReferrerId());
        tx.setCreatedBy(userId);
        if (freePlan) {
            tx.setVerifiedAt(new Date());
            tx.setVerifyMethod("FREE_PLAN");
        }
        transactionRepository.save(tx);
        logEvent(tx, null, tx.getPaymentStatus(), Constants.PAY_SRC_CHECKOUT,
            (freePlan ? "Free plan — activated without payment"
                : planChange ? "Prorated upgrade checkout" : renew ? "Renewal checkout" : "New checkout")
                + " for plan " + plan.getName(), userId);

        if (freePlan) {
            // No gateway round-trip for a $0 plan: settle the transaction and activate the
            // subscription right away, exactly like a PayWay-approved callback would.
            billingService.issueInvoiceForPayment(tx);
            activateSubscription(tx);
            completeClientOnboarding(userId);

            return SubscriptionCheckoutResponse.builder()
                .tranId(tx.getTranId())
                .subscriptionId(sub.getId())
                .amount(amount)
                .currency("USD")
                .paymentRequired(false)
                .prorated(planChange)
                .creditApplied(creditApplied)
                .remainingDays(remainingDays)
                .build();
        }

        // Onboarding: client is now at the payment step
        clientRepository.findByUserId(userId).ifPresent(c -> {
            if (!Constants.STEP_DONE.equals(c.getOnboardingStep())) {
                c.setOnboardingStep(Constants.STEP_PAYMENT);
                c.setUpdatedAt(new Date());
                clientRepository.save(c);
            }
        });

        String itemsJson = "[{\"name\":\"" + plan.getName().replace("\"", "")
            + " (" + cycle.toLowerCase() + (planChange ? ", prorated upgrade" : "") + ")\",\"quantity\":1,\"price\":"
            + amount.setScale(2, RoundingMode.HALF_UP).toPlainString() + "}]";

        PaywayPurchaseResult qr = paywayClient.purchase(
            tx.getTranId(), amount, "USD",
            user.getUsername(), null, user.getEmail(), user.getPhoneNumber(),
            itemsJson, request.getPaymentOption(),
            StringUtils.hasText(callbackUrl) ? callbackUrl : null,
            tx.getTranId());

        if (!qr.isSuccess()) {
            log.error("[PayWay] purchase failed for tran_id={} code={} message={}",
                tx.getTranId(), qr.getStatusCode(), qr.getStatusMessage());
            AppException ex = new AppException(ErrorCode.PAYMENT_GATEWAY_ERROR,
                "Payment gateway rejected the request: " + qr.getStatusMessage());
            ex.setHttpStatus(HttpStatus.BAD_GATEWAY);
            throw ex;
        }

        return SubscriptionCheckoutResponse.builder()
            .tranId(tx.getTranId())
            .subscriptionId(sub.getId())
            .amount(amount)
            .currency("USD")
            .qrImage(qr.getQrImage())
            .qrString(qr.getQrString())
            .abapayDeeplink(qr.getAbapayDeeplink())
            .paymentRequired(true)
            .prorated(planChange)
            .creditApplied(creditApplied)
            .remainingDays(remainingDays)
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
    @Transactional
    @Auditable(action = "REFUND", module = "PAYMENT")
    public PaymentTransactionResponse refundPayment(String tranId, String reason, String adminId) {
        PaymentTransactionEntity tx = transactionRepository.findByTranId(tranId)
            .orElseThrow(() -> {
                AppException ex = new AppException(ErrorCode.PAYMENT_NOT_FOUND, "Payment transaction not found");
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
        if (Constants.PAY_REFUNDED.equals(tx.getPaymentStatus())) {
            return toPaymentResponse(tx); // already refunded — idempotent
        }
        if (!Constants.PAY_APPROVED.equals(tx.getPaymentStatus())) {
            AppException ex = new AppException(ErrorCode.PAYMENT_NOT_COMPLETED,
                "Only a settled (approved) payment can be refunded");
            ex.setHttpStatus(HttpStatus.BAD_REQUEST);
            throw ex;
        }

        // Money actually moves here (once ABA's refund API is wired in) — if this throws,
        // nothing below runs and the transaction stays APPROVED.
        paywayClient.refund(tx.getTranId(), tx.getAmount(), reason);

        String from = tx.getPaymentStatus();
        tx.setPaymentStatus(Constants.PAY_REFUNDED);
        tx.setRefundedAt(new Date());
        tx.setRefundedBy(adminId);
        tx.setRefundReason(reason);
        tx.setUpdatedAt(new Date());
        tx.setUpdatedBy(adminId);
        transactionRepository.save(tx);
        logEvent(tx, from, Constants.PAY_REFUNDED, Constants.PAY_SRC_MANUAL, reason, adminId);
        billingService.markInvoiceRefunded(tx.getId());

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

    // ── Auto-renew (Card-on-File) ──────────────────────────────────────────

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SUBSCRIPTION")
    public SubscriptionResponse setAutoRenew(String userId, boolean autoRenew) {
        UserSubscriptionEntity sub = subscriptionRepository
            .findFirstByUserIdAndSubStatusAndExpiresAtAfterOrderByExpiresAtDesc(userId, Constants.SUB_ACTIVE, new Date())
            .orElseThrow(() -> {
                AppException ex = new AppException(ErrorCode.ACTIVE_SUBSCRIPTION_NOT_FOUND, "No active subscription found");
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
        applyAutoRenewToggle(sub, autoRenew, userId);
        return toSubscriptionResponse(sub);
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SUBSCRIPTION")
    public SubscriptionResponse adminSetAutoRenew(String subscriptionId, boolean autoRenew, String adminId) {
        UserSubscriptionEntity sub = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> {
                AppException ex = new AppException(ErrorCode.ACTIVE_SUBSCRIPTION_NOT_FOUND, "Subscription not found");
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
        applyAutoRenewToggle(sub, autoRenew, adminId);
        return toSubscriptionResponse(sub);
    }

    /**
     * Turning auto-renew OFF is always allowed (safety valve). Turning it ON requires COF to be
     * enabled and a payment token to already exist — never a silent no-op if either is missing.
     */
    private void applyAutoRenewToggle(UserSubscriptionEntity sub, boolean autoRenew, String actorId) {
        if (autoRenew) {
            requireCofEnabled();
            if (!StringUtils.hasText(sub.getPaymentToken())) {
                AppException ex = new AppException(ErrorCode.AUTO_RENEW_TOKEN_MISSING,
                    "No card on file yet — opt in during checkout once auto-renew is available");
                ex.setHttpStatus(HttpStatus.BAD_REQUEST);
                throw ex;
            }
            sub.setAutoRenewFailureCount(0);
        }
        sub.setAutoRenew(autoRenew);
        sub.setUpdatedAt(new Date());
        sub.setUpdatedBy(actorId);
        subscriptionRepository.save(sub);
    }

    private void requireCofEnabled() {
        if (!cofEnabled) {
            AppException ex = new AppException(ErrorCode.AUTO_RENEW_NOT_AVAILABLE,
                "Auto-renewal is not available yet");
            ex.setHttpStatus(HttpStatus.NOT_IMPLEMENTED);
            throw ex;
        }
    }

    /**
     * TODO: verify against ABA's real COF spec — once `lifetime` tokenization is confirmed
     * working, PayWay's transaction-detail/callback payload is expected to carry some token
     * reference. Confirm the exact JSON field name (inspect status.getRaw()) and wire it into
     * sub.setPaymentToken(...) / sub.setPaymentTokenCapturedAt(...). Until then this intentionally
     * does nothing, so an auto_renew subscription never accumulates a token and
     * attemptAutoRenewals() stays inert for it.
     */
    private void captureCofTokenIfRequested(UserSubscriptionEntity sub, PaywayTransactionStatus status) {
        if (!cofEnabled || !Boolean.TRUE.equals(sub.getAutoRenew()) || sub.getPaymentToken() != null) {
            return;
        }
        log.warn("[Subscription] auto-renew requested for sub={} but COF token capture is not implemented "
            + "yet (awaiting ABA spec)", sub.getId());
    }

    @Override
    @Transactional
    public void attemptAutoRenewals() {
        if (!cofEnabled) {
            return; // feature dark — no query, no log noise
        }
        Date now = new Date();
        Calendar windowEndCal = Calendar.getInstance();
        windowEndCal.setTime(now);
        windowEndCal.add(Calendar.DAY_OF_MONTH, AUTO_RENEW_LEAD_DAYS);

        Calendar attemptCutoffCal = Calendar.getInstance();
        attemptCutoffCal.setTime(now);
        attemptCutoffCal.add(Calendar.HOUR_OF_DAY, (int) -AUTO_RENEW_RETRY_HOURS);

        List<UserSubscriptionEntity> candidates = subscriptionRepository.findAutoRenewCandidates(
            Constants.SUB_ACTIVE, now, windowEndCal.getTime(), AUTO_RENEW_MAX_FAILURES, attemptCutoffCal.getTime());
        for (UserSubscriptionEntity sub : candidates) {
            try {
                attemptRenewal(sub, now);
            } catch (Exception e) {
                // One candidate's failure must never abort the batch.
                log.error("[AutoRenew] unexpected error attempting renewal for sub={}", sub.getId(), e);
            }
        }
    }

    private void attemptRenewal(UserSubscriptionEntity sub, Date now) {
        PricingPlanEntity plan = planRepository.findById(sub.getPlanId()).orElse(null);
        if (plan == null) {
            log.error("[AutoRenew] plan {} missing for sub={}", sub.getPlanId(), sub.getId());
            return;
        }

        PaymentTransactionEntity tx = new PaymentTransactionEntity();
        tx.setId(UUID.randomUUID().toString());
        tx.setTranId(newTranId());
        tx.setSubscriptionId(sub.getId());
        tx.setUserId(sub.getUserId());
        tx.setAmount(priceFor(plan, sub.getBillingCycle()));
        tx.setCurrency(sub.getCurrency());
        tx.setPaymentStatus(Constants.PAY_PENDING);
        tx.setTargetPlanId(sub.getPlanId());
        tx.setInitiatedBy(Constants.PAY_INITIATED_AUTO_RENEW);
        tx.setReferrerId(sub.getReferrerId());
        tx.setCreatedBy(Constants.SYSTEM);
        transactionRepository.save(tx);
        logEvent(tx, null, Constants.PAY_PENDING, Constants.PAY_SRC_AUTO_RENEW, "Auto-renew charge attempt", Constants.SYSTEM);

        sub.setAutoRenewLastAttemptAt(now);
        UserEntity user = userRepository.findById(sub.getUserId()).orElse(null);
        try {
            // TODO: verify against ABA's real COF spec — chargeStoredToken always throws today
            // (see PaywayClient#chargeStoredToken). Once real, add the success branch here:
            //   billingService.issueInvoiceForPayment(tx); activateSubscription(tx);
            //   sub.setAutoRenewFailureCount(0); subscriptionRepository.save(sub);
            //   if (user != null) emailService.sendAutoRenewSuccess(user.getEmail(), plan.getName(), sub.getExpiresAt());
            paywayClient.chargeStoredToken(tx.getTranId(), sub.getPaymentToken(), tx.getAmount(), tx.getCurrency());
        } catch (Exception e) {
            tx.setPaymentStatus(Constants.PAY_DECLINED);
            tx.setVerifyMethod("AUTO");
            tx.setVerifyNote(e.getMessage());
            tx.setUpdatedAt(now);
            tx.setUpdatedBy(Constants.SYSTEM);
            transactionRepository.save(tx);
            logEvent(tx, Constants.PAY_PENDING, Constants.PAY_DECLINED, Constants.PAY_SRC_AUTO_RENEW, e.getMessage(), Constants.SYSTEM);

            int failures = sub.getAutoRenewFailureCount() + 1;
            sub.setAutoRenewFailureCount(failures);
            if (failures >= AUTO_RENEW_MAX_FAILURES) {
                // Give up — the existing daily expireSubscriptions job naturally reaps this once
                // expires_at passes; no separate cancel flow is needed here.
                sub.setAutoRenew(false);
                if (user != null) {
                    emailService.sendAutoRenewGaveUp(user.getEmail(), plan.getName(), sub.getExpiresAt());
                }
            } else if (user != null) {
                emailService.sendAutoRenewFailed(user.getEmail(), plan.getName(), sub.getExpiresAt(), failures, AUTO_RENEW_MAX_FAILURES);
            }
            subscriptionRepository.save(sub);
        }
    }

    @Override
    @Transactional
    public void notifyExpiringSubscriptions() {
        Date now = new Date();
        Calendar windowEndCal = Calendar.getInstance();
        windowEndCal.setTime(now);
        windowEndCal.add(Calendar.DAY_OF_MONTH, EXPIRY_NOTICE_THRESHOLDS[EXPIRY_NOTICE_THRESHOLDS.length - 1]);

        List<UserSubscriptionEntity> candidates = subscriptionRepository.findExpiryReminderCandidates(
            Constants.SUB_ACTIVE, now, windowEndCal.getTime());
        List<String> adminEmails = resolveAdminEmails();
        for (UserSubscriptionEntity sub : candidates) {
            try {
                maybeNotifyExpiry(sub, now, adminEmails);
            } catch (Exception e) {
                // One candidate's failure must never abort the batch.
                log.error("[ExpiryReminder] unexpected error for sub={}", sub.getId(), e);
            }
        }
    }

    private void maybeNotifyExpiry(UserSubscriptionEntity sub, Date now, List<String> adminEmails) {
        long daysRemaining = daysBetween(now, sub.getExpiresAt());
        Integer threshold = null;
        for (int t : EXPIRY_NOTICE_THRESHOLDS) {
            if (daysRemaining <= t && !isNoticeSent(sub, t)) {
                threshold = t;
                break;
            }
        }
        if (threshold == null) {
            return;
        }

        // Mark this tier and every coarser tier sent, so a subscription that jumps straight to a
        // closer tier (e.g. created already near expiry) fires exactly one notice per run, not a stack.
        for (int t : EXPIRY_NOTICE_THRESHOLDS) {
            if (t >= threshold) {
                markNoticeSent(sub, t);
            }
        }
        subscriptionRepository.save(sub);

        UserEntity user = userRepository.findById(sub.getUserId()).orElse(null);
        PricingPlanEntity plan = planRepository.findById(sub.getPlanId()).orElse(null);
        String planName = plan != null ? plan.getName() : "Unknown plan";
        String customerLabel = user != null ? user.getUsername() + " (" + user.getEmail() + ")" : sub.getUserId();
        String dayWord = daysRemaining == 1 ? "day" : "days";

        notificationService.create(
            Constants.NOTIF_TYPE_SUBSCRIPTION_EXPIRING,
            "Subscription expiring in " + daysRemaining + " " + dayWord,
            customerLabel + "'s " + planName + " subscription expires on " + new SimpleDateFormat("yyyy-MM-dd").format(sub.getExpiresAt())
                + ". Consider following up to renew.",
            sub.getId(), Constants.NOTIF_REF_SUBSCRIPTION);

        if (user != null) {
            if (!adminEmails.isEmpty()) {
                emailService.sendSubscriptionExpiringAlert(
                    adminEmails, user.getUsername(), user.getEmail(), planName, sub.getExpiresAt(), daysRemaining);
            } else {
                log.warn("[ExpiryReminder] no ADMIN/SUPER_ADMIN users with an email found; in-app notification still created");
            }
        }
    }

    private boolean isNoticeSent(UserSubscriptionEntity sub, int thresholdDays) {
        return switch (thresholdDays) {
            case 1 -> Boolean.TRUE.equals(sub.getNotice1dSent());
            case 3 -> Boolean.TRUE.equals(sub.getNotice3dSent());
            case 7 -> Boolean.TRUE.equals(sub.getNotice7dSent());
            default -> throw new IllegalArgumentException("Unsupported expiry notice threshold: " + thresholdDays);
        };
    }

    private void markNoticeSent(UserSubscriptionEntity sub, int thresholdDays) {
        switch (thresholdDays) {
            case 1 -> sub.setNotice1dSent(true);
            case 3 -> sub.setNotice3dSent(true);
            case 7 -> sub.setNotice7dSent(true);
            default -> throw new IllegalArgumentException("Unsupported expiry notice threshold: " + thresholdDays);
        }
    }

    private List<String> resolveAdminEmails() {
        return userRepository.findByRoleCodesAndStatus(
                List.of(Constants.ROLE_ADMIN, Constants.ROLE_SUPER_ADMIN), Constants.STATUS_ACTIVE)
            .stream()
            .map(UserEntity::getEmail)
            .filter(StringUtils::hasText)
            .distinct()
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
            subscriptionRepository.findById(tx.getSubscriptionId()).ifPresent(sub -> captureCofTokenIfRequested(sub, status));
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
        // Renewal on a still-active subscription extends from its current expiry;
        // everything else (first activation, reactivation after expiry) starts now.
        boolean stillActive = Constants.SUB_ACTIVE.equals(sub.getSubStatus())
            && sub.getExpiresAt() != null && sub.getExpiresAt().after(now);

        // Apply the target plan regardless of branch below — if the sub is still active this
        // is a prorated in-place switch; if it lapsed before the payment settled, it still
        // belongs on the new plan, just via a normal full-cycle (re)activation.
        boolean planChange = tx.getTargetPlanId() != null && !tx.getTargetPlanId().equals(sub.getPlanId());
        if (planChange) {
            sub.setPlanId(tx.getTargetPlanId());
            planRepository.findById(tx.getTargetPlanId())
                .ifPresent(newPlan -> sub.setPrice(priceFor(newPlan, sub.getBillingCycle())));
        }

        if (planChange && stillActive) {
            // Already paid the prorated difference for the remainder of this cycle —
            // switch plans in place and leave expires_at untouched.
            sub.setSubStatus(Constants.SUB_ACTIVE);
            sub.setUpdatedAt(now);
            sub.setUpdatedBy(Constants.SYSTEM);
            subscriptionRepository.save(sub);
            log.info("[Subscription] upgraded sub={} user={} to plan={} (prorated), still expires {}",
                sub.getId(), sub.getUserId(), tx.getTargetPlanId(), sub.getExpiresAt());
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(stillActive ? sub.getExpiresAt() : now);
        if (Constants.BILLING_YEARLY.equals(sub.getBillingCycle())) {
            cal.add(Calendar.YEAR, 1);
        } else {
            cal.add(Calendar.MONTH, 1);
        }
        if (!stillActive) {
            sub.setStartAt(now);
        }
        sub.setSubStatus(Constants.SUB_ACTIVE);
        sub.setExpiresAt(cal.getTime());
        // A fresh cycle starts here — re-arm the expiry reminder thresholds for it.
        sub.setNotice7dSent(false);
        sub.setNotice3dSent(false);
        sub.setNotice1dSent(false);
        sub.setUpdatedAt(now);
        sub.setUpdatedBy(Constants.SYSTEM);
        subscriptionRepository.save(sub);
        log.info("[Subscription] {} sub={} user={} until {}",
            stillActive ? "renewed" : "activated", sub.getId(), sub.getUserId(), sub.getExpiresAt());
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
            .autoRenewAvailable(cofEnabled)
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

    private record ProrationResult(BigDecimal amount, BigDecimal credit, long remainingDays) {}

    /**
     * Prorates a mid-cycle plan switch: credits the unused remainder of what's already paid
     * on the current plan against the new plan's price for that same remainder, so the client
     * only pays the difference and expires_at doesn't move. Rejects same-or-cheaper plans —
     * those aren't "upgrades" and there's no self-serve downgrade/cancel flow yet.
     */
    private ProrationResult computeUpgradeProration(UserSubscriptionEntity activeSub, PricingPlanEntity newPlan, String cycle) {
        BigDecimal oldPrice = activeSub.getPrice();
        BigDecimal newPrice = priceFor(newPlan, cycle);
        if (newPrice.compareTo(oldPrice) <= 0) {
            AppException ex = new AppException(ErrorCode.PLAN_CHANGE_NOT_UPGRADE,
                "The selected plan must cost more than your current plan to upgrade");
            ex.setHttpStatus(HttpStatus.BAD_REQUEST);
            throw ex;
        }

        Date expiresAt = activeSub.getExpiresAt();
        Calendar periodStart = Calendar.getInstance();
        periodStart.setTime(expiresAt);
        if (Constants.BILLING_YEARLY.equals(cycle)) {
            periodStart.add(Calendar.YEAR, -1);
        } else {
            periodStart.add(Calendar.MONTH, -1);
        }

        long totalDays = Math.max(1, daysBetween(periodStart.getTime(), expiresAt));
        long remainingDays = Math.min(totalDays, Math.max(0, daysBetween(new Date(), expiresAt)));
        BigDecimal remainingFraction = BigDecimal.valueOf(remainingDays)
            .divide(BigDecimal.valueOf(totalDays), 6, RoundingMode.HALF_UP);

        BigDecimal newPortion = newPrice.multiply(remainingFraction).setScale(2, RoundingMode.HALF_UP);
        BigDecimal credit = oldPrice.multiply(remainingFraction).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amount = newPortion.subtract(credit);
        // Rounding/near-zero remainder can floor the delta to $0 even for a genuine upgrade —
        // PayWay needs a positive amount, so charge a token minimum instead of nothing.
        BigDecimal minCharge = BigDecimal.valueOf(0.01);
        if (amount.compareTo(minCharge) < 0) {
            amount = minCharge;
        }
        return new ProrationResult(amount, credit, remainingDays);
    }

    private long daysBetween(Date from, Date to) {
        return Math.round((to.getTime() - from.getTime()) / 86_400_000.0);
    }

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
        String referrerUsername = StringUtils.hasText(s.getReferrerId())
            ? userRepository.findById(s.getReferrerId()).map(UserEntity::getUsername).orElse(null)
            : null;
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
            .autoRenew(Boolean.TRUE.equals(s.getAutoRenew()))
            .referrerId(s.getReferrerId())
            .referrerUsername(referrerUsername)
            .hasPaymentToken(StringUtils.hasText(s.getPaymentToken()))
            .autoRenewFailureCount(s.getAutoRenewFailureCount())
            .paymentTokenCapturedAt(s.getPaymentTokenCapturedAt())
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
            .initiatedBy(t.getInitiatedBy())
            .referrerId(t.getReferrerId())
            .createdAt(t.getCreatedAt())
            .verifiedAt(t.getVerifiedAt())
            .refundedBy(t.getRefundedBy())
            .refundReason(t.getRefundReason())
            .refundedAt(t.getRefundedAt())
            .build();
    }
}
