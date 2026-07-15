package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.FaqEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<FaqEntity, String> {

    List<FaqEntity> findByStatusOrderBySortOrderAsc(String status);

    @Query("SELECT f FROM FaqEntity f WHERE f.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(f.question) LIKE :search OR LOWER(f.answer) LIKE :search)")
    Page<FaqEntity> searchActive(@Param("search") String search, Pageable pageable);
}
