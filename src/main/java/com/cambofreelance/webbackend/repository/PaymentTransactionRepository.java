package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PaymentTransactionEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransactionEntity, String> {

    Optional<PaymentTransactionEntity> findByTranId(String tranId);

    List<PaymentTransactionEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    Page<PaymentTransactionEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<PaymentTransactionEntity> findByPaymentStatusAndCreatedAtBefore(String paymentStatus, java.util.Date cutoff);

    /** Sum of transactions carrying this referrerId at the given payment status — basis for future commission calc. */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PaymentTransactionEntity p "
         + "WHERE p.referrerId = :referrerId AND p.paymentStatus = :paymentStatus")
    BigDecimal sumAmountByReferrerIdAndPaymentStatus(
        @Param("referrerId") String referrerId, @Param("paymentStatus") String paymentStatus);
}
