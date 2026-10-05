package com.cambofreelance.webbackend.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class LinkPartnerClientsResponse {

    /** userIds now attributed to the partner. */
    private List<String> linked;

    private List<Skipped> skipped;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Skipped {
        private String userId;
        /** NOT_FOUND / SELF / CYCLE / ALREADY_REFERRED / DUPLICATE */
        private String reason;
    }
}
