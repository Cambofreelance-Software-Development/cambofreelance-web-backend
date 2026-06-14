package com.cambofreelance.webbackend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SiteStatsResponse {

    private int projectsCompleted;
    private int happyClients;
    private int clientSatisfaction;
}
