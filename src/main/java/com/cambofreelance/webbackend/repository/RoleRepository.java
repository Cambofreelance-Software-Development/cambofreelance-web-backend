package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.RoleEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String>,
    JpaSpecificationExecutor<RoleEntity> {

    Optional<RoleEntity> findByCode(String code);

    List<RoleEntity> findAllByStatus(String status);

    boolean existsByCodeAndStatusNot(String code, String status);

    List<RoleEntity> findByTenantIdAndStatusNot(String tenantId, String status);

    Optional<RoleEntity> findByIdAndTenantId(String id, String tenantId);
}
