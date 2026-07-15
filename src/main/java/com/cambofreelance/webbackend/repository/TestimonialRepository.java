package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.TestimonialEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialRepository extends JpaRepository<TestimonialEntity, String> {

    List<TestimonialEntity> findByStatusOrderBySortOrderAsc(String status);

    @Query("SELECT t FROM TestimonialEntity t WHERE t.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(t.authorName) LIKE :search OR LOWER(t.quote) LIKE :search)")
    Page<TestimonialEntity> searchActive(@Param("search") String search, Pageable pageable);
}
