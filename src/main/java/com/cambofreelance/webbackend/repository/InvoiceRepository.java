package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.InvoiceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, String> {

    List<InvoiceEntity> findByUserIdOrderByIssuedAtDesc(String userId);

    Optional<InvoiceEntity> findByTransactionId(String transactionId);

    Page<InvoiceEntity> findAllByOrderByIssuedAtDesc(Pageable pageable);

    Page<InvoiceEntity> findByInvoiceStatusOrderByIssuedAtDesc(String invoiceStatus, Pageable pageable);

    long countByInvoiceNoStartingWith(String prefix);
}
