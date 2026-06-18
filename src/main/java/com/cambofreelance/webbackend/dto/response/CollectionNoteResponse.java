package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.CollectionNoteEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectionNoteResponse {

    private String id;
    private String collectionCaseId;
    private String loanApplicationId;
    private String noteType;
    private String content;
    private String contactPerson;
    private String contactResult;
    private String createdBy;
    private Date createdAt;

    public static CollectionNoteResponse from(CollectionNoteEntity e) {
        return CollectionNoteResponse.builder()
            .id(e.getId())
            .collectionCaseId(e.getCollectionCaseId())
            .loanApplicationId(e.getLoanApplicationId())
            .noteType(e.getNoteType())
            .content(e.getContent())
            .contactPerson(e.getContactPerson())
            .contactResult(e.getContactResult())
            .createdBy(e.getCreatedBy())
            .createdAt(e.getCreatedAt())
            .build();
    }
}
