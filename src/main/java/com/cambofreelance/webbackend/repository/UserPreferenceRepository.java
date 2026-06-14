package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.UserPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreferenceEntity, String> {
}
