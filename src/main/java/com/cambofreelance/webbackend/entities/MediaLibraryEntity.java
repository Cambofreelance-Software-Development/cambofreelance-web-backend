package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "media_library")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class MediaLibraryEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "media_id", nullable = false, unique = true)
    private String mediaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", insertable = false, updatable = false)
    private MediaFileEntity mediaFile;

    @Column(name = "folder")
    private String folder;

    @Column(name = "title")
    private String title;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "tags")
    private String tags;

    @Column(name = "uploaded_by")
    private String uploadedBy;
}
