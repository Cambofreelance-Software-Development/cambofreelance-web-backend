package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.UserEntity;
import java.util.Optional;
import java.util.Set;
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

    Optional<UserEntity> findBySocialProviderAndSocialProviderId(String socialProvider, String socialProviderId);

    long countByTenantIdNotNullAndStatus(String status);

    long countByTenantIdAndStatus(String tenantId, String status);

    @Query("SELECT DISTINCT p.code FROM UserEntity u " +
           "JOIN u.roles r " +
           "JOIN r.permissions p " +
           "WHERE u.userId = :userId AND r.status = 'ACT'")
    Set<String> findActivePermissionCodesByUserId(@Param("userId") String userId);
}
