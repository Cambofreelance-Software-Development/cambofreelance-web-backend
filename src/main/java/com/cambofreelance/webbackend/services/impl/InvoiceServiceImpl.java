package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.BillingCycle;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.InvoiceStatus;
import com.cambofreelance.webbackend.constants.PaymentStatus;
import com.cambofreelance.webbackend.dto.response.InvoiceResponse;
import com.cambofreelance.webbackend.entities.InvoiceEntity;
import com.cambofreelance.webbackend.entities.SubscriptionEntity;
import com.cambofreelance.webbackend.entities.SubscriptionPackageEntity;
import com.cambofreelance.webbackend.entities.TenantEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.InvoiceRepository;
import com.cambofreelance.webbackend.repository.SubscriptionPackageRepository;
import com.cambofreelance.webbackend.repository.SubscriptionRepository;
import com.cambofreelance.webbackend.repository.TenantRepository;
import com.cambofreelance.webbackend.services.InvoiceService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPackageRepository packageRepository;
    private final TenantRepository tenantRepository;

    @Override
    @Transactional
    public InvoiceResponse generateInvoice(String tenantId, String billingPeriod, BigDecimal tax) {
        SubscriptionEntity subscription = subscriptionRepository
            .findByTenantIdAndStatus(tenantId, Constants.STATUS_ACTIVE)
            .orElseThrow(() -> new AppException("NO_ACTIVE_SUBSCRIPTION", "Tenant has no active subscription"));

        YearMonth period = StringUtils.hasText(billingPeriod) ? YearMonth.parse(billingPeriod) : YearMonth.now();
        Date periodStart = java.sql.Date.valueOf(period.atDay(1));

        if (invoiceRepository.existsByTenantIdAndBillingPeriod(tenantId, periodStart)) {
            throw new AppException("INVOICE_ALREADY_EXISTS",
                "An invoice for " + period + " already exists for this tenant");
        }

        InvoiceEntity entity = buildInvoice(subscription, periodStart, tax);
        invoiceRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    public void generateMonthlyInvoicesForActiveSubscriptions() {
        YearMonth period = YearMonth.now();
        Date periodStart = java.sql.Date.valueOf(period.atDay(1));

        List<SubscriptionEntity> active = subscriptionRepository.findAll((root, query, cb) ->
            cb.equal(root.get("status"), Constants.STATUS_ACTIVE));

        for (SubscriptionEntity subscription : active) {
            if (invoiceRepository.existsByTenantIdAndBillingPeriod(subscription.getTenantId(), periodStart)) {
                continue;
            }
            try {
                invoiceRepository.save(buildInvoice(subscription, periodStart, null));
            } catch (Exception ex) {
                log.error("Failed to auto-generate invoice for tenantId={}", subscription.getTenantId(), ex);
            }
        }
    }

    @Override
    @Transactional
    public InvoiceResponse recordPayment(String invoiceId) {
        InvoiceEntity entity = findOrThrow(invoiceId);
        Date now = new Date();
        entity.setInvoiceStatus(InvoiceStatus.PAID);
        entity.setPaidAt(now);
        entity.setUpdatedAt(now);
        invoiceRepository.save(entity);

        subscriptionRepository.findByTenantIdAndStatus(entity.getTenantId(), Constants.STATUS_ACTIVE)
            .filter(s -> s.getId().equals(entity.getSubscriptionId()))
            .ifPresent(s -> {
                s.setPaymentStatus(PaymentStatus.PAID);
                s.setUpdatedAt(now);
                subscriptionRepository.save(s);
            });

        return toResponse(entity);
    }

    @Override
    public InvoiceResponse getById(String id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public List<InvoiceResponse> listForTenant(String tenantId) {
        return invoiceRepository.findAllByTenantIdOrderByBillingPeriodDesc(tenantId).stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    public Page<InvoiceResponse> listAll(String status, String tenantId, int page, int size) {
        Specification<InvoiceEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(status)) predicates.add(cb.equal(root.get("invoiceStatus"), status.trim()));
            if (StringUtils.hasText(tenantId)) predicates.add(cb.equal(root.get("tenantId"), tenantId.trim()));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(Sort.Order.desc("billingPeriod"));
        return invoiceRepository.findAll(spec, PageRequest.of(page, size, sort)).map(this::toResponse);
    }

    private InvoiceEntity buildInvoice(SubscriptionEntity subscription, Date periodStart, BigDecimal tax) {
        BigDecimal monthlyAmount = subscription.getAmount()
            .divide(BigDecimal.valueOf(BillingCycle.months(subscription.getBillingCycle())), 2, RoundingMode.HALF_UP);
        BigDecimal taxAmount = tax != null ? tax : BigDecimal.ZERO;

        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(nextInvoiceId());
        entity.setTenantId(subscription.getTenantId());
        entity.setSubscriptionId(subscription.getId());
        entity.setPackageId(subscription.getPackageId());
        entity.setBillingPeriod(periodStart);
        entity.setAmount(monthlyAmount);
        entity.setTax(taxAmount);
        entity.setTotalAmount(monthlyAmount.add(taxAmount));
        entity.setCurrency(subscription.getCurrency());
        entity.setInvoiceStatus(InvoiceStatus.UNPAID);
        entity.setCreatedAt(new Date());
        return entity;
    }

    private String nextInvoiceId() {
        long seq = invoiceRepository.nextInvoiceSequence();
        int year = YearMonth.now().getYear();
        return String.format("INV-%d%06d", year, seq);
    }

    private InvoiceEntity findOrThrow(String id) {
        return invoiceRepository.findById(id)
            .orElseThrow(() -> new AppException("INVOICE_NOT_FOUND", "Invoice not found: " + id));
    }

    private InvoiceResponse toResponse(InvoiceEntity entity) {
        String tenantName = tenantRepository.findById(entity.getTenantId())
            .map(TenantEntity::getName).orElse(null);
        String packageName = packageRepository.findById(entity.getPackageId())
            .map(SubscriptionPackageEntity::getName).orElse(null);
        return InvoiceResponse.from(entity, tenantName, packageName);
    }
}
