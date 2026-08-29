package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.ProductAttributeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttributeEntity, String> {

    List<ProductAttributeEntity> findByProductId(String productId);

    void deleteByProductId(String productId);
}
