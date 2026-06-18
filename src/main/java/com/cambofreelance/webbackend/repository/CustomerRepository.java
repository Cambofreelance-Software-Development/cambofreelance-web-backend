package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, String>,
    JpaSpecificationExecutor<CustomerEntity> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByIdentityCardNumber(String identityCardNumber);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, String id);

    boolean existsByIdentityCardNumberAndIdNot(String identityCardNumber, String id);

    @Query(value = "SELECT nextval('customer_id_seq')", nativeQuery = true)
    long nextCustomerSequence();
}
