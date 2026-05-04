package com.comission.system.service;

import com.comission.system.dto.request.commissionTransaction.CommissionTransactionReqDTO;
import com.comission.system.dto.response.commissionTransaction.CommissionTransactionResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommissionTransactionService {
    CommissionTransactionResDTO create(CommissionTransactionReqDTO reqDTO);
    CommissionTransactionResDTO update(Long id, CommissionTransactionReqDTO reqDTO);
    void delete(Long id);
    Page<CommissionTransactionResDTO> findAll(Pageable pageable);
}
