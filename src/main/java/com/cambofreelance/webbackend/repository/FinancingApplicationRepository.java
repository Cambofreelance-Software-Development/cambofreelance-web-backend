package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.FinancingApplicationEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancingApplicationRepository extends JpaRepository<FinancingApplicationEntity, String>,
    JpaSpecificationExecutor<FinancingApplicationEntity> {

    Optional<FinancingApplicationEntity> findByApplicationNo(String applicationNo);

    Optional<FinancingApplicationEntity> findBySaleId(String saleId);

    Optional<FinancingApplicationEntity> findByExternalReference(String externalReference);

    List<FinancingApplicationEntity> findByCustomerId(String customerId);

    long countByStatus(String status);

    @Query(value = "SELECT nextval('financing_app_no_seq')", nativeQuery = true)
    long nextFinancingSequence();
}
