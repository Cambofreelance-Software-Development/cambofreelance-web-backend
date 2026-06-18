package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.TaxonomyEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaxonomyRepository extends JpaRepository<TaxonomyEntity, String> {

    List<TaxonomyEntity> findAllByStatus(String status);

    Optional<TaxonomyEntity> findByCodeAndStatus(String code, String status);

    Optional<TaxonomyEntity> findByCode(String code);

    Page<TaxonomyEntity> findAll(Specification<TaxonomyEntity> spec, Pageable pageable);

}
