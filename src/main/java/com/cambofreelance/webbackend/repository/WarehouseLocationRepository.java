package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.WarehouseLocationEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseLocationRepository extends JpaRepository<WarehouseLocationEntity, String> {

    List<WarehouseLocationEntity> findByWarehouseId(String warehouseId);

    Optional<WarehouseLocationEntity> findByWarehouseIdAndCode(String warehouseId, String code);

    boolean existsByWarehouseIdAndCode(String warehouseId, String code);

    void deleteByWarehouseId(String warehouseId);
}
