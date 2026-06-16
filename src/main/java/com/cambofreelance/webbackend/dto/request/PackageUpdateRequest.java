package com.cambofreelance.webbackend.dto.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PackageUpdateRequest {

    private String name;

    private BigDecimal monthlyPrice;

    private Boolean customPricing;

    private Integer maxCustomers;

    private Integer maxLoans;

    private Integer maxUsers;

    private String description;

    private Integer sortOrder;
}
