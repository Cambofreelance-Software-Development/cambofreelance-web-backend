package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.SaleItemEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItemEntity, String> {

    List<SaleItemEntity> findBySaleId(String saleId);

    Optional<SaleItemEntity> findByInventoryItemId(String inventoryItemId);

    void deleteBySaleId(String saleId);
}
