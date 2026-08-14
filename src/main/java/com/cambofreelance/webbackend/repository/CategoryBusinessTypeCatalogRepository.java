package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.CategoryBusinessTypeCatalogEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryBusinessTypeCatalogRepository extends JpaRepository<CategoryBusinessTypeCatalogEntity, String> {

    List<CategoryBusinessTypeCatalogEntity> findByStatusOrderBySortOrderAsc(String status);

    @Query("SELECT c FROM CategoryBusinessTypeCatalogEntity c WHERE c.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(c.name) LIKE :search)")
    Page<CategoryBusinessTypeCatalogEntity> searchActive(@Param("search") String search, Pageable pageable);
}
