package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.TaxonomyItemEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaxonomyItemRepository extends JpaRepository<TaxonomyItemEntity, String> {

    Optional<TaxonomyItemEntity> findByCodeAndStatus(String code, String status);

    Optional<TaxonomyItemEntity> findByCode(String code);

    Page<TaxonomyItemEntity> findAll(Specification<TaxonomyItemEntity> spec, Pageable pageable);
}
