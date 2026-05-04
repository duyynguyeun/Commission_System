package com.comission.system.service.impl;

import com.comission.system.dto.request.commissionTransaction.CommissionTransactionReqDTO;
import com.comission.system.dto.response.commissionTransaction.CommissionTransactionResDTO;
import com.comission.system.entity.CommissionTransaction;
import com.comission.system.exception.BusinessException;
import com.comission.system.exception.ErrorCode;
import com.comission.system.mapper.CommissionTransactionMapper;
import com.comission.system.repository.CommissionTransactionRepository;
import com.comission.system.service.CommissionTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class CommissionTransactionServiceImpl implements CommissionTransactionService {
    private final CommissionTransactionRepository commissionTransactionRepository;
    private final CommissionTransactionMapper commissionTransactionMapper;

    @Override
    public CommissionTransactionResDTO create(CommissionTransactionReqDTO reqDTO) {
        CommissionTransaction commissionTransaction = commissionTransactionMapper.toEntity(reqDTO);
        commissionTransaction.setCreateAt(Instant.now());
        commissionTransactionRepository.save(commissionTransaction);
        return commissionTransactionMapper.toResponse(commissionTransaction);
    }

    @Override
    public CommissionTransactionResDTO update(Long id, CommissionTransactionReqDTO reqDTO) {
        CommissionTransaction commissionTransaction = commissionTransactionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRANSACTION_001));
        commissionTransactionMapper.updateFromReq(reqDTO, commissionTransaction);
        commissionTransactionRepository.save(commissionTransaction);
        return commissionTransactionMapper.toResponse(commissionTransaction);
    }

    @Override
    public void delete(Long id) {
        if (!commissionTransactionRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.TRANSACTION_001);
        }
        commissionTransactionRepository.deleteById(id);
    }

    @Override
    public Page<CommissionTransactionResDTO> findAll(Pageable pageable) {
        return commissionTransactionRepository.findAll(pageable).map(commissionTransactionMapper::toResponse);
    }
}
