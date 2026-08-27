package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.SubscriptionCheckoutRequest;
import com.cambofreelance.webbackend.dto.response.MySubscriptionResponse;
import com.cambofreelance.webbackend.dto.response.PaymentTransactionResponse;
import com.cambofreelance.webbackend.dto.response.SubscriptionCheckoutResponse;
import com.cambofreelance.webbackend.dto.response.SubscriptionResponse;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface SubscriptionService {

    SubscriptionCheckoutResponse createCheckout(String userId, SubscriptionCheckoutRequest request);

    /** Handles the ABA PayWay pushback — verifies server-to-server before activating. Must be idempotent. */
    void handlePaywayCallback(Map<String, String> params, String rawPayload);

    /** Verifies a pending transaction against PayWay and returns its current state (poll from success page). */
    PaymentTransactionResponse checkTransaction(String userId, String tranId);

    MySubscriptionResponse getMySubscription(String userId);

    java.util.List<PaymentTransactionResponse> getMyPayments(String userId);

    Page<SubscriptionResponse> adminListSubscriptions(int page, int size);

    Page<PaymentTransactionResponse> adminListPayments(int page, int size);

    /** Admin manually settles a payment that PayWay could not confirm automatically. */
    PaymentTransactionResponse manualVerify(String tranId, boolean approve, String note, String adminId);

    /** Admin-initiated refund of a settled (APPROVED) payment. Idempotent if already refunded. */
    PaymentTransactionResponse refundPayment(String tranId, String reason, String adminId);

    java.util.List<com.cambofreelance.webbackend.dto.response.PaymentEventResponse> getPaymentLogs(String tranId);

    /** Self-service opt in/out of Card-on-File auto-renewal on the caller's active subscription. */
    SubscriptionResponse setAutoRenew(String userId, boolean autoRenew);

    /** Admin override — e.g. a support request to turn off auto-renew for a customer. */
    SubscriptionResponse adminSetAutoRenew(String subscriptionId, boolean autoRenew, String adminId);

    /** Scheduled by SubscriptionJobs — attempts stored-token renewal for subscriptions nearing expiry. */
    void attemptAutoRenewals();
}
