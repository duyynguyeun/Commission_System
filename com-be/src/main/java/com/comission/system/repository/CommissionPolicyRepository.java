package com.comission.system.repository;

import com.comission.system.entity.CommissionPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionPolicyRepository extends JpaRepository<CommissionPolicy, Long> {
}