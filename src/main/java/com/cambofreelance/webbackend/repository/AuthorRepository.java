package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.AuthorEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, String> {

    List<AuthorEntity> findByStatusOrderBySortOrderAsc(String status);

    @Query("SELECT a FROM AuthorEntity a WHERE a.status = 'ACT' " +
           "AND (:search IS NULL OR LOWER(a.name) LIKE :search OR LOWER(a.email) LIKE :search)")
    Page<AuthorEntity> searchActive(@Param("search") String search, Pageable pageable);
}
