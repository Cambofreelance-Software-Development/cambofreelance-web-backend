package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.PromiseToPayEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromiseToPayRepository extends JpaRepository<PromiseToPayEntity, String> {

    List<PromiseToPayEntity> findByCollectionCaseIdOrderByCreatedAtDesc(String collectionCaseId);
}
