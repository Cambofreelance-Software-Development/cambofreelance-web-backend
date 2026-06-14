package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.CmsSettingEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsSettingRepository extends JpaRepository<CmsSettingEntity, String> {

    List<CmsSettingEntity> findAllBySettingGroup(String settingGroup);

    Optional<CmsSettingEntity> findBySettingKey(String settingKey);
}
