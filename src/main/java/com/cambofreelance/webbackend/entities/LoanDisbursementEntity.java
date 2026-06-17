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

@Entity
@Table(name = "loan_disbursements")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class LoanDisbursementEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "loan_application_id", nullable = false, unique = true)
    private String loanApplicationId;

    @Column(name = "loan_number", nullable = false)
    private String loanNumber;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "customer_code", nullable = false)
    private String customerCode;

    @Column(name = "customer_first_name", nullable = false)
    private String customerFirstName;

    @Column(name = "customer_last_name", nullable = false)
    private String customerLastName;

    @Column(name = "disbursement_amount", nullable = false, columnDefinition = "NUMERIC(14,2)")
    private BigDecimal disbursementAmount;

    @Column(name = "currency", nullable = false)
    private String currency;

    /** MediaId of the signed contract document. Nullable until uploaded. */
    @Column(name = "contract_media_id")
    private String contractMediaId;

    @Column(name = "disbursement_date")
    @Temporal(TemporalType.DATE)
    private Date disbursementDate;

    @Column(name = "disbursed_by_id")
    private String disbursedById;

    /** PENDING | COMPLETED */
    @Column(name = "disbursement_status", nullable = false)
    private String disbursementStatus;

    @Column(name = "notes", length = 1000)
    private String notes;
}
