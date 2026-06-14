package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.entities.MediaFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<MediaFileEntity, String> {

    @Query("""
        SELECT m FROM MediaFileEntity m
        WHERE m.status = 'ACT'
          AND (:mediaType IS NULL OR m.mediaType = :mediaType)
          AND (:search IS NULL OR LOWER(m.originalName) LIKE :search)
        ORDER BY m.createdAt DESC
        """)
    Page<MediaFileEntity> findAllActive(
        @Param("mediaType") String mediaType,
        @Param("search") String search,
        Pageable pageable
    );
}
