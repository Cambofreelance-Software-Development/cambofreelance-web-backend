package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "taxonomy_item")

public class TaxonomyItemEntity implements Serializable {
    @Id
    private String code;
    private String taxonomyCode;
    private String parentCode;
    private String displayKm;
    private String displayEn;
    private String metadata;
    private String status;
    private UUID userId;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    @PrePersist
    @PreUpdate
    public void normalizeEmptyStrings() {
        if (this.parentCode != null && this.parentCode.trim().isEmpty()) {
            this.parentCode = null;
        }
    }
}
