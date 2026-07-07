package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.PricingFeatureEntity;
import com.cambofreelance.webbackend.entities.PricingPlanValueEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PricingFeatureResponse {

    private String id;
    private String name;
    private String nameKh;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private List<Value> values;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Value {
        private String planId;
        private String value;

        public static Value from(PricingPlanValueEntity e) {
            return Value.builder()
                .planId(e.getPlanId())
                .value(e.getValue())
                .build();
        }
    }

    public static PricingFeatureResponse from(PricingFeatureEntity e, List<PricingPlanValueEntity> values) {
        return PricingFeatureResponse.builder()
            .id(e.getId())
            .name(e.getName())
            .nameKh(e.getNameKh())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .values(values == null ? null : values.stream().map(Value::from).toList())
            .build();
    }
}
