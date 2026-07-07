package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.CategoryHardwareEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryHardwareRepository extends JpaRepository<CategoryHardwareEntity, String> {

    List<CategoryHardwareEntity> findByStatusOrderBySortOrderAsc(String status);

    @Query("SELECT c FROM CategoryHardwareEntity c WHERE c.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(c.name) LIKE :search)")
    Page<CategoryHardwareEntity> searchActive(@Param("search") String search, Pageable pageable);
}
