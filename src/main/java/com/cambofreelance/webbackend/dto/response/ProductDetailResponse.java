package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.ProductEntity;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetailResponse {

    private ProductResponse product;
    private List<ProductVariantResponse> variants;
    private List<ProductAttributeResponse> additionalAttributes;
    private List<InventoryItemResponse> recentItems;

    // Vehicle Specific
    private Integer engineCc;
    private String fuelType;
    private String transmission;
    private String vehicleCondition;
    private String warrantyPeriod;

    // Packaging ladder
    private String caseName;
    private Integer caseQty;
    private String caseBarcode;
    private String boxName;
    private Integer boxQty;
    private String boxBarcode;

    public static ProductDetailResponse from(
        ProductEntity e,
        ProductResponse productResp,
        List<ProductVariantResponse> variants,
        List<ProductAttributeResponse> attrs,
        List<InventoryItemResponse> items
    ) {
        return ProductDetailResponse.builder()
            .product(productResp)
            .variants(variants)
            .additionalAttributes(attrs)
            .recentItems(items)
            .engineCc(e.getEngineCc())
            .fuelType(e.getFuelType())
            .transmission(e.getTransmission())
            .vehicleCondition(e.getVehicleCondition())
            .warrantyPeriod(e.getWarrantyPeriod())
            .caseName(e.getCaseName())
            .caseQty(e.getCaseQty())
            .caseBarcode(e.getCaseBarcode())
            .boxName(e.getBoxName())
            .boxQty(e.getBoxQty())
            .boxBarcode(e.getBoxBarcode())
            .build();
    }
}
