package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PricingPlanFeatureEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingPlanFeatureRepository extends JpaRepository<PricingPlanFeatureEntity, String> {

    List<PricingPlanFeatureEntity> findByPlanIdAndStatusOrderBySortOrderAsc(String planId, String status);

    void deleteByPlanId(String planId);
}
