package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.BillingSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingSettingRepository extends JpaRepository<BillingSettingEntity, String> {
}
