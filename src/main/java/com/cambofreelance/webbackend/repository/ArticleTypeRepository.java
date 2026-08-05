package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.ArticleTypeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTypeRepository extends JpaRepository<ArticleTypeEntity, String> {

    List<ArticleTypeEntity> findByStatusOrderBySortOrderAsc(String status);

    boolean existsByCodeAndStatusNot(String code, String status);

    Optional<ArticleTypeEntity> findByCodeAndStatusNot(String code, String status);
}
