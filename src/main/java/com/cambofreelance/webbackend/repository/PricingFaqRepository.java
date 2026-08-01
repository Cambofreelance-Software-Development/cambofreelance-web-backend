package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PricingFaqEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingFaqRepository extends JpaRepository<PricingFaqEntity, String> {

    List<PricingFaqEntity> findByStatusOrderBySortOrderAsc(String status);

    @Query("SELECT f FROM PricingFaqEntity f WHERE f.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(f.question) LIKE :search OR LOWER(f.answer) LIKE :search)")
    Page<PricingFaqEntity> searchActive(@Param("search") String search, Pageable pageable);
}
