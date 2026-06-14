package com.cambofreelance.authenticationservice.dto.response;

import com.cambofreelance.authenticationservice.entities.MediaFileEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaFileResponse {

    private String id;
    private String fileName;
    private String originalName;
    private String mediaType;
    private String mimeType;
    private Long fileSize;
    private String fileUrl;
    private String uploadedBy;
    private Date createdAt;

    public static MediaFileResponse from(MediaFileEntity e) {
        return MediaFileResponse.builder()
            .id(e.getId())
            .fileName(e.getFileName())
            .originalName(e.getOriginalName())
            .mediaType(e.getMediaType())
            .mimeType(e.getMimeType())
            .fileSize(e.getFileSize())
            .fileUrl(e.getFileUrl())
            .uploadedBy(e.getUploadedBy())
            .createdAt(e.getCreatedAt())
            .build();
    }
}
