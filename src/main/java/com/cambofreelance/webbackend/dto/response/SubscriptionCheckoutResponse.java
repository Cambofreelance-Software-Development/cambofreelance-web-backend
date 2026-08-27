package com.cambofreelance.webbackend.dto.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/**
 * Everything the frontend needs to render the ABA PayWay KHQR checkout in-page:
 * show {@code qrImage}, then poll {@code /subscriptions/transactions/{tranId}/status}.
 *
 * <p>When {@code paymentRequired} is false (a $0 plan), the subscription is already ACTIVE and
 * there is no QR/transaction to pay or poll — {@code qrImage}/{@code qrString}/
 * {@code abapayDeeplink} are left null.
 */
@Data
@Builder
public class SubscriptionCheckoutResponse {

    private String tranId;
    private String subscriptionId;
    private BigDecimal amount;
    private String currency;
    /** data:image/png;base64,... ready to render directly */
    private String qrImage;
    private String qrString;
    /** Deep link to open the QR in the ABA Mobile app, when provided */
    private String abapayDeeplink;

    /** False for a $0 plan: the subscription was activated immediately, no payment needed. */
    private boolean paymentRequired;

    /** True when {@code amount} is a mid-cycle prorated upgrade charge, not the plan's full price */
    private boolean prorated;
    /** Credit from the unused remainder of the current plan, already netted into {@code amount} */
    private BigDecimal creditApplied;
    /** Days left in the current billing period used to compute the proration */
    private Long remainingDays;
}
