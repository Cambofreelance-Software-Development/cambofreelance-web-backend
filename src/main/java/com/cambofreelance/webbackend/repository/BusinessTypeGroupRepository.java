package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.BusinessTypeGroupEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessTypeGroupRepository extends JpaRepository<BusinessTypeGroupEntity, String> {

    List<BusinessTypeGroupEntity> findByStatusOrderBySortOrderAsc(String status);
}
