package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.ProductEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String>,
    JpaSpecificationExecutor<ProductEntity> {

    Optional<ProductEntity> findBySku(String sku);

    Optional<ProductEntity> findByBarcode(String barcode);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, String id);

    boolean existsByBarcode(String barcode);

    boolean existsByBarcodeAndIdNot(String barcode, String id);

    @Query(value = "SELECT nextval('product_code_seq')", nativeQuery = true)
    long nextProductSequence();
}
