package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUsernameAndStatus(String username, String status);

    Optional<UserEntity> findByUserIdAndStatus(String userId, String status);

    Optional<UserEntity> findByPhoneNumberAndStatus(String phoneNumber, String status);

    Optional<UserEntity> findByEmailAndStatus(String email, String status);

    Optional<UserEntity> findByReferralCodeAndStatus(String referralCode, String status);

    boolean existsByReferralCode(String referralCode);

    /** Count of accounts registered with this user's referral code. */
    long countByReferredBy(String referredBy);

    /** Accounts registered with this user's referral code, newest first. */
    Page<UserEntity> findByReferredByOrderByCreatedAtDesc(String referredBy, Pageable pageable);

    @Query("SELECT DISTINCT p.code FROM UserEntity u " +
           "JOIN u.roles r " +
           "JOIN r.permissions p " +
           "WHERE u.userId = :userId AND r.status = 'ACT'")
    Set<String> findActivePermissionCodesByUserId(@Param("userId") String userId);

    @Query("SELECT DISTINCT u FROM UserEntity u JOIN u.roles r "
         + "WHERE r.code IN :roleCodes AND u.status = :status")
    List<UserEntity> findByRoleCodesAndStatus(@Param("roleCodes") List<String> roleCodes, @Param("status") String status);
}
