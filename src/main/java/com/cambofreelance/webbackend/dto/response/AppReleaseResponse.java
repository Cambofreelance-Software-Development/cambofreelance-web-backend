package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.AppReleaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppReleaseResponse {

    private String id;
    private String appName;
    private String platform;
    private String versionName;
    private Integer versionCode;
    private String downloadUrl;
    private MediaFileResponse file;
    private String fileSize;
    private String minOsVersion;
    private String releaseNotes;
    private String releaseNotesKh;
    private Boolean forceUpdate;
    private LocalDate releaseDate;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public static AppReleaseResponse from(AppReleaseEntity e) {
        return AppReleaseResponse.builder()
            .id(e.getId())
            .appName(e.getAppName())
            .platform(e.getPlatform())
            .versionName(e.getVersionName())
            .versionCode(e.getVersionCode())
            .downloadUrl(e.getDownloadUrl())
            .file(e.getFile() != null ? MediaFileResponse.from(e.getFile()) : null)
            .fileSize(e.getFileSize())
            .minOsVersion(e.getMinOsVersion())
            .releaseNotes(e.getReleaseNotes())
            .releaseNotesKh(e.getReleaseNotesKh())
            .forceUpdate(e.getForceUpdate())
            .releaseDate(e.getReleaseDate())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
