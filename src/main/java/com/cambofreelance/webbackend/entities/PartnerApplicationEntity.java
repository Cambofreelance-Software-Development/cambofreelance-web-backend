package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "partner_applications")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class PartnerApplicationEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    /** PTR-YYYY-NNNN — assigned on first submit, then copied onto users.referral_code at approval. */
    @Column(name = "partner_ref")
    private String partnerRef;

    /** SUBMITTED / UNDER_REVIEW / APPROVED / REJECTED / WITHDRAWN — see PartnerApplicationStatus. */
    @Column(name = "app_status")
    private String appStatus;

    @Column(name = "partner_name")
    private String partnerName;

    @Column(name = "company_name")
    private String companyName;

    /** RESELLER / SYSTEM_INTEGRATOR / AGENCY / INDIVIDUAL / OTHER. */
    @Column(name = "partner_type")
    private String partnerType;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "employee_count")
    private String employeeCount;

    @Column(name = "business_registration_no")
    private String businessRegistrationNo;

    @Column(name = "website")
    private String website;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "business_address")
    private String businessAddress;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "notes")
    private String notes;

    @Column(name = "agreement_accepted")
    private Boolean agreementAccepted;

    @Column(name = "payout_channel")
    private String payoutChannel;

    @Column(name = "submitted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @Column(name = "reviewed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewedAt;

    @Column(name = "review_note")
    private String reviewNote;

    @Column(name = "activated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activatedAt;
}
