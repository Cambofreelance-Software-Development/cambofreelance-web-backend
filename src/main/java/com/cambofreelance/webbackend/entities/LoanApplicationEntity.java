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
@Table(name = "loan_applications")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class LoanApplicationEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    /** Sequential human-readable number, e.g. "LN-2026000001" — per tenant schema per year. */
    @Column(name = "loan_number", nullable = false, unique = true)
    private String loanNumber;

    /** FK to customers.id in the same tenant schema. */
    @Column(name = "customer_id", nullable = false)
    private String customerId;

    /** Denormalised from the customer entity at create time for display purposes. */
    @Column(name = "customer_code", nullable = false)
    private String customerCode;

    @Column(name = "customer_first_name", nullable = false)
    private String customerFirstName;

    @Column(name = "customer_last_name", nullable = false)
    private String customerLastName;

    @Column(name = "loan_amount", nullable = false, columnDefinition = "NUMERIC(14,2)")
    private BigDecimal loanAmount;

    /** USD | KHR */
    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "tenor_value", nullable = false)
    private Integer tenorValue;

    /** DAY | MONTH | YEAR */
    @Column(name = "tenor_unit", nullable = false)
    private String tenorUnit;

    /** DAILY | WEEKLY | MONTHLY */
    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "interest_rate", nullable = false, columnDefinition = "NUMERIC(5,2)")
    private BigDecimal interestRate;

    @Column(name = "service_fee_rate", nullable = false, columnDefinition = "NUMERIC(5,2)")
    private BigDecimal serviceFeeRate;

    /** Computed: loanAmount × serviceFeeRate / 100 */
    @Column(name = "service_fee_amount", columnDefinition = "NUMERIC(14,2)")
    private BigDecimal serviceFeeAmount;

    /** Computed: loanAmount × interestRate / 100 */
    @Column(name = "interest_amount", columnDefinition = "NUMERIC(14,2)")
    private BigDecimal interestAmount;

    /** Computed: loanAmount - serviceFeeAmount */
    @Column(name = "net_disbursement", columnDefinition = "NUMERIC(14,2)")
    private BigDecimal netDisbursement;

    /** Set to today on create — not user input. */
    @Column(name = "application_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date applicationDate;

    /** User-supplied date of first repayment. */
    @Column(name = "first_payment_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date firstPaymentDate;

    /** BUSINESS | FARMING | PERSONAL */
    @Column(name = "purpose", nullable = false)
    private String purpose;

    /** Set to the acting userId on create; references public.users.user_id. */
    @Column(name = "loan_officer_id", nullable = false)
    private String loanOfficerId;

    /** DRAFT | PENDING_APPROVAL | APPROVED | REJECTED */
    @Column(name = "application_status", nullable = false)
    private String applicationStatus;

    @Column(name = "submitted_by")
    private String submittedBy;

    @Column(name = "submitted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;

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
