package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.ClientEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, String> {

    Optional<ClientEntity> findByUserId(String userId);

    // `search` must arrive already lowercased and wrapped in %...% (or null). Comparing a
    // bind via LIKE against LOWER(column) gives PostgreSQL a definite text type for the
    // parameter; passing a raw null into LOWER(CONCAT(...)) makes it infer bytea and fail.
    @Query("""
        SELECT c FROM ClientEntity c
        WHERE (:clientStatus IS NULL OR c.clientStatus = :clientStatus)
          AND (:search IS NULL
               OR LOWER(c.companyName)  LIKE :search
               OR LOWER(c.companyEmail) LIKE :search)
        ORDER BY c.createdAt DESC
        """)
    Page<ClientEntity> search(@Param("search") String search,
                              @Param("clientStatus") String clientStatus,
                              Pageable pageable);
}
