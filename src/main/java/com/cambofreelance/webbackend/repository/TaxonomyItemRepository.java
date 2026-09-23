package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.TaxonomyItemEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxonomyItemRepository
    extends JpaRepository<TaxonomyItemEntity, String>, JpaSpecificationExecutor<TaxonomyItemEntity> {

    Optional<TaxonomyItemEntity> findByTaxonomyCodeAndCodeAndStatusNot(
        String taxonomyCode, String code, String status);

    boolean existsByTaxonomyCodeAndCodeAndStatusNot(String taxonomyCode, String code, String status);

    List<TaxonomyItemEntity> findByTaxonomyCodeAndStatusOrderBySortOrderAscDisplayEnAsc(
        String taxonomyCode, String status);

    List<TaxonomyItemEntity> findByTaxonomyCodeAndParentCodeAndStatusOrderBySortOrderAscDisplayEnAsc(
        String taxonomyCode, String parentCode, String status);

    // Used only by the delete endpoint: the frontend's deleteTaxonomyItem() posts just
    // { code } (no taxonomyCode) — see TaxonomyItemServiceImpl for how ambiguity is handled.
    List<TaxonomyItemEntity> findByCodeAndStatusNot(String code, String status);
}
