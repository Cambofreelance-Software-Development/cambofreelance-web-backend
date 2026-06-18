package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PaymentEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

    @Query(value = "SELECT nextval('payment_receipt_seq')", nativeQuery = true)
    Long nextReceiptSequence();

    List<PaymentEntity> findByLoanApplicationIdOrderByCreatedAtDesc(String loanApplicationId);

    @Query("""
        SELECT p FROM PaymentEntity p
        WHERE (:loanApplicationId IS NULL OR p.loanApplicationId = :loanApplicationId)
          AND (:paymentStatus IS NULL OR p.paymentStatus = :paymentStatus)
          AND (:paymentType IS NULL OR p.paymentType = :paymentType)
          AND (:currency IS NULL OR p.currency = :currency)
        ORDER BY p.createdAt DESC
        """)
    Page<PaymentEntity> search(
        @Param("loanApplicationId") String loanApplicationId,
        @Param("paymentStatus") String paymentStatus,
        @Param("paymentType") String paymentType,
        @Param("currency") String currency,
        Pageable pageable
    );
}
