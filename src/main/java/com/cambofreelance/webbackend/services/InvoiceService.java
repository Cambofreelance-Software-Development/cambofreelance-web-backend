package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.response.InvoiceResponse;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;

public interface InvoiceService {

    /** billingPeriod is "yyyy-MM"; null defaults to the current month. */
    InvoiceResponse generateInvoice(String tenantId, String billingPeriod, BigDecimal tax);

    /** Scheduled sweep: issues this month's invoice for every active subscription that doesn't have one yet. */
    void generateMonthlyInvoicesForActiveSubscriptions();

    InvoiceResponse recordPayment(String invoiceId);

    InvoiceResponse getById(String id);

    List<InvoiceResponse> listForTenant(String tenantId);

    Page<InvoiceResponse> listAll(String status, String tenantId, int page, int size);
}
