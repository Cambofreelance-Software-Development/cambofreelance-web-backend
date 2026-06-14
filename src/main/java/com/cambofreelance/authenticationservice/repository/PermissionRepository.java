package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.entities.PermissionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {
    List<PermissionEntity> findAllByOrderBySortOrderAsc();
}
