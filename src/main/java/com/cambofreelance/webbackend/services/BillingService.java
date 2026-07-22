package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.BillingSettingsRequest;
import com.cambofreelance.webbackend.dto.response.InvoiceResponse;
import com.cambofreelance.webbackend.entities.BillingSettingEntity;
import com.cambofreelance.webbackend.entities.PaymentTransactionEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface BillingService {

    /** Issues (or returns the existing) invoice for an approved payment and marks it PAID. */
    InvoiceResponse issueInvoiceForPayment(PaymentTransactionEntity tx);

    /** Marks the invoice tied to a refunded transaction as REFUNDED (no-op when absent). */
    void markInvoiceRefunded(String transactionId);

    List<InvoiceResponse> listMyInvoices(String userId);

    /** Print-ready detail for an invoice the caller owns (404 otherwise). */
    com.cambofreelance.webbackend.dto.response.InvoiceDetailResponse getMyInvoiceDetail(String userId, String invoiceId);

    /** Print-ready detail for any invoice (admin). */
    com.cambofreelance.webbackend.dto.response.InvoiceDetailResponse adminGetInvoiceDetail(String invoiceId);

    Page<InvoiceResponse> adminListInvoices(String invoiceStatus, int page, int size);

    BillingSettingEntity getSettings();

    BillingSettingEntity updateSettings(BillingSettingsRequest request, String adminId);

    /** Billing report: totals + counts by payment status and by month. */
    Map<String, Object> summary();
}
