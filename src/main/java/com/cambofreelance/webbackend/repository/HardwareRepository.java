package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.HardwareEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HardwareRepository extends JpaRepository<HardwareEntity, String> {

    // Eager-fetch category/image/images in one round trip — without this, listing N hardware
    // rows lazily loads each association per row (N+1), which against the remote dev DB took
    // 16+ seconds for ~100 rows and blew past the frontend's 10s axios timeout.
    @EntityGraph(attributePaths = {"category", "image", "images"})
    List<HardwareEntity> findByStatusOrderBySortOrderAsc(String status);

    // images is excluded here: fetching a collection association alongside Pageable pagination
    // forces Hibernate to paginate in memory, which we want to avoid for the search endpoint.
    @EntityGraph(attributePaths = {"category", "image"})
    @Query("SELECT h FROM HardwareEntity h WHERE h.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(h.name) LIKE :search OR LOWER(h.brand) LIKE :search)")
    Page<HardwareEntity> searchActive(@Param("search") String search, Pageable pageable);
}
