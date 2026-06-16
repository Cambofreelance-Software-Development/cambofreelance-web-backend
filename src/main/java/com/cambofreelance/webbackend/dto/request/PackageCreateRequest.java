package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;

@Data
public class PackageCreateRequest {

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Name is required")
    private String name;

    private BigDecimal monthlyPrice;

    private boolean customPricing;

    private Integer maxCustomers;

    private Integer maxLoans;

    private Integer maxUsers;

    private String description;

    private Integer sortOrder;

    /** Feature code -> enabled. Unlisted features default to disabled. */
    private Map<String, Boolean> features;
}
