package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.TenantEntity;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TenantRepository extends JpaRepository<TenantEntity, String>,
    JpaSpecificationExecutor<TenantEntity> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, String id);

    long countByStatus(String status);

    List<TenantEntity> findTop5ByStatusNotOrderByCreatedAtDesc(String status);

    List<TenantEntity> findByStatusAndPlanExpiredDateBetween(String status, Date start, Date end);
}
