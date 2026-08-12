package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "clients")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class ClientEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_email")
    private String companyEmail;

    @Column(name = "company_phone")
    private String companyPhone;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    /** PENDING / ACTIVE / SUSPENDED / CLOSED */
    @Column(name = "client_status")
    private String clientStatus;

    /** VERIFY_EMAIL / COMPANY_INFO / SELECT_PACKAGE / PAYMENT / DONE */
    @Column(name = "onboarding_step")
    private String onboardingStep;
}
