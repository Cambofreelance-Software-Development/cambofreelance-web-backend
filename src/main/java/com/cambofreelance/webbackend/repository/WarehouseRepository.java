package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.WarehouseEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseEntity, String>,
    JpaSpecificationExecutor<WarehouseEntity> {

    Optional<WarehouseEntity> findByCode(String code);

    Optional<WarehouseEntity> findByIsDefaultTrue();

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, String id);
}
