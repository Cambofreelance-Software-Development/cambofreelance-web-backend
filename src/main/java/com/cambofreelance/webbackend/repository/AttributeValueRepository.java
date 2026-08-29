package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.AttributeValueEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValueEntity, String> {

    List<AttributeValueEntity> findByAttributeIdOrderBySortOrderAsc(String attributeId);

    List<AttributeValueEntity> findByAttributeIdInOrderBySortOrderAsc(List<String> attributeIds);

    Optional<AttributeValueEntity> findByAttributeIdAndCode(String attributeId, String code);

    boolean existsByAttributeIdAndCode(String attributeId, String code);

    void deleteByAttributeId(String attributeId);
}
