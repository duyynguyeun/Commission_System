package com.comission.system.controller;

import com.comission.system.dto.request.product.ProductReqDTO;
import com.comission.system.dto.response.ApiResponse;
import com.comission.system.dto.response.page.PageResponse;
import com.comission.system.dto.response.product.ProductResDTO;
import com.comission.system.dto.response.product.SaleProductResDTO;
import com.comission.system.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ApiResponse<ProductResDTO> create(@Valid @RequestBody ProductReqDTO productReqDTO) {
        return ApiResponse.success(productService.create(productReqDTO));
    }

    @PostMapping("{id}")
    public ApiResponse<ProductResDTO> update(@Valid @PathVariable Long id, @RequestBody ProductReqDTO productReqDTO) {
        return ApiResponse.success(productService.update(id, productReqDTO));
    }

    @DeleteMapping("{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<PageResponse<ProductResDTO>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<ProductResDTO> products = productService.findAll(pageable);
        return ApiResponse.success(new PageResponse<>(products));
    }

    @GetMapping("/sale")
    public ApiResponse<PageResponse<SaleProductResDTO>> findAllForSale(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<SaleProductResDTO> products = productService.findAllForSale(pageable);
        return ApiResponse.success(new PageResponse<>(products));
    }
}
