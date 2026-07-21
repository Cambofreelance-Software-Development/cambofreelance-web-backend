package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.ContactMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessageEntity, String> {

    @Query("""
        SELECT m FROM ContactMessageEntity m
        WHERE m.status != 'DEL'
          AND (:isRead IS NULL OR m.isRead = :isRead)
          AND (:search IS NULL OR LOWER(m.fullName) LIKE :search
                               OR LOWER(m.email)    LIKE :search
                               OR LOWER(m.subject)  LIKE :search)
        ORDER BY m.createdAt DESC
        """)
    Page<ContactMessageEntity> findFiltered(
        @Param("search") String search,
        @Param("isRead") Boolean isRead,
        Pageable pageable
    );

    long countByIsReadFalseAndStatusNot(String status);
}
