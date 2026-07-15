package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class BusinessTypeGroupRequest {

    @NotBlank
    private String title;

    private String titleKh;

    private String icon;

    private Integer sortOrder = 0;

    @Valid
    private List<BusinessTypeTagRequest> tags = new ArrayList<>();

    @Data
    public static class BusinessTypeTagRequest {
        private String id; // present on update, null on create
        @NotBlank
        private String label;
        private String labelKh;
        private Integer sortOrder = 0;
    }
}
