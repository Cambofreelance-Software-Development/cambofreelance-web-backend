package com.cambofreelance.webbackend.dto.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class GenerateInvoiceRequest {

    /** "yyyy-MM"; defaults to the current month when omitted. */
    private String billingPeriod;

    private BigDecimal tax;
}
