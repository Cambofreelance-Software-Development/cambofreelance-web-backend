package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PaymentTransactionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransactionEntity, String> {

    Optional<PaymentTransactionEntity> findByTranId(String tranId);

    List<PaymentTransactionEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    Page<PaymentTransactionEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<PaymentTransactionEntity> findByPaymentStatusAndCreatedAtBefore(String paymentStatus, java.util.Date cutoff);
}
