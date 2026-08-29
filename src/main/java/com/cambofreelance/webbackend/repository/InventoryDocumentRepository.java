package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.InventoryDocumentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDocumentRepository extends JpaRepository<InventoryDocumentEntity, String>,
    JpaSpecificationExecutor<InventoryDocumentEntity> {

    List<InventoryDocumentEntity> findByOwnerTypeAndOwnerIdOrderByCreatedAtDesc(String ownerType, String ownerId);

    List<InventoryDocumentEntity> findByOwnerTypeAndOwnerIdAndDocumentStatus(String ownerType, String ownerId, String documentStatus);

    long countByOwnerTypeAndOwnerId(String ownerType, String ownerId);

    void deleteByOwnerTypeAndOwnerId(String ownerType, String ownerId);
}
