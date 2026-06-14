package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.AuditLogEntity;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuditLogRepository
    extends JpaRepository<AuditLogEntity, String>, JpaSpecificationExecutor<AuditLogEntity> {

    @Modifying
    @Query("DELETE FROM AuditLogEntity a WHERE a.createdAt < :cutoff")
    int deleteByCreatedAtBefore(@Param("cutoff") Date cutoff);
}
