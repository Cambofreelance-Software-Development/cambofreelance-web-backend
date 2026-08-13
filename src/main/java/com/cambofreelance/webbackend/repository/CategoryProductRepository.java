package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.CategoryProductEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProductEntity, String> {

    List<CategoryProductEntity> findByStatusOrderBySortOrderAsc(String status);

    @Query("SELECT c FROM CategoryProductEntity c WHERE c.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(c.name) LIKE :search)")
    Page<CategoryProductEntity> searchActive(@Param("search") String search, Pageable pageable);
}
