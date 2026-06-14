package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.entities.ArticleTypeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTypeRepository extends JpaRepository<ArticleTypeEntity, String> {

    List<ArticleTypeEntity> findByStatusOrderBySortOrderAsc(String status);

    boolean existsByCodeAndStatusNot(String code, String status);
}
