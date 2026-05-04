package com.comission.system.service;

import com.comission.system.dto.request.commissionPolicy.CommissionPolicyReqDTO;
import com.comission.system.dto.response.commissionPolicy.CommissionPolicyResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommissionPolicyService {
    CommissionPolicyResDTO create(CommissionPolicyReqDTO commissionPolicyReqDTO);
    CommissionPolicyResDTO update(Long id, CommissionPolicyReqDTO commissionPolicyReqDTO);
    void delete(Long id);
    Page<CommissionPolicyResDTO> findAll(Pageable pageable);
}