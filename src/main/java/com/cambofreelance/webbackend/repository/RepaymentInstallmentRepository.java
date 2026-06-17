package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.RepaymentInstallmentEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepaymentInstallmentRepository extends JpaRepository<RepaymentInstallmentEntity, String> {

    List<RepaymentInstallmentEntity> findByLoanApplicationIdOrderByInstallmentNoAsc(String loanApplicationId);

    long countByLoanApplicationId(String loanApplicationId);

    boolean existsByLoanApplicationIdAndInstallmentStatusIn(String loanApplicationId, List<String> statuses);

    @Modifying
    @Query("DELETE FROM RepaymentInstallmentEntity r WHERE r.loanApplicationId = :loanApplicationId")
    void deleteByLoanApplicationId(@Param("loanApplicationId") String loanApplicationId);

    @Query("SELECT r FROM RepaymentInstallmentEntity r " +
           "WHERE r.loanApplicationId = :loanApplicationId " +
           "  AND r.installmentStatus = 'PENDING' " +
           "  AND r.dueDate < :today " +
           "ORDER BY r.installmentNo ASC")
    List<RepaymentInstallmentEntity> findOverdueInstallments(
        @Param("loanApplicationId") String loanApplicationId,
        @Param("today") Date today
    );

    @Query("SELECT DISTINCT r.loanApplicationId FROM RepaymentInstallmentEntity r WHERE r.installmentStatus = :status")
    List<String> findDistinctLoanApplicationIdsByInstallmentStatus(@Param("status") String status);

    Optional<RepaymentInstallmentEntity> findFirstByLoanApplicationIdAndInstallmentStatusOrderByDueDateAsc(
        String loanApplicationId, String installmentStatus
    );

    @Query("SELECT COALESCE(SUM(r.remainingBalance), 0) FROM RepaymentInstallmentEntity r " +
           "WHERE r.loanApplicationId = :loanApplicationId AND r.installmentStatus = :status")
    BigDecimal sumRemainingBalanceByStatus(
        @Param("loanApplicationId") String loanApplicationId,
        @Param("status") String status
    );
}
