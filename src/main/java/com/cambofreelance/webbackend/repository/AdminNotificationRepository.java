package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.AdminNotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminNotificationRepository extends JpaRepository<AdminNotificationEntity, String> {

    @Query("""
        SELECT n FROM AdminNotificationEntity n
        WHERE n.status != 'DEL'
          AND (:isRead IS NULL OR n.isRead = :isRead)
          AND (:type IS NULL OR n.type = :type)
        ORDER BY n.createdAt DESC
        """)
    Page<AdminNotificationEntity> findFiltered(
        @Param("isRead") Boolean isRead,
        @Param("type") String type,
        Pageable pageable
    );

    long countByIsReadFalseAndStatusNot(String status);
}
