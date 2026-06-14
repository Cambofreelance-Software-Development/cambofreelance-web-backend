package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.entities.UserPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreferenceEntity, String> {
}
