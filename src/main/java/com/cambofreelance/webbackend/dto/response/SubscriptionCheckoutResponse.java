package com.cambofreelance.webbackend.dto.response;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

/**
 * Everything the frontend needs to open the ABA PayWay checkout:
 * form-POST the {@code formFields} to {@code checkoutUrl} (hosted view/popup).
 */
@Data
@Builder
public class SubscriptionCheckoutResponse {

    private String tranId;
    private String subscriptionId;
    private String checkoutUrl;
    private Map<String, String> formFields;
}
