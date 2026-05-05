package com.comission.system.service.impl;

import com.comission.system.dto.request.orderDetail.OrderDetailReqDTO;
import com.comission.system.dto.response.orderDetail.OrderDetailResDTO;
import com.comission.system.entity.*;
import com.comission.system.exception.BusinessException;
import com.comission.system.exception.ErrorCode;
import com.comission.system.mapper.OrderDetailMapper;
import com.comission.system.repository.*;
import com.comission.system.service.OrderDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final AffiliateLinkRepository affiliateLinkRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final ProductRepository productRepository;
    private final CommissionPolicyRepository commissionPolicyRepository;
    private final CommissionTransactionRepository commissionTransactionRepository;

    @Override
    public OrderDetailResDTO create(OrderDetailReqDTO reqDTO) {
        OrderDetail orderDetail = orderDetailMapper.toEntity(reqDTO);
        
        // Trừ tồn kho
        deductStock(orderDetail.getProduct().getId(), orderDetail.getQuantity());

        applyAffiliateHierarchy(orderDetail, reqDTO);
        validateHierarchy(orderDetail);
        OrderDetail saved = orderDetailRepository.save(orderDetail);
        
        calculateAndSaveCommissions(saved);
        
        return orderDetailMapper.toResponse(saved);
    }

    private void deductStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_001));
        
        if (product.getStockQuantity() < quantity) {
            throw new BusinessException(ErrorCode.PRODUCT_003);
        }
        
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }

    @Override
    public OrderDetailResDTO update(Long id, OrderDetailReqDTO reqDTO) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_002));
        orderDetailMapper.updateFromReq(reqDTO, orderDetail);
        applyAffiliateHierarchy(orderDetail, reqDTO);
        validateHierarchy(orderDetail);
        OrderDetail saved = orderDetailRepository.save(orderDetail);
        return orderDetailMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!orderDetailRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.ORDER_002);
        }
        orderDetailRepository.deleteById(id);
    }

    @Override
    public Page<OrderDetailResDTO> findAll(Pageable pageable) {
        return orderDetailRepository.findAll(pageable).map(orderDetailMapper::toResponse);
    }

    private void applyAffiliateHierarchy(OrderDetail orderDetail, OrderDetailReqDTO reqDTO) {
        AffiliateLink affiliateLink = resolveAffiliateLink(reqDTO);
        
        // Luôn set để ghi đè các object stub có thể được tạo bởi Mapper
        orderDetail.setAffiliateLink(affiliateLink);
        
        if (affiliateLink == null) {
            orderDetail.setSeller(null);
            orderDetail.setParent(null);
            return;
        }

        Employee seller = affiliateLink.getEmployee();
        orderDetail.setSeller(seller);

        if (seller != null && seller.getParentId() != null) {
            Employee parent = employeeRepository.findById(seller.getParentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_001));
            orderDetail.setParent(parent);
        } else {
            orderDetail.setParent(null);
        }
    }

    private AffiliateLink resolveAffiliateLink(OrderDetailReqDTO reqDTO) {
        if (reqDTO.getAffCode() != null && !reqDTO.getAffCode().isBlank()) {
            return affiliateLinkRepository.findByAffCode(reqDTO.getAffCode())
                    .orElseThrow(() -> new BusinessException(ErrorCode.LINK_003));
        }

        if (reqDTO.getAffiliateLinkId() != null) {
            return affiliateLinkRepository.findById(reqDTO.getAffiliateLinkId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.LINK_003));
        }

        return null;
    }

    private void validateHierarchy(OrderDetail orderDetail) {
        if (orderDetail.getParent() != null
                && orderDetail.getSeller() != null
                && orderDetail.getSeller().getParentId() != null
                && !orderDetail.getSeller().getParentId().equals(orderDetail.getParent().getId())) {
            throw new BusinessException(ErrorCode.EMPLOYEE_001);
        }
    }

    private void calculateAndSaveCommissions(OrderDetail orderDetail) {
        if (orderDetail.getSeller() == null) return;

        CommissionPolicy policy = commissionPolicyRepository.findFirstByProduct_Id(orderDetail.getProduct().getId())
                .orElse(null);
        if (policy == null) return;

        BigDecimal totalPrice = orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity()));

        // 1. Commission for Seller (Child rate)
        if (policy.getChildRate() != null && policy.getChildRate().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal sellerAmount = totalPrice.multiply(policy.getChildRate()).divide(BigDecimal.valueOf(100));
            saveTransaction(orderDetail.getSeller(), orderDetail, sellerAmount, policy.getChildRate());
        }

        // 2. Commission for Parent (Parent rate)
        if (orderDetail.getParent() != null && policy.getParentRate() != null && policy.getParentRate().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal parentAmount = totalPrice.multiply(policy.getParentRate()).divide(BigDecimal.valueOf(100));
            saveTransaction(orderDetail.getParent(), orderDetail, parentAmount, policy.getParentRate());
        }
    }

    private void saveTransaction(Employee employee, OrderDetail orderDetail, BigDecimal amount, BigDecimal rate) {
        CommissionTransaction transaction = CommissionTransaction.builder()
                .employee(employee)
                .orderDetail(orderDetail)
                .commissionAmount(amount)
                .commissionRate(rate)
                .createAt(Instant.now())
                .build();
        commissionTransactionRepository.save(transaction);
    }
}
