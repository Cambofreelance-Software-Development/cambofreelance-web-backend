package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.CollectionCaseEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionCaseRepository extends JpaRepository<CollectionCaseEntity, String> {

    Optional<CollectionCaseEntity> findByLoanApplicationId(String loanApplicationId);

    @Query("""
        SELECT c FROM CollectionCaseEntity c
        WHERE (:collectionStatus IS NULL OR c.collectionStatus = :collectionStatus)
          AND (:assignedOfficerId IS NULL OR c.assignedOfficerId = :assignedOfficerId)
          AND (:currency IS NULL OR c.currency = :currency)
        ORDER BY c.dpd DESC, c.createdAt DESC
        """)
    Page<CollectionCaseEntity> search(
        @Param("collectionStatus") String collectionStatus,
        @Param("assignedOfficerId") String assignedOfficerId,
        @Param("currency") String currency,
        Pageable pageable
    );
}
