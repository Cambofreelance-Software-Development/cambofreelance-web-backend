package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.entities.ArticleEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, String id);

    Optional<ArticleEntity> findBySlugAndStatusNot(String slug, String status);

    Optional<ArticleEntity> findBySlugAndWorkflowStatusAndStatusNot(
        String slug, String workflowStatus, String status);

    @Query("""
        SELECT a FROM ArticleEntity a
        WHERE a.status != 'DEL'
          AND (:type           IS NULL OR a.type           = :type)
          AND (:workflowStatus IS NULL OR a.workflowStatus = :workflowStatus)
          AND (:authorId       IS NULL OR a.authorId       = :authorId)
          AND (:search         IS NULL OR LOWER(a.title)   LIKE :search
                                       OR LOWER(a.excerpt) LIKE :search)
        ORDER BY a.sortOrder ASC, a.createdAt DESC
        """)
    Page<ArticleEntity> findFiltered(
        @Param("type")           String type,
        @Param("workflowStatus") String workflowStatus,
        @Param("authorId")       String authorId,
        @Param("search")         String search,
        Pageable pageable
    );
}
