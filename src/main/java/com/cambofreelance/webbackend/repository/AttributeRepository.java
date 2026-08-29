package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.AttributeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<AttributeEntity, String>,
    JpaSpecificationExecutor<AttributeEntity> {

    Optional<AttributeEntity> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, String id);

    List<AttributeEntity> findByApplicableCategoryOrApplicableCategoryIsNullOrderBySortOrderAsc(String applicableCategory);

    List<AttributeEntity> findByIsVariantAttributeTrueOrderBySortOrderAsc();
}
