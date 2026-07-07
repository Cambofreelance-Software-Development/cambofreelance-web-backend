package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

@Data
public class PricingFeatureRequest {

    @NotBlank
    private String name;

    private String nameKh;
    private Integer sortOrder = 0;

    private List<Value> values;

    @Data
    public static class Value {
        private String planId;
        private String value;
    }
}
