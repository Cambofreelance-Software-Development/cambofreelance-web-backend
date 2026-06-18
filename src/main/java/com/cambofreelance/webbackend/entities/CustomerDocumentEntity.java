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
 * Thin link row in the tenant's schema between a {@link CustomerEntity} and a
 * {@code public.media_files} row owned by the shared media upload pipeline — file
 * metadata (name/url/size) is never duplicated here, only fetched via MediaService.
 */
@Entity
@Table(name = "customer_documents")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class CustomerDocumentEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "media_id", nullable = false, unique = true)
    private String mediaId;

    /** NATIONAL_ID_FRONT | NATIONAL_ID_BACK | CUSTOMER_PHOTO | ADDRESS_VERIFICATION | INCOME_VERIFICATION | GUARANTOR_ID */
    @Column(name = "document_type", nullable = false)
    private String documentType;
}
