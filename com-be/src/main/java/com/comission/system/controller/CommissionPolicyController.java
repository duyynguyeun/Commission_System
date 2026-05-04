package com.comission.system.controller;

import com.comission.system.dto.request.commissionPolicy.CommissionPolicyReqDTO;
import com.comission.system.dto.response.ApiResponse;
import com.comission.system.dto.response.commissionPolicy.CommissionPolicyResDTO;
import com.comission.system.dto.response.page.PageResponse;
import com.comission.system.entity.CommissionPolicy;
import com.comission.system.service.CommissionPolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policy")
@RequiredArgsConstructor
public class CommissionPolicyController {
    private final CommissionPolicyService commissionPolicyService;

    @PostMapping
    public ApiResponse<CommissionPolicyResDTO> create(@Valid @RequestBody CommissionPolicyReqDTO commissionPolicyReqDTO) {
        return ApiResponse.success(commissionPolicyService.create(commissionPolicyReqDTO));
    }

    @PostMapping("{id}")
    public ApiResponse<CommissionPolicyResDTO> update(@Valid @PathVariable Long id, @RequestBody CommissionPolicyReqDTO commissionPolicyReqDTO) {
        return ApiResponse.success(commissionPolicyService.update(id, commissionPolicyReqDTO));
    }

    @DeleteMapping("{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        commissionPolicyService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<PageResponse<CommissionPolicyResDTO>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<CommissionPolicyResDTO> policy = commissionPolicyService.findAll(pageable);
        return ApiResponse.success(new PageResponse<>(policy));
    }
}
