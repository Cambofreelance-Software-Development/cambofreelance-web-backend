package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.InventoryBatchEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryBatchRepository extends JpaRepository<InventoryBatchEntity, String>,
    JpaSpecificationExecutor<InventoryBatchEntity> {

    List<InventoryBatchEntity> findByVariantId(String variantId);

    List<InventoryBatchEntity> findByVariantIdAndWarehouseId(String variantId, String warehouseId);

    Optional<InventoryBatchEntity> findByVariantIdAndWarehouseIdAndBatchNo(String variantId, String warehouseId, String batchNo);

    boolean existsByVariantIdAndWarehouseIdAndBatchNo(String variantId, String warehouseId, String batchNo);

    @Query("SELECT COALESCE(SUM(b.quantity), 0) FROM InventoryBatchEntity b WHERE b.variantId = :variantId")
    BigDecimal sumQuantityByVariantId(@Param("variantId") String variantId);
}
