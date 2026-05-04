package com.comission.system.controller;

import com.comission.system.dto.request.commissionTransaction.CommissionTransactionReqDTO;
import com.comission.system.dto.response.ApiResponse;
import com.comission.system.dto.response.commissionTransaction.CommissionTransactionResDTO;
import com.comission.system.dto.response.page.PageResponse;
import com.comission.system.service.CommissionTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commission-transactions")
@RequiredArgsConstructor
public class CommissionTransactionController {
    private final CommissionTransactionService commissionTransactionService;

    @PostMapping
    public ApiResponse<CommissionTransactionResDTO> create(@Valid @RequestBody CommissionTransactionReqDTO reqDTO) {
        return ApiResponse.success(commissionTransactionService.create(reqDTO));
    }

    @PostMapping("{id}")
    public ApiResponse<CommissionTransactionResDTO> update(@PathVariable Long id, @Valid @RequestBody CommissionTransactionReqDTO reqDTO) {
        return ApiResponse.success(commissionTransactionService.update(id, reqDTO));
    }

    @DeleteMapping("{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        commissionTransactionService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<PageResponse<CommissionTransactionResDTO>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<CommissionTransactionResDTO> data = commissionTransactionService.findAll(pageable);
        return ApiResponse.success(new PageResponse<>(data));
    }
}
