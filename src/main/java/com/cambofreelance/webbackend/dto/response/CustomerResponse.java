package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.CustomerEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {

    private String id;
    private String customerCode;
    private String firstName;
    private String lastName;
    private String gender;
    private Date dateOfBirth;
    private String phoneNumber;
    private String alternatePhone;
    private String identityCardNumber;
    private String address;
    private String occupation;
    private String employerName;
    private BigDecimal monthlyIncome;
    private String guarantorName;
    private String guarantorPhone;
    private String onboardingStatus;
    private String submittedBy;
    private String submittedByName;
    private Date submittedAt;
    private String verifiedBy;
    private String verifiedByName;
    private Date verifiedAt;
    private String verificationNote;
    private String approvedBy;
    private String approvedByName;
    private Date approvedAt;
    private String approvalNote;
    private String rejectedBy;
    private String rejectedByName;
    private Date rejectedAt;
    private String rejectionReason;
    private String status;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;
    private static String getUserName(Map<String, String> userNames, String userId) {
        return (userNames == null || userId == null)
            ? null
            : userNames.get(userId);
    }

    public static CustomerResponse from(CustomerEntity e, Map<String, String> userNames) {
        return CustomerResponse.builder()
            .id(e.getId())
            .customerCode(e.getCustomerCode())
            .firstName(e.getFirstName())
            .lastName(e.getLastName())
            .gender(e.getGender())
            .dateOfBirth(e.getDateOfBirth())
            .phoneNumber(e.getPhoneNumber())
            .alternatePhone(e.getAlternatePhone())
            .identityCardNumber(e.getIdentityCardNumber())
            .address(e.getAddress())
            .occupation(e.getOccupation())
            .employerName(e.getEmployerName())
            .monthlyIncome(e.getMonthlyIncome())
            .guarantorName(e.getGuarantorName())
            .guarantorPhone(e.getGuarantorPhone())
            .onboardingStatus(e.getOnboardingStatus())

            .submittedBy(e.getSubmittedBy())
            .submittedByName(getUserName(userNames, e.getSubmittedBy()))
            .submittedAt(e.getSubmittedAt())

            .verifiedBy(e.getVerifiedBy())
            .verifiedByName(getUserName(userNames, e.getVerifiedBy()))
            .verifiedAt(e.getVerifiedAt())
            .verificationNote(e.getVerificationNote())

            .approvedBy(e.getApprovedBy())
            .approvedByName(getUserName(userNames, e.getApprovedBy()))
            .approvedAt(e.getApprovedAt())
            .approvalNote(e.getApprovalNote())

            .rejectedBy(e.getRejectedBy())
            .rejectedByName(getUserName(userNames, e.getRejectedBy()))
            .rejectedAt(e.getRejectedAt())
            .rejectionReason(e.getRejectionReason())

            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .createdBy(e.getCreatedBy())
            .updatedAt(e.getUpdatedAt())
            .updatedBy(e.getUpdatedBy())
            .build();
    }
}
