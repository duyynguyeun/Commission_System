package com.comission.system.service.impl;

import com.comission.system.dto.request.employee.EmployeeReqDTO;
import com.comission.system.dto.response.employee.EmployeeResDTO;
import com.comission.system.entity.Employee;
import com.comission.system.exception.BusinessException;
import com.comission.system.exception.ErrorCode;
import com.comission.system.mapper.EmployeeMapper;
import com.comission.system.repository.AccountRepository;
import com.comission.system.repository.EmployeeRepository;
import com.comission.system.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final EmployeeMapper employeeMapper;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public EmployeeResDTO create(EmployeeReqDTO reqDTO) {
        if (accountRepository.existsByUsername(reqDTO.getUsername())) {
            throw new BusinessException(ErrorCode.EMPLOYEE_002);
        }
        Employee employee = employeeMapper.toEntity(reqDTO);
        employee.getAccount().setPassword(passwordEncoder.encode(reqDTO.getPassword()));
        employee.setCreateAt(Instant.now());
        employee.setUpdateAt(Instant.now());
        employee.getAccount().setCreateAt(Instant.now());
        employee.getAccount().setUpdateAt(Instant.now());
        employeeRepository.save(employee);
        return employeeMapper.toResponse(employee);
    }

    @Override
    public EmployeeResDTO update(Long id, EmployeeReqDTO reqDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_001));
        employeeMapper.updateFromReq(reqDTO, employee);
        employee.setUpdateAt(Instant.now());
        employee.getAccount().setUpdateAt(Instant.now());
        employeeRepository.save(employee);
        return employeeMapper.toResponse(employee);
    }

    @Override
    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.EMPLOYEE_001);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public Page<EmployeeResDTO> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(employeeMapper::toResponse);
    }

    @Override
    public java.util.List<EmployeeResDTO> findByRole(com.comission.system.enums.EmployeeEnum role) {
        return employeeRepository.findByAccount_Role(role).stream()
                .map(employeeMapper::toResponse)
                .toList();
    }
}
