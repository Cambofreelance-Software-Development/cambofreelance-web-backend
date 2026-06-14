package com.cambofreelance.authenticationservice.dto.response;

import com.cambofreelance.authenticationservice.entities.ArticleTypeEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleTypeResponse {

    private String id;
    private String code;
    private String label;
    private String labelKh;
    private Integer sortOrder;
    private String status;
    private Date createdAt;

    public static ArticleTypeResponse from(ArticleTypeEntity e) {
        return ArticleTypeResponse.builder()
            .id(e.getId())
            .code(e.getCode())
            .label(e.getLabel())
            .labelKh(e.getLabelKh())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .build();
    }
}
