package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.AppReleaseEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppReleaseRepository extends JpaRepository<AppReleaseEntity, String> {

    @Query("SELECT r FROM AppReleaseEntity r WHERE r.status = 'ACT' " +
           "ORDER BY r.releaseDate DESC NULLS LAST, r.createdAt DESC")
    List<AppReleaseEntity> findAllActive();

    @Query("SELECT r FROM AppReleaseEntity r WHERE r.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(r.appName) LIKE :search " +
           "OR LOWER(r.platform) LIKE :search OR LOWER(r.versionName) LIKE :search) " +
           "AND (:platform IS NULL OR UPPER(r.platform) = :platform) " +
           "ORDER BY r.releaseDate DESC NULLS LAST, r.createdAt DESC")
    Page<AppReleaseEntity> searchActive(
        @Param("search") String search,
        @Param("platform") String platform,
        Pageable pageable);

    @Query("SELECT r FROM AppReleaseEntity r WHERE r.status = 'ACT' " +
           "AND UPPER(r.platform) = UPPER(:platform) " +
           "ORDER BY r.releaseDate DESC NULLS LAST, r.createdAt DESC")
    List<AppReleaseEntity> findActiveByPlatform(@Param("platform") String platform);
}
