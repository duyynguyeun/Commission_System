package com.comission.system.service.impl;

import com.comission.system.dto.request.affiliateLink.AffiliateLinkRenderReqDTO;
import com.comission.system.dto.request.affiliateLink.AffiliateLinkReqDTO;
import com.comission.system.dto.response.affiliateLink.AffiliateLinkResDTO;
import com.comission.system.entity.AffiliateLink;
import com.comission.system.entity.Employee;
import com.comission.system.entity.Product;
import com.comission.system.exception.BusinessException;
import com.comission.system.exception.ErrorCode;
import com.comission.system.mapper.AffiliateLinkMapper;
import com.comission.system.repository.AffiliateLinkRepository;
import com.comission.system.repository.EmployeeRepository;
import com.comission.system.repository.ProductRepository;
import com.comission.system.service.AffiliateLinkService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AffiliateLinkServiceImpl implements AffiliateLinkService {
    private static final String BUY_PATH = "/buy?aff=";

    private final AffiliateLinkRepository affiliateLinkRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final AffiliateLinkMapper affiliateLinkMapper;

    @Override
    public AffiliateLinkResDTO create(AffiliateLinkReqDTO reqDTO) {
        if (affiliateLinkRepository.existsByAffCode(reqDTO.getAffCode())) {
            throw new BusinessException(ErrorCode.LINK_002);
        }
        AffiliateLink affiliateLink = affiliateLinkMapper.toEntity(reqDTO);
        affiliateLink.setCreateAt(Instant.now());
        affiliateLink.setUpdateAt(Instant.now());
        affiliateLinkRepository.save(affiliateLink);
        return affiliateLinkMapper.toResponse(affiliateLink);
    }

    @Override
    public AffiliateLinkResDTO update(Long id, AffiliateLinkReqDTO reqDTO) {
        AffiliateLink affiliateLink = affiliateLinkRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.LINK_003));
        affiliateLinkMapper.updateFromReq(reqDTO, affiliateLink);
        affiliateLink.setUpdateAt(Instant.now());
        affiliateLinkRepository.save(affiliateLink);
        return affiliateLinkMapper.toResponse(affiliateLink);
    }

    @Override
    public AffiliateLinkResDTO trackByCode(String affCode) {
        AffiliateLink affiliateLink = affiliateLinkRepository.findByAffCode(affCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.LINK_003));
        return affiliateLinkMapper.toResponse(affiliateLink);
    }

    @Override
    public AffiliateLinkResDTO renderForSale(Long saleId, AffiliateLinkRenderReqDTO reqDTO) {
        Employee sale = employeeRepository.findById(saleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_001));
        Product product = productRepository.findById(reqDTO.getProductId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_001));

        AffiliateLink existing = affiliateLinkRepository
                .findByEmployee_IdAndProduct_Id(saleId, reqDTO.getProductId())
                .orElse(null);
        if (existing != null) {
            return affiliateLinkMapper.toResponse(existing);
        }

        String affCode = generateAffCode();
        AffiliateLink link = AffiliateLink.builder()
                .affCode(affCode)
                .affUrl(BUY_PATH + affCode)
                .employee(sale)
                .product(product)
                .createAt(Instant.now())
                .updateAt(Instant.now())
                .build();

        affiliateLinkRepository.save(link);
        return affiliateLinkMapper.toResponse(link);
    }

    @Override
    public List<AffiliateLinkResDTO> findMyLinks(Long saleId) {
        return affiliateLinkRepository.findByEmployee_Id(saleId)
                .stream()
                .map(affiliateLinkMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!affiliateLinkRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.LINK_003);
        }
        affiliateLinkRepository.deleteById(id);
    }

    @Override
    public Page<AffiliateLinkResDTO> findAll(Pageable pageable) {
        return affiliateLinkRepository.findAll(pageable).map(affiliateLinkMapper::toResponse);
    }

    private String generateAffCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        } while (affiliateLinkRepository.existsByAffCode(code));
        return code;
    }
}
