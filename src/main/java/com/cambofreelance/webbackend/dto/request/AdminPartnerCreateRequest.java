package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Admin onboards an existing (offline) partner directly — same form as the public application,
 *  approved immediately. {@code agreementAccepted} is implied and may be omitted. */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminPartnerCreateRequest extends PartnerApplicationRequest {

    /** Optional custom commission rate, 0..1 (e.g. 0.15 = 15%). Null = tier default. */
    @DecimalMin(value = "0", message = "Commission rate must be between 0 and 1")
    @DecimalMax(value = "1", message = "Commission rate must be between 0 and 1")
    private BigDecimal commissionRateOverride;

    @Size(max = 1000, message = "Review note must be at most 1000 characters")
    private String reviewNote;
}
