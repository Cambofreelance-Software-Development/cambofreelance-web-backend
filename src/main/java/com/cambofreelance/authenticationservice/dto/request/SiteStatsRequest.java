package com.cambofreelance.authenticationservice.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class SiteStatsRequest {

    @Min(0)
    private int projectsCompleted;

    @Min(0)
    private int happyClients;

    @Min(0)
    private int clientSatisfaction;
}
