package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Data;

@Data
public class AppReleaseRequest {

    @NotBlank
    private String appName;

    @NotBlank
    private String platform;

    @NotBlank
    private String versionName;

    private Integer versionCode;

    private String downloadUrl;

    private String fileId;

    private String fileSize;

    private String minOsVersion;

    private String releaseNotes;

    private String releaseNotesKh;

    private Boolean forceUpdate = Boolean.FALSE;

    private LocalDate releaseDate;
}
