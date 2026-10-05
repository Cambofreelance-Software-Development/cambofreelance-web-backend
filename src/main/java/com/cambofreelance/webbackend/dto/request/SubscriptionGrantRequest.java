package com.cambofreelance.webbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

/**
 * Admin grants a subscription to an EXISTING client (already on SOP POS / paid offline) —
 * no checkout and no payment transaction. Dates are calendar days in Asia/Phnom_Penh:
 * the period runs from the start of {@code startDate} to the end of {@code endDate}.
 */
@Data
public class SubscriptionGrantRequest {

    @NotBlank(message = "User is required")
    private String userId;

    @NotBlank(message = "Plan is required")
    private String planId;

    /** YEARLY (default) / MONTHLY */
    @Pattern(regexp = "YEARLY|MONTHLY", message = "Billing cycle must be YEARLY or MONTHLY")
    private String billingCycle;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /** Defaults to the plan's price for the billing cycle. */
    @DecimalMin(value = "0", message = "Price must not be negative")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 digits and 2 decimals")
    private BigDecimal price;

    @Size(max = 500, message = "Note must be at most 500 characters")
    private String note;

    /** LINK_EXISTING / PROVISION_NEW / LATER (default) */
    @Pattern(regexp = "LINK_EXISTING|PROVISION_NEW|LATER", message = "POS mode must be LINK_EXISTING, PROVISION_NEW or LATER")
    private String posMode;

    @Size(max = 64)
    private String posRegistrationId;

    @Size(max = 32)
    private String posClientCode;

    @Size(max = 255)
    private String posBackendUrl;

    @Size(max = 255)
    private String posEmenuUrl;

    @Size(max = 128)
    private String posRootUser;

    @Size(max = 255)
    private String posRootPassword;

    /** Partner PTR code (or any user's referral code) to attribute this client to. */
    @Size(max = 50)
    private String referrerCode;

    @Size(max = 255)
    private String companyName;
}
