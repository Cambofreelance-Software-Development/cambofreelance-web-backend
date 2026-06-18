package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.LoanApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplicationEntity, String>,
    JpaSpecificationExecutor<LoanApplicationEntity> {

    @Query(value = "SELECT nextval('loan_application_id_seq')", nativeQuery = true)
    long nextLoanApplicationSequence();
}
