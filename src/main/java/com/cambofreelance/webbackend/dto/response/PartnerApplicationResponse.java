package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.constants.PartnerApplicationStatus;
import com.cambofreelance.webbackend.entities.PartnerApplicationEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class PartnerApplicationResponse {

    private String id;
    private String userId;
    private String partnerRef;
    private String appStatus;

    private String partnerName;
    private String companyName;
    private String partnerType;
    private String businessType;
    private String employeeCount;
    private String businessRegistrationNo;
    private String website;

    private String country;
    private String city;
    private String businessAddress;

    private String logoUrl;
    private String notes;
    private Boolean agreementAccepted;
    private String payoutChannel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private java.util.Date submittedAt;

    private String reviewedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private java.util.Date reviewedAt;

    private String reviewNote;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private java.util.Date activatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private java.util.Date createdAt;

    /** Whether the applicant can still edit the form (SUBMITTED only). */
    private boolean editable;

    /** Whether a fresh application can be filed (REJECTED / WITHDRAWN). */
    private boolean reapplyable;

    /** Onboarding steps derived from appStatus, for the status page. */
    private List<TimelineStep> timeline;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimelineStep {
        private int step;
        private String title;
        /** COMPLETED / IN_PROGRESS / PENDING / REJECTED */
        private String state;
    }

    public static PartnerApplicationResponse from(PartnerApplicationEntity e) {
        return PartnerApplicationResponse.builder()
            .id(e.getId())
            .userId(e.getUserId())
            .partnerRef(e.getPartnerRef())
            .appStatus(e.getAppStatus())
            .partnerName(e.getPartnerName())
            .companyName(e.getCompanyName())
            .partnerType(e.getPartnerType())
            .businessType(e.getBusinessType())
            .employeeCount(e.getEmployeeCount())
            .businessRegistrationNo(e.getBusinessRegistrationNo())
            .website(e.getWebsite())
            .country(e.getCountry())
            .city(e.getCity())
            .businessAddress(e.getBusinessAddress())
            .logoUrl(e.getLogoUrl())
            .notes(e.getNotes())
            .agreementAccepted(e.getAgreementAccepted())
            .payoutChannel(e.getPayoutChannel())
            .submittedAt(e.getSubmittedAt())
            .reviewedBy(e.getReviewedBy())
            .reviewedAt(e.getReviewedAt())
            .reviewNote(e.getReviewNote())
            .activatedAt(e.getActivatedAt())
            .createdAt(e.getCreatedAt())
            .editable(PartnerApplicationStatus.isEditable(e.getAppStatus()))
            .reapplyable(PartnerApplicationStatus.isReapplyable(e.getAppStatus()))
            .timeline(buildTimeline(e.getAppStatus()))
            .build();
    }

    private static List<TimelineStep> buildTimeline(String status) {
        boolean approved = PartnerApplicationStatus.APPROVED.equals(status);
        boolean rejected = PartnerApplicationStatus.REJECTED.equals(status);
        boolean withdrawn = PartnerApplicationStatus.WITHDRAWN.equals(status);
        boolean demoStage = PartnerApplicationStatus.DEMO_SCHEDULED.equals(status);
        // Verification is done once the application has moved past it, either into the
        // demo-call stage or straight through to approval.
        boolean pastVerification = demoStage || approved;

        String verifyState = pastVerification ? "COMPLETED"
            : rejected ? "REJECTED"
            : withdrawn ? "PENDING"
            : "IN_PROGRESS";
        String demoState = approved ? "COMPLETED"
            : demoStage ? "IN_PROGRESS"
            : "PENDING";
        String activationState = approved ? "COMPLETED" : "PENDING";

        return List.of(
            new TimelineStep(1, "Application Submitted", "COMPLETED"),
            new TimelineStep(2, "Document & Merchant Verification", verifyState),
            new TimelineStep(3, "Strategy & Demo Call", demoState),
            new TimelineStep(4, "Portal Activation & Revenue Payout Setup", activationState)
        );
    }
}
