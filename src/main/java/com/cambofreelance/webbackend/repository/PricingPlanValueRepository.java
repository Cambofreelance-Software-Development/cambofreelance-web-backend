package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PricingPlanValueEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingPlanValueRepository extends JpaRepository<PricingPlanValueEntity, String> {

    List<PricingPlanValueEntity> findByFeatureId(String featureId);

    void deleteByFeatureId(String featureId);

    void deleteByPlanId(String planId);
}
