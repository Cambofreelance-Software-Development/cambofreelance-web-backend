package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.ProductEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    // Eager-fetch category/image in one round trip — avoids N+1 lazy loads per row on listing.
    @EntityGraph(attributePaths = {"category", "image"})
    List<ProductEntity> findByStatusOrderBySortOrderAsc(String status);

    @EntityGraph(attributePaths = {"category", "image"})
    @Query("SELECT p FROM ProductEntity p WHERE p.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(p.name) LIKE :search)")
    Page<ProductEntity> searchActive(@Param("search") String search, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "image"})
    List<ProductEntity> findByStatusAndCategory_IdOrderBySortOrderAsc(String status, String categoryId);
}
