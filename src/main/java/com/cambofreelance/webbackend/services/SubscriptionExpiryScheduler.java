package com.cambofreelance.webbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionExpiryScheduler {

    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 5 0 * * *")
    public void run() {
        log.info("Running subscription expiry sweep");
        subscriptionService.runExpirySweep();
    }
}
