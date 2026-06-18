package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "payments")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class PaymentEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "receipt_number", nullable = false, unique = true)
    private String receiptNumber;

    @Column(name = "loan_application_id", nullable = false)
    private String loanApplicationId;

    @Column(name = "loan_number")
    private String loanNumber;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "customer_code")
    private String customerCode;

    @Column(name = "customer_first_name")
    private String customerFirstName;

    @Column(name = "customer_last_name")
    private String customerLastName;

    @Column(name = "payment_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date paymentDate;

    @Column(name = "principal_paid", columnDefinition = "NUMERIC(14,2)")
    private BigDecimal principalPaid;

    @Column(name = "interest_paid", columnDefinition = "NUMERIC(14,2)")
    private BigDecimal interestPaid;

    @Column(name = "penalty_fee", columnDefinition = "NUMERIC(14,2)")
    private BigDecimal penaltyFee;

    @Column(name = "total_paid", nullable = false, columnDefinition = "NUMERIC(14,2)")
    private BigDecimal totalPaid;

    @Column(name = "payment_method", nullable = false, length = 20)
    private String paymentMethod;

    @Column(name = "payment_type", nullable = false, length = 20)
    private String paymentType;

    @Column(name = "received_by_id")
    private String receivedById;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "payment_status", nullable = false, length = 20)
    private String paymentStatus;

    @Column(name = "reversed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reversedAt;

    @Column(name = "reversed_by_id")
    private String reversedById;

    @Column(name = "reversal_reason", length = 500)
    private String reversalReason;
}
