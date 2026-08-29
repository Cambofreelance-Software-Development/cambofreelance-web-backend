package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.InventoryItemEntity;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryItemResponse {

    private String id;
    private String variantId;
    private String variantName;
    private String productName;
    private String warehouseId;
    private String warehouseName;
    private String locationId;
    private String locationCode;

    private String serialNo;
    private String vin;
    private String engineNo;
    private String color;

    private BigDecimal purchaseCost;
    private String itemStatus;

    private String supplierName;
    private String poReference;
    private Date receivedAt;

    private String reservedBy;
    private Date reservedUntil;

    private String status;
    private Date createdAt;
    private String createdBy;

    public static InventoryItemResponse from(
        InventoryItemEntity e,
        String variantName,
        String productName,
        String warehouseName,
        String locationCode
    ) {
        return InventoryItemResponse.builder()
            .id(e.getId())
            .variantId(e.getVariantId())
            .variantName(variantName)
            .productName(productName)
            .warehouseId(e.getWarehouseId())
            .warehouseName(warehouseName)
            .locationId(e.getLocationId())
            .locationCode(locationCode)
            .serialNo(e.getSerialNo())
            .vin(e.getVin())
            .engineNo(e.getEngineNo())
            .color(e.getColor())
            .purchaseCost(e.getPurchaseCost())
            .itemStatus(e.getItemStatus())
            .supplierName(e.getSupplierName())
            .poReference(e.getPoReference())
            .receivedAt(e.getReceivedAt())
            .reservedBy(e.getReservedBy())
            .reservedUntil(e.getReservedUntil())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .createdBy(e.getCreatedBy())
            .build();
    }
}
