package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "collection_cases")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class CollectionCaseEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "loan_application_id", nullable = false, unique = true)
    private String loanApplicationId;

    @Column(name = "loan_number")
    private String loanNumber;

    @Column(name = "currency")
    private String currency;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "customer_code")
    private String customerCode;

    @Column(name = "customer_first_name")
    private String customerFirstName;

    @Column(name = "customer_last_name")
    private String customerLastName;

    @Column(name = "collection_status", nullable = false)
    private String collectionStatus;

    @Column(name = "assigned_officer_id")
    private String assignedOfficerId;

    @Column(name = "dpd", nullable = false)
    private int dpd;

    @Column(name = "total_overdue_amount", nullable = false)
    private BigDecimal totalOverdueAmount;

    @Column(name = "penalty_rate", nullable = false)
    private BigDecimal penaltyRate;

    @Column(name = "penalty_amount", nullable = false)
    private BigDecimal penaltyAmount;

    @Column(name = "remarks", length = 2000)
    private String remarks;
}
