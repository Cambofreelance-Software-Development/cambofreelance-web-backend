package com.cambofreelance.webbackend.dto.response;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

/**
 * Section 38 / Section 68 vehicle timeline entry.
 */
@Data
@Builder
public class VehicleTimelineItemResponse {

    private String stage; // PURCHASED, RECEIVED, AVAILABLE, RESERVED, LOAN_SUBMITTED, BANK_REVIEW, LOAN_APPROVED, SALE_CONFIRMED, SOLD, DELIVERED
    private String title;
    private String description;
    private String status;
    private String referenceType;
    private String referenceId;
    private String performedBy;
    private Date eventTime;
    private boolean completed;
}
