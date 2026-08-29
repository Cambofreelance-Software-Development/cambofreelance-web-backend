package com.cambofreelance.webbackend.jobs;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.entities.PaymentEventLogEntity;
import com.cambofreelance.webbackend.repository.PaymentEventLogRepository;
import com.cambofreelance.webbackend.repository.PaymentTransactionRepository;
import com.cambofreelance.webbackend.repository.UserSubscriptionRepository;
import com.cambofreelance.webbackend.services.SubscriptionService;
import jakarta.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SubscriptionJobs {

    private final UserSubscriptionRepository subscriptionRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final PaymentEventLogRepository eventLogRepository;
    private final SubscriptionService subscriptionService;

    /** Daily 00:10 — flip ACTIVE subscriptions past their expiry to EXPIRED. */
    @Scheduled(cron = "0 10 0 * * *")
    @Transactional
    public void expireSubscriptions() {
        Date now = new Date();
        var expired = subscriptionRepository.findBySubStatusAndExpiresAtBefore(Constants.SUB_ACTIVE, now);
        for (var sub : expired) {
            sub.setSubStatus(Constants.SUB_EXPIRED);
            sub.setUpdatedAt(now);
            sub.setUpdatedBy(Constants.SYSTEM);
            subscriptionRepository.save(sub);
        }
        if (!expired.isEmpty()) {
            log.info("[Jobs] expired {} subscriptions", expired.size());
        }
    }

    /** Hourly — PENDING payments older than 24h can no longer complete on PayWay: mark EXPIRED. */
    @Scheduled(cron = "0 5 * * * *")
    @Transactional
    public void expireStalePendingPayments() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -24);
        var stale = transactionRepository.findByPaymentStatusAndCreatedAtBefore(
            Constants.PAY_PENDING, cal.getTime());
        for (var tx : stale) {
            tx.setPaymentStatus(Constants.PAY_EXPIRED);
            tx.setUpdatedAt(new Date());
            tx.setUpdatedBy(Constants.SYSTEM);
            transactionRepository.save(tx);

            PaymentEventLogEntity e = new PaymentEventLogEntity();
            e.setId(UUID.randomUUID().toString());
            e.setTransactionId(tx.getId());
            e.setFromStatus(Constants.PAY_PENDING);
            e.setToStatus(Constants.PAY_EXPIRED);
            e.setSource(Constants.PAY_SRC_JOB);
            e.setNote("Pending payment older than 24h");
            e.setActor(Constants.SYSTEM);
            eventLogRepository.save(e);
        }
        if (!stale.isEmpty()) {
            log.info("[Jobs] expired {} stale pending payments", stale.size());
        }
    }

    /**
     * Daily 00:20 (after expireSubscriptions at 00:10, so a sub that just lapsed tonight isn't
     * double-processed) — attempt Card-on-File renewal charges for eligible subscriptions.
     * STUBBED: PaywayClient#chargeStoredToken always fails until ABA enables Card-on-File for
     * this merchant; this job exercises the opt-in/failure-handling/notification scaffolding
     * today so only the actual charge needs to change later.
     */
    @Scheduled(cron = "0 20 0 * * *")
    public void attemptAutoRenewals() {
        subscriptionService.attemptAutoRenewals();
    }

    /**
     * Daily 00:15 (after expireSubscriptions at 00:10, so a sub that lapsed overnight is already
     * EXPIRED and excluded) — remind admins of ACTIVE subscriptions crossing the 7/3/1-day expiry marks.
     */
    @Scheduled(cron = "0 15 0 * * *")
    public void notifyExpiringSubscriptions() {
        subscriptionService.notifyExpiringSubscriptions();
    }
}
