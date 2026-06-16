package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.SubscriptionPackageFeatureEntity;
import com.cambofreelance.webbackend.entities.SubscriptionPackageFeatureId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPackageFeatureRepository
    extends JpaRepository<SubscriptionPackageFeatureEntity, SubscriptionPackageFeatureId> {

    List<SubscriptionPackageFeatureEntity> findByIdPackageId(String packageId);

    void deleteByIdPackageId(String packageId);
}
