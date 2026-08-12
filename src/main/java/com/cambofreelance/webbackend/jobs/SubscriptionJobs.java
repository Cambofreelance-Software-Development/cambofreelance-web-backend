package com.cambofreelance.webbackend.jobs;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.entities.PaymentEventLogEntity;
import com.cambofreelance.webbackend.repository.PaymentEventLogRepository;
import com.cambofreelance.webbackend.repository.PaymentTransactionRepository;
import com.cambofreelance.webbackend.repository.UserSubscriptionRepository;
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
}
