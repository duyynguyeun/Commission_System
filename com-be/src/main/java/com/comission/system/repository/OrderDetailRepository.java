package com.comission.system.repository;

import com.comission.system.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findBySeller_IdOrParent_Id(Long sellerId, Long parentId);
    List<OrderDetail> findByCustomerOrder_Id(Long customerOrderId);
}
