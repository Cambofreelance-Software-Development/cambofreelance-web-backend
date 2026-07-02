package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.BillingCycle;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.PaymentStatus;
import com.cambofreelance.webbackend.dto.request.SubscriptionCreateRequest;
import com.cambofreelance.webbackend.dto.response.SubscriptionDashboardResponse;
import com.cambofreelance.webbackend.dto.response.SubscriptionResponse;
import com.cambofreelance.webbackend.entities.SubscriptionEntity;
import com.cambofreelance.webbackend.entities.SubscriptionPackageEntity;
import com.cambofreelance.webbackend.entities.TenantEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.SubscriptionPackageRepository;
import com.cambofreelance.webbackend.repository.SubscriptionRepository;
import com.cambofreelance.webbackend.repository.TenantRepository;
import com.cambofreelance.webbackend.services.SubscriptionService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPackageRepository packageRepository;
    private final TenantRepository tenantRepository;

    @Override
    public Page<SubscriptionResponse> listAll(String status, String paymentStatus, String tenantId, int page, int size) {
        Specification<SubscriptionEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(status)) predicates.add(cb.equal(root.get("status"), status.trim()));
            if (StringUtils.hasText(paymentStatus)) predicates.add(cb.equal(root.get("paymentStatus"), paymentStatus.trim()));
            if (StringUtils.hasText(tenantId)) predicates.add(cb.equal(root.get("tenantId"), tenantId.trim()));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        return subscriptionRepository.findAll(spec, PageRequest.of(page, size, sort)).map(this::toResponse);
    }

    @Override
    public SubscriptionResponse getCurrentForTenant(String tenantId) {
        return subscriptionRepository.findByTenantIdAndStatus(tenantId, Constants.STATUS_ACTIVE)
            .map(this::toResponse)
            .orElse(null);
    }

    @Override
    public List<SubscriptionResponse> listHistoryForTenant(String tenantId) {
        return subscriptionRepository.findAllByTenantIdOrderByCreatedAtDesc(tenantId).stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public SubscriptionResponse subscribe(String tenantId, SubscriptionCreateRequest request) {
        TenantEntity tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new AppException("TENANT_NOT_FOUND", "Tenant not found: " + tenantId));
        ValidatedPlan plan = validatePlan(request);

        Date now = new Date();
        Date startDate = request.getStartDate() != null ? request.getStartDate() : now;
        Date endDate = addMonths(startDate, BillingCycle.months(request.getBillingCycle()));

        // Supersede any currently active subscription for this tenant
        subscriptionRepository.findByTenantIdAndStatus(tenantId, Constants.STATUS_ACTIVE).ifPresent(old -> {
            old.setStatus(Constants.STATUS_DELETE);
            old.setUpdatedAt(now);
            subscriptionRepository.save(old);
        });

        SubscriptionEntity entity = buildSubscriptionEntity(tenantId, plan, request, now);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setStatus(Constants.STATUS_ACTIVE);
        subscriptionRepository.save(entity);

        syncTenant(tenant, Constants.STATUS_ACTIVE, startDate, endDate, now);

        return toResponse(entity);
    }

    @Override
    @Transactional
    public SubscriptionResponse subscribeToFreePlan(String tenantId) {
        SubscriptionPackageEntity freePkg = packageRepository.findByCode("FREE")
            .orElseThrow(() -> new AppException("PACKAGE_NOT_FOUND", "FREE subscription package not found"));

        SubscriptionCreateRequest request = new SubscriptionCreateRequest();
        request.setPackageId(freePkg.getId());
        request.setBillingCycle(BillingCycle.MONTHLY);
        request.setAmount(BigDecimal.ZERO);
        request.setCurrency("USD");
        request.setAutoRenewal(false);
        return subscribe(tenantId, request);
    }

    @Override
    @Transactional
    public SubscriptionResponse createPending(String tenantId, SubscriptionCreateRequest request) {
        if (!tenantRepository.existsById(tenantId)) {
            throw new AppException("TENANT_NOT_FOUND", "Tenant not found: " + tenantId);
        }
        ValidatedPlan plan = validatePlan(request);
        SubscriptionEntity entity = buildSubscriptionEntity(tenantId, plan, request, new Date());
        entity.setStatus(Constants.STATUS_PENDING);
        subscriptionRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    public SubscriptionResponse activate(String subscriptionId) {
        SubscriptionEntity entity = findOrThrow(subscriptionId);
        if (!Constants.STATUS_PENDING.equals(entity.getStatus())) {
            throw new AppException("SUBSCRIPTION_NOT_PENDING", "Only a pending subscription can be activated");
        }
        Date now = new Date();
        Date endDate = addMonths(now, BillingCycle.months(entity.getBillingCycle()));
        entity.setStartDate(now);
        entity.setEndDate(endDate);
        entity.setStatus(Constants.STATUS_ACTIVE);
        entity.setUpdatedAt(now);
        subscriptionRepository.save(entity);

        tenantRepository.findById(entity.getTenantId())
            .ifPresent(tenant -> syncTenant(tenant, Constants.STATUS_ACTIVE, now, endDate, now));

        return toResponse(entity);
    }

    @Override
    @Transactional
    public void rejectPending(String tenantId) {
        subscriptionRepository.findByTenantIdAndStatus(tenantId, Constants.STATUS_PENDING).ifPresent(sub -> {
            sub.setStatus(Constants.STATUS_DELETE);
            sub.setUpdatedAt(new Date());
            subscriptionRepository.save(sub);
        });
    }

    private record ValidatedPlan(SubscriptionPackageEntity pkg, BigDecimal amount) {}

    private ValidatedPlan validatePlan(SubscriptionCreateRequest request) {
        SubscriptionPackageEntity pkg = packageRepository.findById(request.getPackageId())
            .orElseThrow(() -> new AppException("PACKAGE_NOT_FOUND", "Subscription package not found: " + request.getPackageId()));
        if (!BillingCycle.isValid(request.getBillingCycle())) {
            throw new AppException("INVALID_BILLING_CYCLE", "Billing cycle must be one of MONTHLY, QUARTERLY, YEARLY");
        }

        BigDecimal amount = request.getAmount();
        if (amount == null) {
            if (pkg.isCustomPricing() || pkg.getMonthlyPrice() == null) {
                throw new AppException("AMOUNT_REQUIRED", "Amount is required for custom-priced packages");
            }
            amount = pkg.getMonthlyPrice().multiply(BigDecimal.valueOf(BillingCycle.months(request.getBillingCycle())));
        }
        return new ValidatedPlan(pkg, amount);
    }

    private SubscriptionEntity buildSubscriptionEntity(
        String tenantId, ValidatedPlan plan, SubscriptionCreateRequest request, Date now) {
        SubscriptionEntity entity = new SubscriptionEntity();
        entity.setId(nextSubscriptionId());
        entity.setTenantId(tenantId);
        entity.setPackageId(plan.pkg().getId());
        entity.setBillingCycle(request.getBillingCycle().toUpperCase());
        entity.setAmount(plan.amount());
        entity.setCurrency(StringUtils.hasText(request.getCurrency()) ? request.getCurrency() : "USD");
        entity.setPaymentStatus(PaymentStatus.PENDING);
        entity.setAutoRenewal(request.getAutoRenewal() == null || request.getAutoRenewal());
        entity.setCreatedAt(now);
        return entity;
    }

    @Override
    @Transactional
    public SubscriptionResponse upgrade(String tenantId, SubscriptionCreateRequest request) {
        return changePlan(tenantId, request, true);
    }

    @Override
    @Transactional
    public SubscriptionResponse downgrade(String tenantId, SubscriptionCreateRequest request) {
        return changePlan(tenantId, request, false);
    }

    private SubscriptionResponse changePlan(String tenantId, SubscriptionCreateRequest request, boolean isUpgrade) {
        SubscriptionEntity current = subscriptionRepository.findByTenantIdAndStatus(tenantId, Constants.STATUS_ACTIVE)
            .orElseThrow(() -> new AppException("NO_ACTIVE_SUBSCRIPTION",
                "Tenant has no active subscription to " + (isUpgrade ? "upgrade" : "downgrade")));
        SubscriptionPackageEntity currentPkg = packageRepository.findById(current.getPackageId())
            .orElseThrow(() -> new AppException("PACKAGE_NOT_FOUND", "Current package not found"));
        SubscriptionPackageEntity newPkg = packageRepository.findById(request.getPackageId())
            .orElseThrow(() -> new AppException("PACKAGE_NOT_FOUND",
                "Subscription package not found: " + request.getPackageId()));

        int currentOrder = currentPkg.getSortOrder() != null ? currentPkg.getSortOrder() : 0;
        int newOrder = newPkg.getSortOrder() != null ? newPkg.getSortOrder() : 0;
        if (isUpgrade && newOrder <= currentOrder) {
            throw new AppException("NOT_AN_UPGRADE", "Selected package is not a higher tier than the current plan");
        }
        if (!isUpgrade && newOrder >= currentOrder) {
            throw new AppException("NOT_A_DOWNGRADE", "Selected package is not a lower tier than the current plan");
        }
        return subscribe(tenantId, request);
    }

    @Override
    @Transactional
    public SubscriptionResponse renew(String subscriptionId) {
        SubscriptionEntity entity = findOrThrow(subscriptionId);
        if (!Constants.STATUS_ACTIVE.equals(entity.getStatus())) {
            throw new AppException("SUBSCRIPTION_NOT_ACTIVE", "Only an active subscription can be renewed");
        }
        renewInPlace(entity, new Date());
        return toResponse(entity);
    }

    @Override
    @Transactional
    public SubscriptionResponse suspend(String subscriptionId) {
        SubscriptionEntity entity = findOrThrow(subscriptionId);
        if (!Constants.STATUS_ACTIVE.equals(entity.getStatus())) {
            throw new AppException("SUBSCRIPTION_NOT_ACTIVE", "Only an active subscription can be suspended");
        }
        Date now = new Date();
        entity.setStatus(Constants.STATUS_SUSPENDED);
        entity.setUpdatedAt(now);
        subscriptionRepository.save(entity);
        tenantRepository.findById(entity.getTenantId()).ifPresent(t -> {
            t.setStatus(Constants.STATUS_SUSPENDED);
            t.setUpdatedAt(now);
            tenantRepository.save(t);
        });
        return toResponse(entity);
    }

    @Override
    @Transactional
    public SubscriptionResponse resume(String subscriptionId) {
        SubscriptionEntity entity = findOrThrow(subscriptionId);
        if (!Constants.STATUS_SUSPENDED.equals(entity.getStatus())) {
            throw new AppException("SUBSCRIPTION_NOT_SUSPENDED", "Only a suspended subscription can be resumed");
        }
        Date now = new Date();
        entity.setStatus(Constants.STATUS_ACTIVE);
        entity.setUpdatedAt(now);
        subscriptionRepository.save(entity);
        tenantRepository.findById(entity.getTenantId())
            .ifPresent(t -> syncTenant(t, Constants.STATUS_ACTIVE, entity.getStartDate(), entity.getEndDate(), now));
        return toResponse(entity);
    }

    @Override
    @Transactional
    public SubscriptionResponse updatePaymentStatus(String subscriptionId, String paymentStatus) {
        if (!PaymentStatus.isValid(paymentStatus)) {
            throw new AppException("INVALID_PAYMENT_STATUS", "Payment status must be one of PENDING, PAID, OVERDUE");
        }
        SubscriptionEntity entity = findOrThrow(subscriptionId);
        entity.setPaymentStatus(paymentStatus.toUpperCase());
        entity.setUpdatedAt(new Date());
        subscriptionRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    public SubscriptionResponse updateAutoRenewal(String subscriptionId, boolean autoRenewal) {
        SubscriptionEntity entity = findOrThrow(subscriptionId);
        entity.setAutoRenewal(autoRenewal);
        entity.setUpdatedAt(new Date());
        subscriptionRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    public SubscriptionResponse cancel(String subscriptionId) {
        SubscriptionEntity entity = findOrThrow(subscriptionId);
        boolean wasActive = Constants.STATUS_ACTIVE.equals(entity.getStatus());
        Date now = new Date();
        entity.setStatus(Constants.STATUS_DELETE);
        entity.setUpdatedAt(now);
        subscriptionRepository.save(entity);

        if (wasActive) {
            tenantRepository.findById(entity.getTenantId()).ifPresent(tenant -> {
                tenant.setStatus(Constants.STATUS_SUSPENDED);
                tenant.setUpdatedAt(now);
                tenantRepository.save(tenant);
            });
        }
        return toResponse(entity);
    }

    @Override
    @Transactional
    public void runExpirySweep() {
        Date now = new Date();
        List<SubscriptionEntity> dueForAction =
            subscriptionRepository.findByStatusAndEndDateLessThan(Constants.STATUS_ACTIVE, now);

        for (SubscriptionEntity sub : dueForAction) {
            if (sub.isAutoRenewal()) {
                renewInPlace(sub, now);
            } else {
                sub.setStatus(Constants.STATUS_EXPIRED);
                sub.setUpdatedAt(now);
                subscriptionRepository.save(sub);
                tenantRepository.findById(sub.getTenantId()).ifPresent(tenant ->
                    syncTenant(tenant, Constants.STATUS_EXPIRED, tenant.getPlanStartDate(), sub.getEndDate(), now));
            }
        }
    }

    private void renewInPlace(SubscriptionEntity sub, Date now) {
        Date newStart = sub.getEndDate();
        Date newEnd = addMonths(newStart, BillingCycle.months(sub.getBillingCycle()));
        sub.setStartDate(newStart);
        sub.setEndDate(newEnd);
        sub.setPaymentStatus(PaymentStatus.PENDING);
        sub.setUpdatedAt(now);
        subscriptionRepository.save(sub);
        tenantRepository.findById(sub.getTenantId())
            .ifPresent(tenant -> syncTenant(tenant, Constants.STATUS_ACTIVE, newStart, newEnd, now));
    }

    private void syncTenant(TenantEntity tenant, String status, Date planStartDate, Date planExpiredDate, Date now) {
        tenant.setStatus(status);
        tenant.setPlanStartDate(planStartDate);
        tenant.setPlanExpiredDate(planExpiredDate);
        tenant.setUpdatedAt(now);
        tenantRepository.save(tenant);
    }

    @Override
    public SubscriptionDashboardResponse getDashboardStats() {
        List<SubscriptionEntity> activeSubs = subscriptionRepository.findByStatus(Constants.STATUS_ACTIVE);
        long activeCount = activeSubs.size();
        long expiredCount = subscriptionRepository.countByStatus(Constants.STATUS_EXPIRED);

        Date now = new Date();
        Date in7Days = addDays(now, 7);
        Date in30Days = addDays(now, 30);

        BigDecimal mrr = BigDecimal.ZERO;
        Map<String, Long> packageDistribution = new LinkedHashMap<>();
        Map<String, BigDecimal> revenueByPackage = new LinkedHashMap<>();
        Map<String, String> packageNames = new LinkedHashMap<>();
        packageRepository.findAll().forEach(p -> packageNames.put(p.getId(), p.getName()));

        for (SubscriptionEntity sub : activeSubs) {
            BigDecimal monthlyAmount = sub.getAmount()
                .divide(BigDecimal.valueOf(BillingCycle.months(sub.getBillingCycle())), 2, RoundingMode.HALF_UP);
            mrr = mrr.add(monthlyAmount);
            String packageName = packageNames.getOrDefault(sub.getPackageId(), "Unknown");
            packageDistribution.merge(packageName, 1L, Long::sum);
            revenueByPackage.merge(packageName, monthlyAmount, BigDecimal::add);
        }

        long totalTenants = tenantRepository.count() - tenantRepository.countByStatus(Constants.STATUS_DELETE);
        double renewalRate = (activeCount + expiredCount) == 0
            ? 0.0
            : Math.round((activeCount * 10000.0) / (activeCount + expiredCount)) / 100.0;

        List<SubscriptionResponse> expiryAlerts = subscriptionRepository
            .findByStatusAndEndDateBetween(Constants.STATUS_ACTIVE, now, in7Days)
            .stream().map(this::toResponse).toList();
        long expiringSubscriptions = subscriptionRepository
            .findByStatusAndEndDateBetween(Constants.STATUS_ACTIVE, now, in30Days).size();

        return SubscriptionDashboardResponse.builder()
            .activeSubscriptions(activeCount)
            .expiringSubscriptions(expiringSubscriptions)
            .monthlyRecurringRevenue(mrr)
            .totalTenants(totalTenants)
            .packageDistribution(packageDistribution)
            .revenueByPackage(revenueByPackage)
            .renewalRate(renewalRate)
            .expiryAlerts(expiryAlerts)
            .build();
    }

    private Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    private String nextSubscriptionId() {
        long seq = subscriptionRepository.nextSubscriptionSequence();
        return String.format("SUB-%08d", seq);
    }

    private SubscriptionEntity findOrThrow(String subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new AppException("SUBSCRIPTION_NOT_FOUND", "Subscription not found: " + subscriptionId));
    }

    private SubscriptionResponse toResponse(SubscriptionEntity entity) {
        String tenantName = tenantRepository.findById(entity.getTenantId())
            .map(TenantEntity::getName).orElse(null);
        String packageName = packageRepository.findById(entity.getPackageId())
            .map(SubscriptionPackageEntity::getName).orElse(null);
        return SubscriptionResponse.from(entity, tenantName, packageName);
    }
}
