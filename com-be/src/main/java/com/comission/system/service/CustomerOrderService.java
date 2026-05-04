package com.comission.system.service;

import com.comission.system.dto.request.customerOrder.CustomerOrderReqDTO;
import com.comission.system.dto.response.customerOrder.CustomerOrderResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerOrderService {
    CustomerOrderResDTO create(CustomerOrderReqDTO reqDTO);
    CustomerOrderResDTO update(Long id, CustomerOrderReqDTO reqDTO);
    void delete(Long id);
    Page<CustomerOrderResDTO> findAll(Pageable pageable);
}
