package com.cambofreelance.webbackend.dto.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BillingSettingsRequest {

    private BigDecimal taxRate;
    private String taxLabel;
    private String invoicePrefix;
}
