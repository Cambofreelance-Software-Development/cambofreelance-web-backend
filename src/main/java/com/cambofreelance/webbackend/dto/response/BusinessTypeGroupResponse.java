package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.entities.BusinessTypeGroupEntity;
import com.cambofreelance.webbackend.entities.BusinessTypeTagEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessTypeGroupResponse {

    private String id;
    private String title;
    private String titleKh;
    private String icon;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private List<BusinessTypeTagResponse> tags;

    public static BusinessTypeGroupResponse from(BusinessTypeGroupEntity e) {
        List<BusinessTypeTagResponse> tagResponses = e.getTags() == null ? List.of() :
            e.getTags().stream()
                .filter(t -> !Constants.STATUS_DELETE.equals(t.getStatus()))
                .sorted(Comparator.comparing(t -> t.getSortOrder() == null ? 0 : t.getSortOrder()))
                .map(BusinessTypeTagResponse::from)
                .collect(Collectors.toList());
        return BusinessTypeGroupResponse.builder()
            .id(e.getId())
            .title(e.getTitle())
            .titleKh(e.getTitleKh())
            .icon(e.getIcon())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .tags(tagResponses)
            .build();
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BusinessTypeTagResponse {
        private String id;
        private String label;
        private String labelKh;
        private Integer sortOrder;

        public static BusinessTypeTagResponse from(BusinessTypeTagEntity t) {
            return BusinessTypeTagResponse.builder()
                .id(t.getId())
                .label(t.getLabel())
                .labelKh(t.getLabelKh())
                .sortOrder(t.getSortOrder())
                .build();
        }
    }
}
