package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.InventoryMovementEntity;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovementEntity, String>,
    JpaSpecificationExecutor<InventoryMovementEntity> {

    List<InventoryMovementEntity> findByVariantIdOrderByCreatedAtDesc(String variantId);

    List<InventoryMovementEntity> findByInventoryItemIdOrderByCreatedAtAsc(String inventoryItemId);

    List<InventoryMovementEntity> findByReferenceTypeAndReferenceId(String referenceType, String referenceId);

    @Query("SELECT COALESCE(SUM(m.quantity), 0) FROM InventoryMovementEntity m WHERE m.variantId = :variantId")
    BigDecimal calculateNetStockByVariantId(@Param("variantId") String variantId);
}
