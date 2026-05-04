package com.comission.system.repository;

import com.comission.system.entity.CommissionPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommissionPolicyRepository extends JpaRepository<CommissionPolicy, Long> {
    boolean existsByProduct_Id(Long productId);
    Optional<CommissionPolicy> findFirstByProduct_Id(Long productId);
}
