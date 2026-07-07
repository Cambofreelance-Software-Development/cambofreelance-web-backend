package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PricingPlanEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingPlanRepository extends JpaRepository<PricingPlanEntity, String> {

    List<PricingPlanEntity> findByStatusOrderBySortOrderAsc(String status);

    @Query("SELECT p FROM PricingPlanEntity p WHERE p.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(p.name) LIKE :search)")
    Page<PricingPlanEntity> searchActive(@Param("search") String search, Pageable pageable);
}
