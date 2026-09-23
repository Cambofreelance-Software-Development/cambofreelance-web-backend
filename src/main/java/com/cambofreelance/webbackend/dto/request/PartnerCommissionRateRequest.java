package com.cambofreelance.webbackend.dto.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PartnerCommissionRateRequest {

    /** New commission rate as a fraction, e.g. 0.15 for 15%. Null clears the override and
     *  reverts the partner to the tier-derived default rate. */
    private BigDecimal rate;
}
