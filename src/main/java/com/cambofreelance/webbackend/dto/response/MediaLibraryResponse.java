package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.MediaLibraryEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaLibraryResponse {

    private String id;
    private String mediaId;
    private String folder;
    private String title;
    private String altText;
    private String description;
    private String tags;
    private String uploadedBy;
    private Date createdAt;
    private Date updatedAt;

    // Flattened media file fields
    private String fileName;
    private String originalName;
    private String mediaType;
    private String mimeType;
    private Long fileSize;
    private String fileUrl;

    public static MediaLibraryResponse from(MediaLibraryEntity e) {
        var builder = MediaLibraryResponse.builder()
            .id(e.getId())
            .mediaId(e.getMediaId())
            .folder(e.getFolder())
            .title(e.getTitle())
            .altText(e.getAltText())
            .description(e.getDescription())
            .tags(e.getTags())
            .uploadedBy(e.getUploadedBy())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt());

        if (e.getMediaFile() != null) {
            var mf = e.getMediaFile();
            builder
                .fileName(mf.getFileName())
                .originalName(mf.getOriginalName())
                .mediaType(mf.getMediaType())
                .mimeType(mf.getMimeType())
                .fileSize(mf.getFileSize())
                .fileUrl(mf.getFileUrl());
        }

        return builder.build();
    }
}
