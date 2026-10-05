package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

/** Admin attributes existing clients to a partner (offline reseller's book of clients). */
@Data
public class LinkPartnerClientsRequest {

    @NotEmpty(message = "At least one client is required")
    @Size(max = 200, message = "At most 200 clients can be linked at once")
    private List<String> clientUserIds;

    /** Re-attribute clients already referred by someone else. Default false (they are skipped). */
    private Boolean overrideExisting;
}
