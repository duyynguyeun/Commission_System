package com.comission.system.controller;

import com.comission.system.dto.request.employee.EmployeeReqDTO;
import com.comission.system.dto.response.ApiResponse;
import com.comission.system.dto.response.employee.EmployeeResDTO;
import com.comission.system.dto.response.page.PageResponse;
import com.comission.system.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public ApiResponse<EmployeeResDTO> create(@Valid @RequestBody EmployeeReqDTO reqDTO) {
        return ApiResponse.success(employeeService.create(reqDTO));
    }

    @PostMapping("{id}")
    public ApiResponse<EmployeeResDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeReqDTO reqDTO) {
        return ApiResponse.success(employeeService.update(id, reqDTO));
    }

    @DeleteMapping("{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<PageResponse<EmployeeResDTO>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<EmployeeResDTO> data = employeeService.findAll(pageable);
        return ApiResponse.success(new PageResponse<>(data));
    }

    @GetMapping("/role/{role}")
    public ApiResponse<java.util.List<EmployeeResDTO>> findByRole(@PathVariable com.comission.system.enums.EmployeeEnum role) {
        return ApiResponse.success(employeeService.findByRole(role));
    }
}
