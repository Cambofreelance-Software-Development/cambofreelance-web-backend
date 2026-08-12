package com.cambofreelance.webbackend.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MySubscriptionResponse {

    /** PEN / APR / REJ — subscription checkout requires APR */
    private String approvalStatus;
    private boolean canSubscribe;
    private SubscriptionResponse activeSubscription;
    private List<SubscriptionResponse> history;
}
