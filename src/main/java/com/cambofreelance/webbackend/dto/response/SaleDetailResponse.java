package com.cambofreelance.webbackend.dto.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleDetailResponse {

    private SaleResponse sale;
    private List<SaleItemResponse> items;
    private FinancingApplicationResponse financing;
    private List<InventoryDocumentResponse> documents;

    @Data
    @Builder
    public static class SaleItemResponse {
        private String id;
        private String variantId;
        private String variantName;
        private String productName;
        private String inventoryItemId;
        private String vin;
        private String engineNo;
        private String color;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal discountAmount;
        private BigDecimal costPrice;
        private BigDecimal totalAmount;
    }
}
