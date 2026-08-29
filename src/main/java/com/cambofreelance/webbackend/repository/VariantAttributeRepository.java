package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.VariantAttributeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantAttributeRepository extends JpaRepository<VariantAttributeEntity, String> {

    List<VariantAttributeEntity> findByVariantId(String variantId);

    List<VariantAttributeEntity> findByVariantIdIn(List<String> variantIds);

    void deleteByVariantId(String variantId);
}
