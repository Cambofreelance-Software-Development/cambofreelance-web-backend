package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TenantRepository extends JpaRepository<TenantEntity, String>,
    JpaSpecificationExecutor<TenantEntity> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, String id);
}
