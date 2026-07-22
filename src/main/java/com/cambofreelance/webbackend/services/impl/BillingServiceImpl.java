package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.BillingSettingsRequest;
import com.cambofreelance.webbackend.dto.response.InvoiceResponse;
import com.cambofreelance.webbackend.entities.BillingSettingEntity;
import com.cambofreelance.webbackend.entities.InvoiceEntity;
import com.cambofreelance.webbackend.entities.PaymentTransactionEntity;
import com.cambofreelance.webbackend.repository.BillingSettingRepository;
import com.cambofreelance.webbackend.repository.InvoiceRepository;
import com.cambofreelance.webbackend.repository.PaymentTransactionRepository;
import com.cambofreelance.webbackend.services.BillingService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final InvoiceRepository invoiceRepository;
    private final BillingSettingRepository settingRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final com.cambofreelance.webbackend.repository.UserRepository userRepository;
    private final com.cambofreelance.webbackend.repository.ClientRepository clientRepository;
    private final com.cambofreelance.webbackend.repository.PricingPlanRepository planRepository;
    private final com.cambofreelance.webbackend.repository.UserSubscriptionRepository subscriptionRepository;

    // Seller identity printed on invoices. Sourced from CMS settings later if needed.
    private static final String SELLER_NAME = "SOPPOS System";
    private static final String SELLER_EMAIL = "billing@soppossystem.com";
    private static final String SELLER_ADDRESS = "Phnom Penh, Cambodia";

    @Override
    @Transactional
    public InvoiceResponse issueInvoiceForPayment(PaymentTransactionEntity tx) {
        InvoiceEntity existing = invoiceRepository.findByTransactionId(tx.getId()).orElse(null);
        if (existing != null) {
            if (!Constants.INV_PAID.equals(existing.getInvoiceStatus())) {
                existing.setInvoiceStatus(Constants.INV_PAID);
                existing.setPaidAt(new Date());
                existing.setUpdatedAt(new Date());
                invoiceRepository.save(existing);
            }
            return toResponse(existing);
        }

        BillingSettingEntity settings = getSettings();
        BigDecimal taxRate = settings.getTaxRate() != null ? settings.getTaxRate() : BigDecimal.ZERO;
        // Payment amount is tax-inclusive: derive subtotal backwards so total == amount paid.
        BigDecimal total = tx.getAmount().setScale(2, RoundingMode.HALF_UP);
        BigDecimal divisor = BigDecimal.ONE.add(taxRate.movePointLeft(2));
        BigDecimal subtotal = total.divide(divisor, 2, RoundingMode.HALF_UP);
        BigDecimal taxAmount = total.subtract(subtotal);

        InvoiceEntity inv = new InvoiceEntity();
        inv.setId(UUID.randomUUID().toString());
        inv.setInvoiceNo(nextInvoiceNo(settings));
        inv.setUserId(tx.getUserId());
        inv.setSubscriptionId(tx.getSubscriptionId());
        inv.setTransactionId(tx.getId());
        inv.setSubtotal(subtotal);
        inv.setTaxRate(taxRate);
        inv.setTaxAmount(taxAmount);
        inv.setTotal(total);
        inv.setCurrency(tx.getCurrency());
        inv.setInvoiceStatus(Constants.INV_PAID);
        inv.setIssuedAt(new Date());
        inv.setPaidAt(new Date());
        invoiceRepository.save(inv);
        log.info("[Billing] issued invoice {} for tran {}", inv.getInvoiceNo(), tx.getTranId());
        return toResponse(inv);
    }

    @Override
    @Transactional
    public void markInvoiceRefunded(String transactionId) {
        invoiceRepository.findByTransactionId(transactionId).ifPresent(inv -> {
            inv.setInvoiceStatus(Constants.INV_REFUNDED);
            inv.setUpdatedAt(new Date());
            invoiceRepository.save(inv);
        });
    }

    @Override
    public List<InvoiceResponse> listMyInvoices(String userId) {
        return invoiceRepository.findByUserIdOrderByIssuedAtDesc(userId).stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    public com.cambofreelance.webbackend.dto.response.InvoiceDetailResponse getMyInvoiceDetail(String userId, String invoiceId) {
        InvoiceEntity inv = invoiceRepository.findById(invoiceId)
            .filter(i -> i.getUserId().equals(userId))
            .orElseThrow(this::invoiceNotFound);
        return buildDetail(inv);
    }

    @Override
    public com.cambofreelance.webbackend.dto.response.InvoiceDetailResponse adminGetInvoiceDetail(String invoiceId) {
        InvoiceEntity inv = invoiceRepository.findById(invoiceId).orElseThrow(this::invoiceNotFound);
        return buildDetail(inv);
    }

    private com.cambofreelance.webbackend.logger.exceptions.AppException invoiceNotFound() {
        var ex = new com.cambofreelance.webbackend.logger.exceptions.AppException(
            com.cambofreelance.webbackend.constants.ErrorCode.PAYMENT_NOT_FOUND, "Invoice not found");
        ex.setHttpStatus(org.springframework.http.HttpStatus.NOT_FOUND);
        return ex;
    }

    private com.cambofreelance.webbackend.dto.response.InvoiceDetailResponse buildDetail(InvoiceEntity inv) {
        var user = userRepository.findById(inv.getUserId()).orElse(null);
        var client = clientRepository.findByUserId(inv.getUserId()).orElse(null);
        var tx = inv.getTransactionId() != null ? transactionRepository.findById(inv.getTransactionId()).orElse(null) : null;
        var sub = inv.getSubscriptionId() != null ? subscriptionRepository.findById(inv.getSubscriptionId()).orElse(null) : null;
        String planName = sub != null
            ? planRepository.findById(sub.getPlanId()).map(p -> p.getName()).orElse(null)
            : null;
        String taxLabel = getSettings().getTaxLabel();

        return com.cambofreelance.webbackend.dto.response.InvoiceDetailResponse.builder()
            .id(inv.getId())
            .invoiceNo(inv.getInvoiceNo())
            .currency(inv.getCurrency())
            .invoiceStatus(inv.getInvoiceStatus())
            .subtotal(inv.getSubtotal())
            .taxRate(inv.getTaxRate())
            .taxAmount(inv.getTaxAmount())
            .total(inv.getTotal())
            .taxLabel(taxLabel)
            .issuedAt(inv.getIssuedAt())
            .paidAt(inv.getPaidAt())
            .planName(planName)
            .billingCycle(sub != null ? sub.getBillingCycle() : null)
            .tranId(tx != null ? tx.getTranId() : null)
            .apv(tx != null ? tx.getApv() : null)
            .paymentOption(tx != null ? tx.getPaymentOption() : null)
            .customerName(user != null ? user.getUsername() : null)
            .customerEmail(user != null ? user.getEmail() : null)
            .companyName(client != null ? client.getCompanyName() : null)
            .companyPhone(client != null ? client.getCompanyPhone() : null)
            .address(client != null ? client.getAddress() : null)
            .city(client != null ? client.getCity() : null)
            .country(client != null ? client.getCountry() : null)
            .sellerName(SELLER_NAME)
            .sellerEmail(SELLER_EMAIL)
            .sellerAddress(SELLER_ADDRESS)
            .build();
    }

    @Override
    public Page<InvoiceResponse> adminListInvoices(String invoiceStatus, int page, int size) {
        Page<InvoiceEntity> result = StringUtils.hasText(invoiceStatus)
            ? invoiceRepository.findByInvoiceStatusOrderByIssuedAtDesc(invoiceStatus, PageRequest.of(page, size))
            : invoiceRepository.findAllByOrderByIssuedAtDesc(PageRequest.of(page, size));
        return result.map(this::toResponse);
    }

    @Override
    public BillingSettingEntity getSettings() {
        return settingRepository.findAll().stream().findFirst().orElseGet(() -> {
            BillingSettingEntity s = new BillingSettingEntity();
            s.setId(UUID.randomUUID().toString());
            s.setTaxRate(BigDecimal.ZERO);
            s.setTaxLabel("VAT");
            s.setInvoicePrefix("INV");
            return settingRepository.save(s);
        });
    }

    @Override
    @Transactional
    public BillingSettingEntity updateSettings(BillingSettingsRequest request, String adminId) {
        BillingSettingEntity s = getSettings();
        if (request.getTaxRate() != null)                  s.setTaxRate(request.getTaxRate());
        if (StringUtils.hasText(request.getTaxLabel()))     s.setTaxLabel(request.getTaxLabel());
        if (StringUtils.hasText(request.getInvoicePrefix()))s.setInvoicePrefix(request.getInvoicePrefix());
        s.setUpdatedBy(adminId);
        s.setUpdatedAt(new Date());
        return settingRepository.save(s);
    }

    @Override
    public Map<String, Object> summary() {
        List<PaymentTransactionEntity> all = transactionRepository.findAll();
        Map<String, Long> countByStatus = new LinkedHashMap<>();
        Map<String, BigDecimal> revenueByMonth = new LinkedHashMap<>();
        BigDecimal totalPaid = BigDecimal.ZERO;
        SimpleDateFormat month = new SimpleDateFormat("yyyy-MM");

        for (PaymentTransactionEntity tx : all) {
            countByStatus.merge(tx.getPaymentStatus(), 1L, Long::sum);
            if (Constants.PAY_APPROVED.equals(tx.getPaymentStatus())) {
                totalPaid = totalPaid.add(tx.getAmount());
                String key = month.format(tx.getCreatedAt() != null ? tx.getCreatedAt() : new Date());
                revenueByMonth.merge(key, tx.getAmount(), BigDecimal::add);
            }
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("totalPaid", totalPaid);
        out.put("countByStatus", countByStatus);
        out.put("revenueByMonth", revenueByMonth);
        out.put("invoiceCount", invoiceRepository.count());
        return out;
    }

    private synchronized String nextInvoiceNo(BillingSettingEntity settings) {
        String prefix = (settings.getInvoicePrefix() != null ? settings.getInvoicePrefix() : "INV")
            + "-" + new SimpleDateFormat("yyyyMM").format(new Date()) + "-";
        long seq = invoiceRepository.countByInvoiceNoStartingWith(prefix) + 1;
        return prefix + String.format("%04d", seq);
    }

    private InvoiceResponse toResponse(InvoiceEntity inv) {
        return InvoiceResponse.builder()
            .id(inv.getId())
            .invoiceNo(inv.getInvoiceNo())
            .userId(inv.getUserId())
            .subscriptionId(inv.getSubscriptionId())
            .transactionId(inv.getTransactionId())
            .subtotal(inv.getSubtotal())
            .taxRate(inv.getTaxRate())
            .taxAmount(inv.getTaxAmount())
            .total(inv.getTotal())
            .currency(inv.getCurrency())
            .invoiceStatus(inv.getInvoiceStatus())
            .issuedAt(inv.getIssuedAt())
            .paidAt(inv.getPaidAt())
            .build();
    }
}
