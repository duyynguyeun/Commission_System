package com.comission.system.service;

import com.comission.system.dto.request.employee.EmployeeReqDTO;
import com.comission.system.dto.response.employee.EmployeeResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeResDTO create(EmployeeReqDTO reqDTO);
    EmployeeResDTO update(Long id, EmployeeReqDTO reqDTO);
    void delete(Long id);
    Page<EmployeeResDTO> findAll(Pageable pageable);
    java.util.List<EmployeeResDTO> findByRole(com.comission.system.enums.EmployeeEnum role);
}
