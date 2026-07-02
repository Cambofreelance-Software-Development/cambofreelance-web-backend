package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

/** Lives in the tenant's own Postgres schema — isolation is physical (schema), not a tenant_id column. */
@Entity
@Table(name = "customers")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class CustomerEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    /** Sequential human-readable code, e.g. "CUS-00000001" — restarts at 1 per tenant schema. */
    @Column(name = "customer_code", nullable = false, unique = true)
    private String customerCode;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    /** MALE | FEMALE | OTHER */
    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "alternate_phone")
    private String alternatePhone;

    @Column(name = "identity_card_number")
    private String identityCardNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "province_code")
    private String provinceCode;

    @Column(name = "district_code")
    private String districtCode;

    @Column(name = "commune_code")
    private String communeCode;

    @Column(name = "village_code")
    private String villageCode;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "employer_name")
    private String employerName;

    @Column(name = "monthly_income")
    private BigDecimal monthlyIncome;

    @Column(name = "guarantor_name")
    private String guarantorName;

    @Column(name = "guarantor_phone")
    private String guarantorPhone;

    /** DRAFT | PENDING_VERIFICATION | VERIFIED | REJECTED | ACTIVE | INACTIVE */
    @Column(name = "onboarding_status", nullable = false)
    private String onboardingStatus;

    @Column(name = "submitted_by")
    private String submittedBy;

    @Column(name = "submitted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;

    @Column(name = "verified_by")
    private String verifiedBy;

    @Column(name = "verified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date verifiedAt;

    @Column(name = "verification_note", length = 1000)
    private String verificationNote;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedAt;

    @Column(name = "approval_note", length = 1000)
    private String approvalNote;

    @Column(name = "rejected_by")
    private String rejectedBy;

    @Column(name = "rejected_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rejectedAt;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;
}
