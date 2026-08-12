package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.UserSubscriptionEntity;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscriptionEntity, String> {

    List<UserSubscriptionEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    List<UserSubscriptionEntity> findByUserIdAndSubStatus(String userId, String subStatus);

    Optional<UserSubscriptionEntity> findFirstByUserIdAndSubStatusAndExpiresAtAfterOrderByExpiresAtDesc(
        String userId, String subStatus, Date now);

    Page<UserSubscriptionEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<UserSubscriptionEntity> findBySubStatusAndExpiresAtBefore(String subStatus, Date cutoff);
}
