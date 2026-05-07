package com.comission.system.repository;

import com.comission.system.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    boolean existsByCustomer_Id(Long customerId);
}