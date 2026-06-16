package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.InvoiceEntity;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, String>,
    JpaSpecificationExecutor<InvoiceEntity> {

    List<InvoiceEntity> findAllByTenantIdOrderByBillingPeriodDesc(String tenantId);

    boolean existsByTenantIdAndBillingPeriod(String tenantId, Date billingPeriod);

    @Query(value = "SELECT nextval('invoice_id_seq')", nativeQuery = true)
    long nextInvoiceSequence();
}
