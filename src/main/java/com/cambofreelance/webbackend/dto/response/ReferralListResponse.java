package com.cambofreelance.webbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferralListResponse {

    @JsonProperty("content")
    private List<ReferredUserItem> content;

    @JsonProperty("totalElements")
    private long totalElements;

    @JsonProperty("totalPages")
    private int totalPages;

    @JsonProperty("currentPage")
    private int currentPage;

    @JsonProperty("pageSize")
    private int pageSize;

    @JsonProperty("first")
    private boolean first;

    @JsonProperty("last")
    private boolean last;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReferredUserItem {
        @JsonProperty("userId")
        private String userId;

        @JsonProperty("username")
        private String username;

        @JsonProperty("createdAt")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
        private Date createdAt;

        /** True once this referred user has created at least one subscription. */
        @JsonProperty("subscribed")
        private boolean subscribed;
    }
}
