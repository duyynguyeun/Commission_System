package com.comission.system.controller;

import com.comission.system.dto.request.customerOrder.CustomerOrderReqDTO;
import com.comission.system.dto.response.ApiResponse;
import com.comission.system.dto.response.customerOrder.CustomerOrderResDTO;
import com.comission.system.dto.response.page.PageResponse;
import com.comission.system.service.CustomerOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;

    @PostMapping
    public ApiResponse<CustomerOrderResDTO> create(@Valid @RequestBody CustomerOrderReqDTO reqDTO) {
        return ApiResponse.success(customerOrderService.create(reqDTO));
    }

    @PostMapping("{id}")
    public ApiResponse<CustomerOrderResDTO> update(@PathVariable Long id, @Valid @RequestBody CustomerOrderReqDTO reqDTO) {
        return ApiResponse.success(customerOrderService.update(id, reqDTO));
    }

    @DeleteMapping("{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        customerOrderService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<PageResponse<CustomerOrderResDTO>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<CustomerOrderResDTO> data = customerOrderService.findAll(pageable);
        return ApiResponse.success(new PageResponse<>(data));
    }
}
