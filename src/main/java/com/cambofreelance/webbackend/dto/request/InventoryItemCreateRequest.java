package com.cambofreelance.webbackend.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class InventoryItemCreateRequest {

    private String variantId;
    private String variantName; // Helper for auto-matching if variantId not yet known
    private String warehouseId;
    private String locationId;

    private String serialNo;
    private String vin;
    private String engineNo;
    private String color;

    private BigDecimal purchaseCost;
    private String itemStatus; // AVAILABLE, RESERVED, LOAN_PENDING, SOLD, etc.

    private String supplierName;
    private String poReference;
    private Date receivedAt;
}
