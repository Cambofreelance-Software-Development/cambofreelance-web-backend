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
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "app_releases")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class AppReleaseEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "app_name", nullable = false, length = 150)
    private String appName;

    @Column(name = "platform", nullable = false, length = 30)
    private String platform;

    @Column(name = "version_name", nullable = false, length = 50)
    private String versionName;

    @Column(name = "version_code")
    private Integer versionCode;

    @Column(name = "download_url", length = 500)
    private String downloadUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private MediaFileEntity file;

    @Column(name = "file_size", length = 50)
    private String fileSize;

    @Column(name = "min_os_version", length = 50)
    private String minOsVersion;

    @Column(name = "release_notes", columnDefinition = "TEXT")
    private String releaseNotes;

    @Column(name = "release_notes_kh", columnDefinition = "TEXT")
    private String releaseNotesKh;

    @Column(name = "force_update", nullable = false)
    private Boolean forceUpdate = Boolean.FALSE;

    @Column(name = "release_date")
    private LocalDate releaseDate;
}