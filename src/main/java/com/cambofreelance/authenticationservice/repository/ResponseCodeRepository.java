package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.entities.ResponseCodeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseCodeRepository extends JpaRepository<ResponseCodeEntity, Long> {

    List<ResponseCodeEntity> findByStatus(String status);

    Optional<ResponseCodeEntity> findByCode(String code);

    boolean existsByCodeAndStatusNot(String code, String status);

    @Query(value = """
        SELECT * FROM public.response_codes r
        WHERE r.status != 'DEL'
        AND (CAST(:search AS TEXT) IS NULL
             OR LOWER(r.code)        LIKE LOWER('%' || CAST(:search AS TEXT) || '%')
             OR LOWER(r.description) LIKE LOWER('%' || CAST(:search AS TEXT) || '%'))
        AND (CAST(:type AS TEXT)        IS NULL OR r."type"        = CAST(:type        AS TEXT))
        AND (CAST(:status AS TEXT)      IS NULL OR r.status        = CAST(:status      AS TEXT))
        AND (CAST(:serviceType AS TEXT) IS NULL OR r.service_type  = CAST(:serviceType AS TEXT))
        ORDER BY r.code
        """,
        countQuery = """
        SELECT COUNT(*) FROM public.response_codes r
        WHERE r.status != 'DEL'
        AND (CAST(:search AS TEXT) IS NULL
             OR LOWER(r.code)        LIKE LOWER('%' || CAST(:search AS TEXT) || '%')
             OR LOWER(r.description) LIKE LOWER('%' || CAST(:search AS TEXT) || '%'))
        AND (CAST(:type AS TEXT)        IS NULL OR r."type"        = CAST(:type        AS TEXT))
        AND (CAST(:status AS TEXT)      IS NULL OR r.status        = CAST(:status      AS TEXT))
        AND (CAST(:serviceType AS TEXT) IS NULL OR r.service_type  = CAST(:serviceType AS TEXT))
        """,
        nativeQuery = true)
    Page<ResponseCodeEntity> search(
        @Param("search") String search,
        @Param("type") String type,
        @Param("status") String status,
        @Param("serviceType") String serviceType,
        Pageable pageable
    );
}