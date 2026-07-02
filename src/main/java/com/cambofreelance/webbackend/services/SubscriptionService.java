package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.SubscriptionCreateRequest;
import com.cambofreelance.webbackend.dto.response.SubscriptionDashboardResponse;
import com.cambofreelance.webbackend.dto.response.SubscriptionResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface SubscriptionService {

    Page<SubscriptionResponse> listAll(String status, String paymentStatus, String tenantId, int page, int size);

    /** Null when the tenant has never subscribed to a package. */
    SubscriptionResponse getCurrentForTenant(String tenantId);

    List<SubscriptionResponse> listHistoryForTenant(String tenantId);

    SubscriptionResponse subscribe(String tenantId, SubscriptionCreateRequest request);

    /** Auto-assigns the FREE package for 1 month, activated immediately. Used when an admin creates a new tenant. */
    SubscriptionResponse subscribeToFreePlan(String tenantId);

    /** Used by tenant self-registration: validates the package/cycle/amount but stays PEN, no dates, no tenant sync. */
    SubscriptionResponse createPending(String tenantId, SubscriptionCreateRequest request);

    /** Used on registration approval: computes start/end dates from now, flips PEN to ACT, and syncs the tenant. */
    SubscriptionResponse activate(String subscriptionId);

    /** Used on registration rejection: marks the tenant's PEN subscription as DEL. */
    void rejectPending(String tenantId);

    /** Requires an existing active subscription and a target package with a higher sort order. */
    SubscriptionResponse upgrade(String tenantId, SubscriptionCreateRequest request);

    /** Requires an existing active subscription and a target package with a lower sort order. */
    SubscriptionResponse downgrade(String tenantId, SubscriptionCreateRequest request);

    /** Extends the current active subscription by one billing cycle on demand. */
    SubscriptionResponse renew(String subscriptionId);

    /** Reversible pause: subscription and tenant both move to SUS. */
    SubscriptionResponse suspend(String subscriptionId);

    /** Reverses a suspend back to ACT. */
    SubscriptionResponse resume(String subscriptionId);

    SubscriptionResponse updatePaymentStatus(String subscriptionId, String paymentStatus);

    SubscriptionResponse updateAutoRenewal(String subscriptionId, boolean autoRenewal);

    /** Permanent end: subscription is retired (DEL) and the tenant is suspended until a new plan is subscribed. */
    SubscriptionResponse cancel(String subscriptionId);

    /** Scheduled sweep: renews or expires ACT subscriptions past their end date. */
    void runExpirySweep();

    SubscriptionDashboardResponse getDashboardStats();
}
