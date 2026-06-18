package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "taxonomy")
public class TaxonomyEntity {
    @Id
    private String code;
    private String name;
    private Boolean isHierarchical;
    private String description;
    private String remark;
    private String status;
    private UUID userId;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
