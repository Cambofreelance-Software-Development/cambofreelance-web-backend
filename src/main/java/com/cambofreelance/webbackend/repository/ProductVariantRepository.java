package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.ProductVariantEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, String>,
    JpaSpecificationExecutor<ProductVariantEntity> {

    List<ProductVariantEntity> findByProductId(String productId);

    Optional<ProductVariantEntity> findBySku(String sku);

    Optional<ProductVariantEntity> findByBarcode(String barcode);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, String id);

    boolean existsByBarcode(String barcode);

    boolean existsByBarcodeAndIdNot(String barcode, String id);

    void deleteByProductId(String productId);
}
