package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.FeatureEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<FeatureEntity, String> {

    List<FeatureEntity> findByStatusOrderBySortOrderAsc(String status);

    @Query("SELECT f FROM FeatureEntity f WHERE f.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(f.title) LIKE :search OR LOWER(f.category) LIKE :search)")
    Page<FeatureEntity> searchActive(@Param("search") String search, Pageable pageable);
}
