package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.AttributeValueEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttributeValueResponse {

    private String id;
    private String attributeId;
    private String code;
    private String value;
    private Integer sortOrder;

    public static AttributeValueResponse from(AttributeValueEntity e) {
        return AttributeValueResponse.builder()
            .id(e.getId())
            .attributeId(e.getAttributeId())
            .code(e.getCode())
            .value(e.getValue())
            .sortOrder(e.getSortOrder())
            .build();
    }
}
