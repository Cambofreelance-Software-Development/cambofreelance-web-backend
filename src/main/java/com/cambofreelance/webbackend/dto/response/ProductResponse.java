package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.ProductEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

    private String id;
    private String name;
    private String nameKh;
    private String description;
    private String descriptionKh;
    private String price;
    private CategoryProductResponse category;
    private MediaFileResponse image;
    private String icon;
    private String link;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public static ProductResponse from(ProductEntity e) {
        return ProductResponse.builder()
            .id(e.getId())
            .name(e.getName())
            .nameKh(e.getNameKh())
            .description(e.getDescription())
            .descriptionKh(e.getDescriptionKh())
            .price(e.getPrice())
            .category(e.getCategory() != null ? CategoryProductResponse.from(e.getCategory()) : null)
            .image(e.getImage() != null ? MediaFileResponse.from(e.getImage()) : null)
            .icon(e.getIcon())
            .link(e.getLink())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
