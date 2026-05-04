package com.comission.system.controller;

import com.comission.system.dto.request.customer.CustomerReqDTO;
import com.comission.system.dto.response.ApiResponse;
import com.comission.system.dto.response.customer.CustomerResDTO;
import com.comission.system.dto.response.page.PageResponse;
import com.comission.system.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ApiResponse<CustomerResDTO> create(@Valid @RequestBody CustomerReqDTO reqDTO) {
        return ApiResponse.success(customerService.create(reqDTO));
    }

    @PostMapping("{id}")
    public ApiResponse<CustomerResDTO> update(@PathVariable Long id, @Valid @RequestBody CustomerReqDTO reqDTO) {
        return ApiResponse.success(customerService.update(id, reqDTO));
    }

    @DeleteMapping("{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<PageResponse<CustomerResDTO>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<CustomerResDTO> data = customerService.findAll(pageable);
        return ApiResponse.success(new PageResponse<>(data));
    }
}
