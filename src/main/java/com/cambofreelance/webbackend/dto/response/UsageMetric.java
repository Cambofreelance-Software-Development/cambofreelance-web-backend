package com.cambofreelance.webbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsageMetric {

    @JsonProperty("current")
    private long current;

    /** null = unlimited or no package assigned */
    @JsonProperty("limit")
    private Integer limit;

    /** false when this metric isn't backed by real data yet (no domain module to measure it from) */
    @JsonProperty("tracked")
    private boolean tracked;
}
