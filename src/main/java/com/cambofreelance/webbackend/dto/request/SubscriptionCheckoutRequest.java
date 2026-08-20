package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubscriptionCheckoutRequest {

    @NotBlank
    private String planId;

    /** MONTHLY (default) or YEARLY */
    private String billingCycle;

    /** Optional PayWay payment_option: cards, abapay_khqr, alipay, wechat... omit to let PayWay show all */
    private String paymentOption;

    /** true = renew/extend the current active subscription instead of starting a new one */
    private Boolean renew;

    /** Opt in to Card-on-File auto-renewal. Only honored on a new subscription, and only when
     *  payway.cof-enabled=true — otherwise rejected with AUTO_RENEW_NOT_AVAILABLE. */
    private Boolean autoRenew;
}
