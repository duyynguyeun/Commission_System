package com.comission.system.service.impl;

import com.comission.system.dto.request.product.ProductReqDTO;
import com.comission.system.dto.response.product.ProductResDTO;
import com.comission.system.dto.response.product.SaleProductResDTO;
import com.comission.system.entity.CommissionPolicy;
import com.comission.system.entity.Product;
import com.comission.system.exception.BusinessException;
import com.comission.system.exception.ErrorCode;
import com.comission.system.exception.NotFoundException;
import com.comission.system.mapper.ProductMapper;
import com.comission.system.repository.AffiliateLinkRepository;
import com.comission.system.repository.CommissionPolicyRepository;
import com.comission.system.repository.OrderDetailRepository;
import com.comission.system.repository.ProductRepository;
import com.comission.system.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CommissionPolicyRepository commissionPolicyRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AffiliateLinkRepository affiliateLinkRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductResDTO create(ProductReqDTO productReqDTO) {
        logger.info("Tạo sản phẩm với tên là: {}", productReqDTO.getName());
        if(productRepository.existsByName(productReqDTO.getName())) {
            throw new BusinessException(ErrorCode.PRODUCT_002);
        }
        Product product = productMapper.toEntity(productReqDTO);
        product.setCreateAt(Instant.now());
        product.setUpdateAt(Instant.now());
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResDTO update(Long id, ProductReqDTO productReqDTO) {
        logger.info("Cập nhật sản phẩm có id là: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_001));
        productMapper.updateProductFromReq(productReqDTO, product);
        product.setUpdateAt(Instant.now());
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    public void delete(Long id) {
        logger.info("Xóa sản phẩm có id là: {}", id);
        if (!productRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.PRODUCT_001);
        }

        if (orderDetailRepository.existsByProduct_Id(id) || affiliateLinkRepository.existsByProduct_Id(id)) {
            throw new BusinessException(ErrorCode.PRODUCT_004);
        }

        commissionPolicyRepository.findFirstByProduct_Id(id).ifPresent(p -> commissionPolicyRepository.deleteById(p.getId()));

        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductResDTO> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<SaleProductResDTO> findAllForSale(Pageable pageable) {
        return productRepository.findAll(pageable).map(product -> {
            CommissionPolicy policy = commissionPolicyRepository.findFirstByProduct_Id(product.getId()).orElse(null);
            BigDecimal parentRate = policy != null ? policy.getParentRate() : BigDecimal.ZERO;
            BigDecimal childRate = policy != null ? policy.getChildRate() : BigDecimal.ZERO;
            BigDecimal maxRate = parentRate.add(childRate);
            BigDecimal maxAmount = product.getPrice().multiply(maxRate).divide(BigDecimal.valueOf(100));

            return SaleProductResDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .maxCommissionRate(maxRate)
                    .maxCommissionAmount(maxAmount)
                    .urlImage(product.getUrlImage())
                    .build();
        });
    }
}
