package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.HelpCenterCategoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpCenterCategoryRepository extends JpaRepository<HelpCenterCategoryEntity, String> {

    List<HelpCenterCategoryEntity> findByStatusOrderByDisplayOrderAsc(String status);

    List<HelpCenterCategoryEntity> findByArticleTypeIdAndStatusOrderByDisplayOrderAsc(String articleTypeId, String status);

    boolean existsBySlugAndStatusNot(String slug, String status);

    boolean existsBySlugAndIdNotAndStatusNot(String slug, String id, String status);

    boolean existsByParentIdAndStatusNot(String parentId, String status);
}
