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

/**
 * Polymorphic document table for Inventory, Customer, Sale, and Financing files.
 */
@Entity
@Table(name = "inventory_documents")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class InventoryDocumentEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    /** CUSTOMER | SALE | FINANCING_APPLICATION | INVENTORY_ITEM | PRODUCT | VARIANT */
    @Column(name = "owner_type", nullable = false)
    private String ownerType;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "media_id")
    private String mediaId;

    /** NATIONAL_ID | FAMILY_BOOK | SALARY_CERTIFICATE | BANK_STATEMENT | REGISTRATION | INVOICE | CONTRACT | WARRANTY | SPECIFICATION | OTHER */
    @Column(name = "document_type", nullable = false)
    private String documentType;

    @Column(name = "document_name", nullable = false)
    private String documentName;

    @Column(name = "file_url", length = 1000)
    private String fileUrl;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "file_size")
    private Long fileSize;

    /** UPLOADED | PENDING_REVIEW | VERIFIED | REJECTED | EXPIRED | REPLACED */
    @Column(name = "document_status", nullable = false)
    private String documentStatus = "UPLOADED";

    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @Column(name = "expires_at")
    @Temporal(TemporalType.DATE)
    private Date expiresAt;

    @Column(name = "verified_by")
    private String verifiedBy;

    @Column(name = "verified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date verifiedAt;
}
