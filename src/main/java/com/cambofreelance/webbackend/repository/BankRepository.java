package com.cambofreelance.webbackend.repository;

import com.cambofreelance.webbackend.entities.BankEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, String> {

    Optional<BankEntity> findByCode(String code);

    List<BankEntity> findByActiveTrue();

    boolean existsByCode(String code);
}
