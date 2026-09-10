package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PartnerApplicationEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerApplicationRepository extends JpaRepository<PartnerApplicationEntity, String> {

    Optional<PartnerApplicationEntity> findByUserIdAndStatus(String userId, String status);

    Optional<PartnerApplicationEntity> findByIdAndStatus(String id, String status);

    boolean existsByPartnerRef(String partnerRef);

    long countByPartnerRefStartingWith(String prefix);

    Page<PartnerApplicationEntity> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    Page<PartnerApplicationEntity> findByAppStatusAndStatusOrderByCreatedAtDesc(
        String appStatus, String status, Pageable pageable);
}
