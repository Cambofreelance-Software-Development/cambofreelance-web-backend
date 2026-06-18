package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.MediaLibraryEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaLibraryRepository extends JpaRepository<MediaLibraryEntity, String> {

    boolean existsByMediaId(String mediaId);

    Optional<MediaLibraryEntity> findByIdAndStatus(String id, String status);

    @Query("""
        SELECT ml FROM MediaLibraryEntity ml
        JOIN FETCH ml.mediaFile mf
        WHERE ml.status = 'ACT'
          AND (:folder IS NULL OR ml.folder = :folder)
          AND (:mediaType IS NULL OR mf.mediaType = :mediaType)
          AND (:search IS NULL OR
               LOWER(ml.title) LIKE :search OR
               LOWER(mf.originalName) LIKE :search OR
               LOWER(ml.tags) LIKE :search)
        ORDER BY ml.createdAt DESC
        """)
    Page<MediaLibraryEntity> findAllActive(
        @Param("folder") String folder,
        @Param("mediaType") String mediaType,
        @Param("search") String search,
        Pageable pageable
    );
}
