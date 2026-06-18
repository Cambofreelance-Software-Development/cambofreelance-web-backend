package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.LoanDisbursementEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanDisbursementRepository
    extends JpaRepository<LoanDisbursementEntity, String>, JpaSpecificationExecutor<LoanDisbursementEntity> {

    Optional<LoanDisbursementEntity> findByLoanApplicationId(String loanApplicationId);

    boolean existsByLoanApplicationId(String loanApplicationId);
}
