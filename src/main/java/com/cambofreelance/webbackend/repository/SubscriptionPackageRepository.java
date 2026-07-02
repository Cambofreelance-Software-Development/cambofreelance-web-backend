package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.SubscriptionPackageEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubscriptionPackageRepository extends JpaRepository<SubscriptionPackageEntity, String>,
    JpaSpecificationExecutor<SubscriptionPackageEntity> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, String id);

    Optional<SubscriptionPackageEntity> findByCode(String code);
}
