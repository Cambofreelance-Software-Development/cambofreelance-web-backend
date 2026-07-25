package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.HardwareEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HardwareResponse {

    private String id;
    private String name;
    private String nameKh;
    private String brand;
    private String description;
    private String descriptionKh;
    private String content;
    private String contentKh;
    private String overview;
    private String overviewKh;
    private String connectivity;
    private String price;
    private String platform;
    private String countries;
    private CategoryHardwareResponse category;
    private MediaFileResponse image;
    private List<MediaFileResponse> images;
    private String imageUrl;
    private String icon;
    private String link;
    private LocalDate releaseDate;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    /** Lightweight projection for list views — omits the gallery and rich content
     *  (detail-only fields) so the public list stays cheap and avoids an extra
     *  per-row lazy-load of the images join table. */
    public static HardwareResponse fromSummary(HardwareEntity e) {
        return HardwareResponse.builder()
            .id(e.getId())
            .name(e.getName())
            .nameKh(e.getNameKh())
            .brand(e.getBrand())
            .description(e.getDescription())
            .descriptionKh(e.getDescriptionKh())
            .connectivity(e.getConnectivity())
            .price(e.getPrice())
            .platform(e.getPlatform())
            .countries(e.getCountries())
            .category(e.getCategory() != null ? CategoryHardwareResponse.from(e.getCategory()) : null)
            .image(e.getImage() != null ? MediaFileResponse.from(e.getImage()) : null)
            .imageUrl(e.getImageUrl())
            .icon(e.getIcon())
            .link(e.getLink())
            .releaseDate(e.getReleaseDate())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }

    public static HardwareResponse from(HardwareEntity e) {
        return HardwareResponse.builder()
            .id(e.getId())
            .name(e.getName())
            .nameKh(e.getNameKh())
            .brand(e.getBrand())
            .description(e.getDescription())
            .descriptionKh(e.getDescriptionKh())
            .content(e.getContent())
            .contentKh(e.getContentKh())
            .overview(e.getOverview())
            .overviewKh(e.getOverviewKh())
            .connectivity(e.getConnectivity())
            .price(e.getPrice())
            .platform(e.getPlatform())
            .countries(e.getCountries())
            .category(e.getCategory() != null ? CategoryHardwareResponse.from(e.getCategory()) : null)
            .image(e.getImage() != null ? MediaFileResponse.from(e.getImage()) : null)
            .images(e.getImages() != null && !e.getImages().isEmpty()
                ? e.getImages().stream().map(MediaFileResponse::from).collect(Collectors.toList())
                : null)
            .imageUrl(e.getImageUrl())
            .icon(e.getIcon())
            .link(e.getLink())
            .releaseDate(e.getReleaseDate())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
