package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.HardwareEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HardwareRepository extends JpaRepository<HardwareEntity, String> {

    List<HardwareEntity> findByStatusOrderBySortOrderAsc(String status);

    @Query("SELECT h FROM HardwareEntity h WHERE h.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(h.name) LIKE :search OR LOWER(h.brand) LIKE :search)")
    Page<HardwareEntity> searchActive(@Param("search") String search, Pageable pageable);
}
