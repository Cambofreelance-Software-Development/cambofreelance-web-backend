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
@Table(name = "promise_to_pay")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class PromiseToPayEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "collection_case_id", nullable = false)
    private String collectionCaseId;

    @Column(name = "loan_application_id", nullable = false)
    private String loanApplicationId;

    @Temporal(TemporalType.DATE)
    @Column(name = "promise_date", nullable = false)
    private Date promiseDate;

    @Column(name = "promise_amount", nullable = false)
    private BigDecimal promiseAmount;

    @Column(name = "ptp_status", nullable = false)
    private String ptpStatus;

    @Column(name = "notes", length = 500)
    private String notes;
}
