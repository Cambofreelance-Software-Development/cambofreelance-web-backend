package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.entities.RefreshTokenEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {

  Optional<RefreshTokenEntity> findByRefreshToken(String token);

  Optional<RefreshTokenEntity> findByUserIdAndDeviceIdAndStatus(String userId, String deviceId,
      String status);

  Optional<RefreshTokenEntity> findByRefreshTokenAndStatus(String token, String status);

  List<RefreshTokenEntity> findAllByUserIdAndStatus(String userId, String status);

  boolean existsByUserIdAndDeviceId(String userId, String deviceId);

  Optional<RefreshTokenEntity> findTopByUserIdAndStatusOrderByCreatedAtAsc(String userId, String status);

}
