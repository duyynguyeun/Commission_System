package com.comission.system.service.impl;

import com.comission.system.dto.request.orderDetail.OrderDetailReqDTO;
import com.comission.system.dto.response.orderDetail.OrderDetailResDTO;
import com.comission.system.entity.*;
import com.comission.system.enums.EmployeeEnum;
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

    // Tính số lượng hàng còn lại
    private void deductStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_001));

        if (product.getStockQuantity() < quantity) {
            throw new BusinessException(ErrorCode.PRODUCT_003);
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }

    // Xác định thêm thông tin về đơn hàng (thuộc afflink nào? thuộc seller nào? có parent hay không?)
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

    // Lấy AffLink từ request
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

    // Kiểm tra quan hệ giữa Sale_Parent và Sale_Child
    private void validateHierarchy(OrderDetail orderDetail) {
        if (orderDetail.getParent() != null
                && orderDetail.getSeller() != null
                && orderDetail.getSeller().getParentId() != null
                && !orderDetail.getSeller().getParentId().equals(orderDetail.getParent().getId())) {
            throw new BusinessException(ErrorCode.EMPLOYEE_001);
        }
    }

    // Tính hoa hồng mà sale nhận được tùy theo case
    private void calculateAndSaveCommissions(OrderDetail orderDetail) {
        if (orderDetail.getSeller() == null) return;

        CommissionPolicy policy = commissionPolicyRepository.findFirstByProduct_Id(orderDetail.getProduct().getId())
                .orElse(null);
        if (policy == null) return;

        BigDecimal totalPrice = orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity()));
        Employee seller = orderDetail.getSeller();
        EmployeeEnum sellerRole = seller.getAccount() != null ? seller.getAccount().getRole() : null;

        if (sellerRole == EmployeeEnum.SALE_CHILD) {
            // --- Case 1: Affiliate link belongs to a SALE_CHILD ---
            // Child gets childRate
            if (policy.getChildRate() != null && policy.getChildRate().compareTo(BigDecimal.ZERO) > 0) {
                if (!commissionTransactionRepository.existsByOrderDetail_IdAndCommissionRole(orderDetail.getId(), EmployeeEnum.SALE_CHILD)) {
                    BigDecimal childAmount = totalPrice.multiply(policy.getChildRate()).divide(BigDecimal.valueOf(100));
                    saveTransaction(seller, orderDetail, childAmount, policy.getChildRate(), EmployeeEnum.SALE_CHILD, policy);
                }
            }
            // Parent gets parentRate
            if (orderDetail.getParent() != null && policy.getParentRate() != null && policy.getParentRate().compareTo(BigDecimal.ZERO) > 0) {
                if (!commissionTransactionRepository.existsByOrderDetail_IdAndCommissionRole(orderDetail.getId(), EmployeeEnum.SALE_PARENT)) {
                    BigDecimal parentAmount = totalPrice.multiply(policy.getParentRate()).divide(BigDecimal.valueOf(100));
                    saveTransaction(orderDetail.getParent(), orderDetail, parentAmount, policy.getParentRate(), EmployeeEnum.SALE_PARENT, policy);
                }
            }
        } else if (sellerRole == EmployeeEnum.SALE_PARENT) {
            // --- Case 2: Affiliate link belongs to a SALE_PARENT ---
            // Parent gets parentRate + childRate (total sale commission)
            BigDecimal parentRate = policy.getParentRate() != null ? policy.getParentRate() : BigDecimal.ZERO;
            BigDecimal childRate = policy.getChildRate() != null ? policy.getChildRate() : BigDecimal.ZERO;
            BigDecimal combinedRate = parentRate.add(childRate);

            if (combinedRate.compareTo(BigDecimal.ZERO) > 0) {
                if (!commissionTransactionRepository.existsByOrderDetail_IdAndCommissionRole(orderDetail.getId(), EmployeeEnum.SALE_PARENT)) {
                    BigDecimal combinedAmount = totalPrice.multiply(combinedRate).divide(BigDecimal.valueOf(100));
                    saveTransaction(seller, orderDetail, combinedAmount, combinedRate, EmployeeEnum.SALE_PARENT, policy);
                }
            }
        }
    }

    private void saveTransaction(Employee employee, OrderDetail orderDetail, BigDecimal amount, BigDecimal rate, EmployeeEnum role, CommissionPolicy policy) {
        CommissionTransaction transaction = CommissionTransaction.builder()
                .employee(employee)
                .orderDetail(orderDetail)
                .commissionPolicy(policy)
                .commissionAmount(amount)
                .commissionRate(rate)
                .commissionRole(role)
                .status(com.comission.system.enums.CommissionEnum.PENDING)
                .createAt(Instant.now())
                .build();
        commissionTransactionRepository.save(transaction);
    }
}

