package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.FeatureTabEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureTabRepository extends JpaRepository<FeatureTabEntity, String> {

    List<FeatureTabEntity> findByStatusOrderBySortOrderAsc(String status);
}
