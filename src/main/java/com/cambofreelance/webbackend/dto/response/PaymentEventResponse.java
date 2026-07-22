package com.cambofreelance.webbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentEventResponse {

    private String transactionId;
    private String fromStatus;
    private String toStatus;
    private String source;
    private String note;
    /** Raw actor: userId, or "SYS" for system-driven events */
    private String actor;
    /** Human-friendly actor: username for a user, "System" for SYS */
    private String actorName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date createdAt;
}
