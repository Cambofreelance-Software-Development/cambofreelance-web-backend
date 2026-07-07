package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.CategoryFeatureEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryFeatureRepository extends JpaRepository<CategoryFeatureEntity, String> {

    List<CategoryFeatureEntity> findByStatusOrderBySortOrderAsc(String status);

    @Query("SELECT c FROM CategoryFeatureEntity c WHERE c.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(c.name) LIKE :search)")
    Page<CategoryFeatureEntity> searchActive(@Param("search") String search, Pageable pageable);
}
