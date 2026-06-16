package com.cambofreelance.webbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceScheduler {

    private final InvoiceService invoiceService;

    @Scheduled(cron = "0 10 0 1 * *")
    public void run() {
        log.info("Running monthly invoice generation");
        invoiceService.generateMonthlyInvoicesForActiveSubscriptions();
    }
}
