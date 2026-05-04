package com.comission.system.controller;

import com.comission.system.dto.request.orderDetail.OrderDetailReqDTO;
import com.comission.system.dto.response.ApiResponse;
import com.comission.system.dto.response.orderDetail.OrderDetailResDTO;
import com.comission.system.dto.response.page.PageResponse;
import com.comission.system.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PostMapping
    public ApiResponse<OrderDetailResDTO> create(@Valid @RequestBody OrderDetailReqDTO reqDTO) {
        return ApiResponse.success(orderDetailService.create(reqDTO));
    }

    @PostMapping("{id}")
    public ApiResponse<OrderDetailResDTO> update(@PathVariable Long id, @Valid @RequestBody OrderDetailReqDTO reqDTO) {
        return ApiResponse.success(orderDetailService.update(id, reqDTO));
    }

    @DeleteMapping("{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        orderDetailService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<PageResponse<OrderDetailResDTO>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<OrderDetailResDTO> data = orderDetailService.findAll(pageable);
        return ApiResponse.success(new PageResponse<>(data));
    }
}
