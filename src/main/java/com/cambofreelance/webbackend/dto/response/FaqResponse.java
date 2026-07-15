package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.FaqEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaqResponse {

    private String id;
    private String question;
    private String questionKh;
    private String answer;
    private String answerKh;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public static FaqResponse from(FaqEntity e) {
        return FaqResponse.builder()
            .id(e.getId())
            .question(e.getQuestion())
            .questionKh(e.getQuestionKh())
            .answer(e.getAnswer())
            .answerKh(e.getAnswerKh())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
