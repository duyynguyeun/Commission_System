package com.comission.system.service;

import com.comission.system.dto.request.product.ProductReqDTO;
import com.comission.system.dto.response.product.ProductResDTO;
import com.comission.system.dto.response.product.SaleProductResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResDTO create(ProductReqDTO productReqDTO);
    ProductResDTO update(Long id, ProductReqDTO productReqDTO);
    void delete(Long id);
    Page<ProductResDTO> findAll(Pageable pageable);
    Page<SaleProductResDTO> findAllForSale(Pageable pageable);
}