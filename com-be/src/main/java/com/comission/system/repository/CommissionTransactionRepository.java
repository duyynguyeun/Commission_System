package com.comission.system.repository;

import com.comission.system.entity.CommissionTransaction;
import com.comission.system.enums.EmployeeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommissionTransactionRepository extends JpaRepository<CommissionTransaction, Long> {
    List<CommissionTransaction> findByEmployee_Id(Long employeeId);
    List<CommissionTransaction> findByOrderDetail_IdIn(List<Long> orderDetailIds);
    boolean existsByOrderDetail_IdAndCommissionRole(Long orderDetailId, EmployeeEnum commissionRole);
}
