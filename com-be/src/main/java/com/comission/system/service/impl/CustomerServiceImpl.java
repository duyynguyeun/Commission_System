package com.comission.system.service.impl;

import com.comission.system.dto.request.customer.CustomerReqDTO;
import com.comission.system.dto.response.customer.CustomerResDTO;
import com.comission.system.entity.Customer;
import com.comission.system.exception.BusinessException;
import com.comission.system.exception.ErrorCode;
import com.comission.system.mapper.CustomerMapper;
import com.comission.system.repository.AccountRepository;
import com.comission.system.repository.CustomerRepository;
import com.comission.system.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResDTO create(CustomerReqDTO reqDTO) {
        if (accountRepository.existsByUsername(reqDTO.getUsername())) {
            throw new BusinessException(ErrorCode.CUSTOMER_002);
        }
        Customer customer = customerMapper.toEntity(reqDTO);
        customer.setCreateAt(Instant.now());
        customer.setUpdateAt(Instant.now());
        customer.getAccount().setCreateAt(Instant.now());
        customer.getAccount().setUpdateAt(Instant.now());
        customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }

    @Override
    public CustomerResDTO update(Long id, CustomerReqDTO reqDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_001));
        customerMapper.updateFromReq(reqDTO, customer);
        customer.setUpdateAt(Instant.now());
        customer.getAccount().setUpdateAt(Instant.now());
        customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }

    @Override
    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.CUSTOMER_001);
        }
        customerRepository.deleteById(id);
    }

    @Override
    public Page<CustomerResDTO> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable).map(customerMapper::toResponse);
    }
}
