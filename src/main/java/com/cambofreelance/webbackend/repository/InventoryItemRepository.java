package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.InventoryItemEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, String>,
    JpaSpecificationExecutor<InventoryItemEntity> {

    Optional<InventoryItemEntity> findByVin(String vin);

    Optional<InventoryItemEntity> findBySerialNo(String serialNo);

    Optional<InventoryItemEntity> findByEngineNo(String engineNo);

    boolean existsByVin(String vin);

    boolean existsByVinAndIdNot(String vin, String id);

    boolean existsBySerialNo(String serialNo);

    boolean existsBySerialNoAndIdNot(String serialNo, String id);

    boolean existsByEngineNo(String engineNo);

    boolean existsByEngineNoAndIdNot(String engineNo, String id);

    List<InventoryItemEntity> findByVariantId(String variantId);

    List<InventoryItemEntity> findByVariantIdAndItemStatus(String variantId, String itemStatus);

    long countByVariantIdAndItemStatus(String variantId, String itemStatus);

    long countByWarehouseIdAndItemStatus(String warehouseId, String itemStatus);

    @Query("SELECT i.itemStatus, COUNT(i) FROM InventoryItemEntity i WHERE i.variantId = :variantId GROUP BY i.itemStatus")
    List<Object[]> countStatusByVariantId(@Param("variantId") String variantId);
}
