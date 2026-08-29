package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.InventoryReservationEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryReservationRepository extends JpaRepository<InventoryReservationEntity, String>,
    JpaSpecificationExecutor<InventoryReservationEntity> {

    Optional<InventoryReservationEntity> findByInventoryItemIdAndStatus(String inventoryItemId, String status);

    List<InventoryReservationEntity> findByInventoryItemId(String inventoryItemId);

    List<InventoryReservationEntity> findBySaleId(String saleId);

    List<InventoryReservationEntity> findByCustomerId(String customerId);

    boolean existsByInventoryItemIdAndStatus(String inventoryItemId, String status);
}
