package com.comission.system.service.impl;

import com.comission.system.dto.request.commissionPolicy.CommissionPolicyReqDTO;
import com.comission.system.dto.response.commissionPolicy.CommissionPolicyResDTO;
import com.comission.system.entity.CommissionPolicy;
import com.comission.system.exception.BusinessException;
import com.comission.system.exception.ErrorCode;
import com.comission.system.exception.NotFoundException;
import com.comission.system.mapper.CommissionPolicyMapper;
import com.comission.system.repository.CommissionPolicyRepository;
import com.comission.system.service.CommissionPolicyService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CommissionPolicyServiceImpl implements CommissionPolicyService {
    private final CommissionPolicyRepository commissionPolicyRepository;
    private static final Logger logger = LoggerFactory.getLogger(CommissionPolicyServiceImpl.class);
    private final CommissionPolicyMapper commissionPolicyMapper;

    @Override
    public CommissionPolicyResDTO create(CommissionPolicyReqDTO commissionPolicyReqDTO) {
        logger.info("Create policy for product id: {}", commissionPolicyReqDTO.getProductId());
        validateRates(commissionPolicyReqDTO);
        if (commissionPolicyRepository.existsByProduct_Id(commissionPolicyReqDTO.getProductId())) {
            throw new BusinessException(ErrorCode.POLICY_001);
        }
        CommissionPolicy commissionPolicy = commissionPolicyMapper.toEntity(commissionPolicyReqDTO);
        commissionPolicy.setCreateAt(Instant.now());
        commissionPolicy.setUpdateAt(Instant.now());
        commissionPolicyRepository.save(commissionPolicy);
        return commissionPolicyMapper.toResponse(commissionPolicy);
    }

    @Override
    public CommissionPolicyResDTO update(Long id, CommissionPolicyReqDTO commissionPolicyReqDTO) {
        logger.info("Update policy id: {}", id);
        validateRates(commissionPolicyReqDTO);
        CommissionPolicy commissionPolicy = commissionPolicyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POLICY_002));
        commissionPolicyMapper.updateCommissionPolicyFromReq(commissionPolicyReqDTO, commissionPolicy);
        commissionPolicy.setUpdateAt(Instant.now());
        commissionPolicyRepository.save(commissionPolicy);
        return commissionPolicyMapper.toResponse(commissionPolicy);
    }

    @Override
    public void delete(Long id) {
        logger.info("Delete policy id: {}", id);
        if (commissionPolicyRepository.existsById(id)) {
            commissionPolicyRepository.deleteById(id);
        } else {
            logger.info("Policy id not found: {}", id);
            throw new NotFoundException(ErrorCode.POLICY_002);
        }
    }

    @Override
    public Page<CommissionPolicyResDTO> findAll(Pageable pageable) {
        return commissionPolicyRepository.findAll(pageable).map(commissionPolicyMapper::toResponse);
    }

    private void validateRates(CommissionPolicyReqDTO req) {
        BigDecimal total = req.getCompanyRate().add(req.getParentRate()).add(req.getChildRate());
        if (total.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new BusinessException(ErrorCode.POLICY_001);
        }
    }
}