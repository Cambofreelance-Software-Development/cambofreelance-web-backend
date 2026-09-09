package com.cambofreelance.webbackend.dto.response;

import lombok.Builder;
import lombok.Data;

/** One row of the admin "Approved Partner Clients" directory — a referred client plus which
 *  approved partner they're attributed to. */
@Data
@Builder
public class AdminReferredClientResponse {

    private String partnerApplicationId;
    private String partnerRef;
    private String partnerCompanyName;

    private PartnerPortalResponse.ReferredClient client;
}
