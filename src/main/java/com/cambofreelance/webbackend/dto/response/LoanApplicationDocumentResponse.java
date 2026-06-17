package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.LoanApplicationDocumentEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanApplicationDocumentResponse {

    private String id;
    private String loanApplicationId;
    private String documentType;
    private String mediaId;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String mimeType;
    private String createdBy;
    private Date createdAt;

    public static LoanApplicationDocumentResponse from(LoanApplicationDocumentEntity doc,
                                                       MediaFileResponse media) {
        return LoanApplicationDocumentResponse.builder()
            .id(doc.getId())
            .loanApplicationId(doc.getLoanApplicationId())
            .documentType(doc.getDocumentType())
            .mediaId(doc.getMediaId())
            .fileName(media != null ? media.getFileName() : null)
            .fileUrl(media != null ? media.getFileUrl() : null)
            .fileSize(media != null ? media.getFileSize() : null)
            .mimeType(media != null ? media.getMimeType() : null)
            .createdBy(doc.getCreatedBy())
            .createdAt(doc.getCreatedAt())
            .build();
    }
}
