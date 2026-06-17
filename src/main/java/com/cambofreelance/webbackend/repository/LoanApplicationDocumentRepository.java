package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.LoanApplicationDocumentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanApplicationDocumentRepository
    extends JpaRepository<LoanApplicationDocumentEntity, String> {

    List<LoanApplicationDocumentEntity> findByLoanApplicationIdAndStatus(
        String loanApplicationId, String status);

    Optional<LoanApplicationDocumentEntity> findByLoanApplicationIdAndDocumentTypeAndStatus(
        String loanApplicationId, String documentType, String status);
}
