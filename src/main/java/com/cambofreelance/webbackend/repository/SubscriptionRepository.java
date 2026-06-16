package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.SubscriptionEntity;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, String>,
    JpaSpecificationExecutor<SubscriptionEntity> {

    Optional<SubscriptionEntity> findByTenantIdAndStatus(String tenantId, String status);

    List<SubscriptionEntity> findAllByTenantIdOrderByCreatedAtDesc(String tenantId);

    List<SubscriptionEntity> findByStatusAndEndDateLessThan(String status, Date date);

    List<SubscriptionEntity> findByStatus(String status);

    List<SubscriptionEntity> findByStatusAndEndDateBetween(String status, Date start, Date end);

    long countByStatus(String status);

    @Query(value = "SELECT nextval('subscription_id_seq')", nativeQuery = true)
    long nextSubscriptionSequence();
}
