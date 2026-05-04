package com.comission.system.service;

import com.comission.system.dto.request.orderDetail.OrderDetailReqDTO;
import com.comission.system.dto.response.orderDetail.OrderDetailResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderDetailService {
    OrderDetailResDTO create(OrderDetailReqDTO reqDTO);
    OrderDetailResDTO update(Long id, OrderDetailReqDTO reqDTO);
    void delete(Long id);
    Page<OrderDetailResDTO> findAll(Pageable pageable);
}
