package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Thin link row in the tenant's schema between a {@link LoanApplicationEntity} and a
 * {@code public.media_files} row — file metadata is fetched via MediaService, never duplicated here.
 */
@Entity
@Table(name = "loan_application_documents")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class LoanApplicationDocumentEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "loan_application_id", nullable = false)
    private String loanApplicationId;

    @Column(name = "media_id", nullable = false, unique = true)
    private String mediaId;

    /** APPLICATION_FORM | ID_FRONT | ID_BACK | INCOME_PROOF | COLLATERAL_PHOTO | BUSINESS_LICENSE | LAND_TITLE | OTHER */
    @Column(name = "document_type", nullable = false)
    private String documentType;
}
