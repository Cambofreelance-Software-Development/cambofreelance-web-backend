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
@Table(name = "repayment_installments")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class RepaymentInstallmentEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "loan_application_id", nullable = false)
    private String loanApplicationId;

    @Column(name = "loan_number", nullable = false)
    private String loanNumber;

    @Column(name = "installment_no", nullable = false)
    private int installmentNo;

    @Column(name = "due_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(name = "principal_amount", nullable = false, columnDefinition = "NUMERIC(14,2)")
    private BigDecimal principalAmount;

    @Column(name = "interest_amount", nullable = false, columnDefinition = "NUMERIC(14,2)")
    private BigDecimal interestAmount;

    @Column(name = "total_payment", nullable = false, columnDefinition = "NUMERIC(14,2)")
    private BigDecimal totalPayment;

    @Column(name = "paid_amount", columnDefinition = "NUMERIC(14,2)")
    private BigDecimal paidAmount;

    @Column(name = "remaining_balance", columnDefinition = "NUMERIC(14,2)")
    private BigDecimal remainingBalance;

    /** PENDING | PAID | PARTIAL_PAID | OVERDUE | WAIVED */
    @Column(name = "installment_status", nullable = false)
    private String installmentStatus;

    @Column(name = "paid_date")
    @Temporal(TemporalType.DATE)
    private Date paidDate;

    @Column(name = "notes", length = 500)
    private String notes;
}
