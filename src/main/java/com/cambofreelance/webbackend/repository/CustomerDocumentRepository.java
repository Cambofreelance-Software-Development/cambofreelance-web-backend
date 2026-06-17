package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.CustomerDocumentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDocumentRepository extends JpaRepository<CustomerDocumentEntity, String> {

    List<CustomerDocumentEntity> findByCustomerIdAndStatus(String customerId, String status);

    Optional<CustomerDocumentEntity> findByCustomerIdAndDocumentTypeAndStatus(
        String customerId, String documentType, String status);
}
