package com.comission.system.repository;

import com.comission.system.entity.CommissionTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionTransactionRepository extends JpaRepository<CommissionTransaction, Long> {
}