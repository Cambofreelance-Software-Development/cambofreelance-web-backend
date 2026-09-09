package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PartnerPayoutEntity;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerPayoutRepository extends JpaRepository<PartnerPayoutEntity, String> {

    List<PartnerPayoutEntity> findByApplicationIdAndStatusOrderByPaidAtDesc(String applicationId, String status);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PartnerPayoutEntity p "
         + "WHERE p.applicationId = :applicationId AND p.status = :status")
    BigDecimal sumPaid(@Param("applicationId") String applicationId, @Param("status") String status);
}
