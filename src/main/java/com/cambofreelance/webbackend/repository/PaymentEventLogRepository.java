package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PaymentEventLogEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentEventLogRepository extends JpaRepository<PaymentEventLogEntity, String> {

    List<PaymentEventLogEntity> findByTransactionIdOrderByCreatedAtAsc(String transactionId);

    List<PaymentEventLogEntity> findTop20ByTransactionIdInOrderByCreatedAtDesc(List<String> transactionIds);
}
