package com.comission.system.service;

import com.comission.system.dto.request.customer.CustomerReqDTO;
import com.comission.system.dto.response.customer.CustomerResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerResDTO create(CustomerReqDTO reqDTO);
    CustomerResDTO update(Long id, CustomerReqDTO reqDTO);
    void delete(Long id);
    Page<CustomerResDTO> findAll(Pageable pageable);
}
