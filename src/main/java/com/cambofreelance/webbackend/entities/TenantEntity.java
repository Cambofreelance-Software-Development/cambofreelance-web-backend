package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serial;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "tenants")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class TenantEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    /** Unique short code, e.g. "VLG-001" */
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /** Display name */
    @Column(name = "name", nullable = false)
    private String name;

    /** VILLAGE | BRANCH | COOPERATIVE | LENDER */
    @Column(name = "tenant_type")
    private String tenantType;

    @Column(name = "description", length = 1000)
    private String description;

    /** URL or path to tenant logo */
    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "secondary_color")
    private String secondaryColor;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "company_email")
    private String companyEmail;

    @Column(name = "company_phone")
    private String companyPhone;

    @Column(name = "website")
    private String website;

    /** Subscription / plan start date */
    @Column(name = "plan_start_date")
    @Temporal(TemporalType.DATE)
    private Date planStartDate;

    /** Subscription / plan expiry date */
    @Column(name = "plan_expired_date")
    @Temporal(TemporalType.DATE)
    private Date planExpiredDate;

    /** Set when this tenant came from self-service registration; the user who becomes Tenant Admin on approval */
    @Column(name = "requested_by_user_id")
    private String requestedByUserId;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;

    /** PENDING | READY | FAILED — background schema/Flyway provisioning state */
    @Column(name = "schema_status", nullable = false)
    private String schemaStatus = "READY";

    /** ACT | SUS | EXP | DEL | PEN */
    // status field is inherited from BaseEntity (default ACT)
}
