package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PricingPlanValueEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingPlanValueRepository extends JpaRepository<PricingPlanValueEntity, String> {

    List<PricingPlanValueEntity> findByFeatureId(String featureId);

    // Bulk deletes run against the DB immediately. The derived delete versions queued the
    // removals until flush, where Hibernate orders INSERTs before DELETEs — so re-inserting
    // values for the same feature violated uq_pricing_plan_value (plan_id, feature_id).
    @Modifying
    @Query("DELETE FROM PricingPlanValueEntity v WHERE v.featureId = :featureId")
    void deleteByFeatureId(@Param("featureId") String featureId);

    @Modifying
    @Query("DELETE FROM PricingPlanValueEntity v WHERE v.planId = :planId")
    void deleteByPlanId(@Param("planId") String planId);
}
