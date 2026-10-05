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

    /** This user's subscriptions that carry any SOP POS tenant identity — platform-provisioned
     *  (registration id), manually linked (any access field), or explicitly MANUAL (managed outside
     *  the platform, e.g. imported with POS "later") — newest first. The first row is carried
     *  forward onto a new subscription so the same tenant is reused, not duplicated. */
    @Query("SELECT s FROM UserSubscriptionEntity s WHERE s.userId = :userId "
         + "AND (s.posRegistrationId IS NOT NULL OR s.posClientCode IS NOT NULL "
         + "OR s.posBackendUrl IS NOT NULL OR s.posEmenuUrl IS NOT NULL OR s.posRootUser IS NOT NULL "
         + "OR s.posLinkMode = 'MANUAL') "
         + "ORDER BY s.createdAt DESC")
    List<UserSubscriptionEntity> findPosLinkedByUserIdOrderByCreatedAtDesc(@Param("userId") String userId);

    /** Whether a subscription of a DIFFERENT user already uses this POS registration id. */
    boolean existsByPosRegistrationIdAndUserIdNot(String posRegistrationId, String userId);

    /** ACTIVE subscriptions whose SOP POS sync failed — retried by SubscriptionJobs.
     *  Manually-linked tenants (pos_link_mode = MANUAL) are never auto-synced. */
    @Query("SELECT s FROM UserSubscriptionEntity s WHERE s.posSyncStatus = :status AND s.subStatus = :subStatus "
         + "AND (s.posLinkMode IS NULL OR s.posLinkMode <> :manualMode)")
    List<UserSubscriptionEntity> findPosSyncRetryCandidates(
        @Param("status") String status, @Param("subStatus") String subStatus,
        @Param("manualMode") String manualMode);

    List<UserSubscriptionEntity> findByUserIdAndSubStatusIn(String userId, java.util.Collection<String> subStatuses);

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
