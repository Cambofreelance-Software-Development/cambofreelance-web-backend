package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PermissionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {
    List<PermissionEntity> findAllByOrderBySortOrderAsc();

    List<PermissionEntity> findAllByCodeIn(List<String> codes);
}
