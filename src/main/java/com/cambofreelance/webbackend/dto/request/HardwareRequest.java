package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class HardwareRequest {

    @NotBlank
    private String name;

    private String nameKh;

    private String brand;

    private String description;

    private String descriptionKh;

    private String content;

    private String contentKh;

    private String connectivity;

    private String price;

    private String platform;

    private String countries;

    private String categoryId;

    private String imageId;

    private List<String> imageIds;

    private String icon;

    private String link;

    private LocalDate releaseDate;

    private Integer sortOrder = 0;
}
