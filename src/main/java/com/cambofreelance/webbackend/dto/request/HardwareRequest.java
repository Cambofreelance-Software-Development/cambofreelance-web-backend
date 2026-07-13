package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Data;

@Data
public class HardwareRequest {

    @NotBlank
    private String name;

    private String nameKh;

    private String brand;

    private String description;

    private String descriptionKh;

    private String connectivity;

    private String price;

    private String platform;

    private String countries;

    private String categoryId;

    private String imageId;

    private String icon;

    private String link;

    private LocalDate releaseDate;

    private Integer sortOrder = 0;
}
