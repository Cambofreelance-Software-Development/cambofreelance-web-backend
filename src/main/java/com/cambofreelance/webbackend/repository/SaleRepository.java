package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.SaleEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, String>,
    JpaSpecificationExecutor<SaleEntity> {

    Optional<SaleEntity> findBySaleNo(String saleNo);

    List<SaleEntity> findByCustomerId(String customerId);

    long countBySaleStatus(String saleStatus);

    @Query(value = "SELECT nextval('sale_no_seq')", nativeQuery = true)
    long nextSaleSequence();
}
