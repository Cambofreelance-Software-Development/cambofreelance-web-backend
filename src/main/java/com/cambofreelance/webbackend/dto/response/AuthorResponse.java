package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.AuthorEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorResponse {

    private String id;
    private String name;
    private String nameKh;
    private String bio;
    private String email;
    private MediaFileResponse avatar;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public static AuthorResponse from(AuthorEntity e) {
        return AuthorResponse.builder()
            .id(e.getId())
            .name(e.getName())
            .nameKh(e.getNameKh())
            .bio(e.getBio())
            .email(e.getEmail())
            .avatar(e.getAvatar() != null ? MediaFileResponse.from(e.getAvatar()) : null)
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
