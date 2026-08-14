package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.BusinessTypeCatalogEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessTypeCatalogRepository extends JpaRepository<BusinessTypeCatalogEntity, String> {

    // Eager-fetch category/image in one round trip — avoids N+1 lazy loads per row on listing.
    @EntityGraph(attributePaths = {"category", "image"})
    List<BusinessTypeCatalogEntity> findByStatusOrderBySortOrderAsc(String status);

    @EntityGraph(attributePaths = {"category", "image"})
    List<BusinessTypeCatalogEntity> findByStatusAndCategory_IdOrderBySortOrderAsc(String status, String categoryId);

    @EntityGraph(attributePaths = {"category", "image"})
    @Query("SELECT p FROM BusinessTypeCatalogEntity p WHERE p.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(p.name) LIKE :search)")
    Page<BusinessTypeCatalogEntity> searchActive(@Param("search") String search, Pageable pageable);
}
