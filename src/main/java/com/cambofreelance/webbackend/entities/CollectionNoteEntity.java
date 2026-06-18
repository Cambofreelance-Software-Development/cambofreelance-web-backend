package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "collection_notes")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class CollectionNoteEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "collection_case_id", nullable = false)
    private String collectionCaseId;

    @Column(name = "loan_application_id", nullable = false)
    private String loanApplicationId;

    @Column(name = "note_type", nullable = false)
    private String noteType;

    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_result")
    private String contactResult;
}
