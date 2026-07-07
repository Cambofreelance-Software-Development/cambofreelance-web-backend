package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PricingFeatureEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingFeatureRepository extends JpaRepository<PricingFeatureEntity, String> {

    List<PricingFeatureEntity> findByStatusOrderBySortOrderAsc(String status);

    @Query("SELECT f FROM PricingFeatureEntity f WHERE f.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(f.name) LIKE :search)")
    Page<PricingFeatureEntity> searchActive(@Param("search") String search, Pageable pageable);
}
