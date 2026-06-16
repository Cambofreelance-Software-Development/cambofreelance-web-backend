package com.cambofreelance.webbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class SubscriptionCreateRequest {

    @NotBlank(message = "Package is required")
    private String packageId;

    @NotBlank(message = "Billing cycle is required")
    private String billingCycle;

    /** Required when the package uses custom pricing; otherwise computed from the package's monthly price. */
    private BigDecimal amount;

    private String currency;

    private Boolean autoRenewal;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
}
