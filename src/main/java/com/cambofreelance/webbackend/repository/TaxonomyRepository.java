package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.TaxonomyEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxonomyRepository
    extends JpaRepository<TaxonomyEntity, String>, JpaSpecificationExecutor<TaxonomyEntity> {

    Optional<TaxonomyEntity> findByCodeAndStatusNot(String code, String status);

    boolean existsByCodeAndStatusNot(String code, String status);
}
