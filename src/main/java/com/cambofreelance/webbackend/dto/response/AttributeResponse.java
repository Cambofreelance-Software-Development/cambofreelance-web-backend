package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.AttributeEntity;
import com.cambofreelance.webbackend.entities.AttributeValueEntity;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttributeResponse {

    private String id;
    private String code;
    private String name;
    private String dataType;
    private Boolean isVariantAttribute;
    private String applicableCategory;
    private Integer sortOrder;
    private List<AttributeValueResponse> values;

    public static AttributeResponse from(AttributeEntity e, List<AttributeValueEntity> vals) {
        List<AttributeValueResponse> valDtos = (vals == null) ? List.of() :
            vals.stream().map(AttributeValueResponse::from).toList();

        return AttributeResponse.builder()
            .id(e.getId())
            .code(e.getCode())
            .name(e.getName())
            .dataType(e.getDataType())
            .isVariantAttribute(e.getIsVariantAttribute())
            .applicableCategory(e.getApplicableCategory())
            .sortOrder(e.getSortOrder())
            .values(valDtos)
            .build();
    }
}
