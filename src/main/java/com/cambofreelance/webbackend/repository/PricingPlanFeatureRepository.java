package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PricingPlanFeatureEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingPlanFeatureRepository extends JpaRepository<PricingPlanFeatureEntity, String> {

    List<PricingPlanFeatureEntity> findByPlanIdAndStatusOrderBySortOrderAsc(String planId, String status);

    // Bulk delete so the rows are gone before the replacement bullets are flushed
    // (Hibernate orders INSERTs before queued entity DELETEs within one transaction).
    @Modifying
    @Query("DELETE FROM PricingPlanFeatureEntity f WHERE f.planId = :planId")
    void deleteByPlanId(@Param("planId") String planId);
}
