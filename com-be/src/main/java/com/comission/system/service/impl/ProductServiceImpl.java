package com.comission.system.service.impl;

import com.comission.system.dto.request.product.ProductReqDTO;
import com.comission.system.dto.response.product.ProductResDTO;
import com.comission.system.entity.Product;
import com.comission.system.exception.BusinessException;
import com.comission.system.exception.ErrorCode;
import com.comission.system.exception.NotFoundException;
import com.comission.system.mapper.ProductMapper;
import com.comission.system.repository.ProductRepository;
import com.comission.system.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductResDTO create(ProductReqDTO productReqDTO) {
        logger.info("Create Product for name: {}", productReqDTO.getName());
        if(productRepository.existsByName(productReqDTO.getName())) {
            throw new BusinessException(ErrorCode.PRODUCT_002);
        }
        Product product = productMapper.toEntity(productReqDTO);
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResDTO update(Long id, ProductReqDTO productReqDTO) {
        logger.info("Update Product for id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_001));
        productMapper.updateProductFromReq(productReqDTO, product);
        product.setUpdatedAt(Instant.now());
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    public void delete(Long id) {
        logger.info("Delete Product for id: {}", id);
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            logger.info("Product not found for id: {}", id);
            throw new BusinessException(ErrorCode.PRODUCT_001);
        }
    }

    @Override
    public Page<ProductResDTO> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }
}
