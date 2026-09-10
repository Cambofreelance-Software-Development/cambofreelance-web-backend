package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.UserSubscriptionEntity;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscriptionEntity, String> {

    List<UserSubscriptionEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    List<UserSubscriptionEntity> findByUserIdAndSubStatus(String userId, String subStatus);

    Optional<UserSubscriptionEntity> findFirstByUserIdAndSubStatusAndExpiresAtAfterOrderByExpiresAtDesc(
        String userId, String subStatus, Date now);

    /** Most recent subscription of this user that already has a SOP POS tenant — used to carry the
     *  tenant identity forward when a lapsed subscriber re-subscribes (reactivate, don't duplicate). */
    Optional<UserSubscriptionEntity> findFirstByUserIdAndPosRegistrationIdIsNotNullOrderByCreatedAtDesc(String userId);

    /** ACTIVE subscriptions whose SOP POS sync failed — retried by SubscriptionJobs. */
    @Query("SELECT s FROM UserSubscriptionEntity s WHERE s.posSyncStatus = :status AND s.subStatus = :subStatus")
    List<UserSubscriptionEntity> findPosSyncRetryCandidates(
        @Param("status") String status, @Param("subStatus") String subStatus);

    Page<UserSubscriptionEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<UserSubscriptionEntity> findBySubStatusAndExpiresAtBefore(String subStatus, Date cutoff);

    /** Candidates for the (currently stubbed) Card-on-File renewal job — see SubscriptionJobs. */
    @Query("SELECT s FROM UserSubscriptionEntity s WHERE s.subStatus = :status AND s.autoRenew = true "
         + "AND s.paymentToken IS NOT NULL AND s.expiresAt BETWEEN :from AND :to "
         + "AND s.autoRenewFailureCount < :maxFailures "
         + "AND (s.autoRenewLastAttemptAt IS NULL OR s.autoRenewLastAttemptAt < :attemptCutoff)")
    List<UserSubscriptionEntity> findAutoRenewCandidates(@Param("status") String status, @Param("from") Date from,
        @Param("to") Date to, @Param("maxFailures") int maxFailures, @Param("attemptCutoff") Date attemptCutoff);

    /** ACTIVE subscriptions expiring within the window that haven't been notified at every threshold yet. */
    @Query("SELECT s FROM UserSubscriptionEntity s WHERE s.subStatus = :status "
         + "AND s.expiresAt BETWEEN :from AND :to "
         + "AND (s.notice7dSent = false OR s.notice3dSent = false OR s.notice1dSent = false)")
    List<UserSubscriptionEntity> findExpiryReminderCandidates(
        @Param("status") String status, @Param("from") Date from, @Param("to") Date to);

    /** Count of distinct referred users who have gone on to create at least one subscription. */
    @Query("SELECT COUNT(DISTINCT s.userId) FROM UserSubscriptionEntity s WHERE s.referrerId = :referrerId")
    long countDistinctUsersByReferrerId(@Param("referrerId") String referrerId);

    /** userIds of referred users who have gone on to create at least one subscription. */
    @Query("SELECT DISTINCT s.userId FROM UserSubscriptionEntity s WHERE s.referrerId = :referrerId")
    List<String> findDistinctUserIdsByReferrerId(@Param("referrerId") String referrerId);

    /** Distinct referred users holding a subscription in the given status — drives the partner tier. */
    @Query("SELECT COUNT(DISTINCT s.userId) FROM UserSubscriptionEntity s "
         + "WHERE s.referrerId = :referrerId AND s.subStatus = :subStatus")
    long countDistinctUsersByReferrerIdAndSubStatus(
        @Param("referrerId") String referrerId, @Param("subStatus") String subStatus);
}
