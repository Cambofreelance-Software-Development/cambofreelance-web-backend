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
}
