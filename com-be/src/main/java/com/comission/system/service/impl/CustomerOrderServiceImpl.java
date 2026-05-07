package com.comission.system.service.impl;

import com.comission.system.dto.request.customerOrder.CustomerOrderReqDTO;
import com.comission.system.dto.response.customerOrder.CustomerOrderResDTO;
import com.comission.system.entity.*;
import com.comission.system.enums.CommissionEnum;
import com.comission.system.enums.EmployeeEnum;
import com.comission.system.enums.OrderEnum;
import com.comission.system.exception.BusinessException;
import com.comission.system.exception.ErrorCode;
import com.comission.system.mapper.CustomerOrderMapper;
import com.comission.system.repository.CommissionPolicyRepository;
import com.comission.system.repository.CommissionTransactionRepository;
import com.comission.system.repository.CustomerOrderRepository;
import com.comission.system.repository.OrderDetailRepository;
import com.comission.system.service.CustomerOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerOrderServiceImpl implements CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CommissionPolicyRepository commissionPolicyRepository;
    private final CommissionTransactionRepository commissionTransactionRepository;
    private final CustomerOrderMapper customerOrderMapper;

    @Override
    public CustomerOrderResDTO create(CustomerOrderReqDTO reqDTO) {
        CustomerOrder customerOrder = customerOrderMapper.toEntity(reqDTO);
        customerOrder.setCreateAt(Instant.now());
        customerOrder.setUpdateAt(Instant.now());
        customerOrderRepository.save(customerOrder);
        return customerOrderMapper.toResponse(customerOrder);
    }

    @Override
    public CustomerOrderResDTO update(Long id, CustomerOrderReqDTO reqDTO) {
        CustomerOrder customerOrder = customerOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_002));

        OrderEnum oldStatus = customerOrder.getStatus();
        customerOrderMapper.updateFromReq(reqDTO, customerOrder);
        customerOrder.setUpdateAt(Instant.now());
        customerOrderRepository.save(customerOrder);

        if (customerOrder.getStatus() == OrderEnum.COMPLETED && oldStatus != OrderEnum.COMPLETED) {
            generateCommissions(customerOrder.getId());
        }

        return customerOrderMapper.toResponse(customerOrder);
    }

    @Override
    public void delete(Long id) {
        if (!customerOrderRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.ORDER_002);
        }
        customerOrderRepository.deleteById(id);
    }

    @Override
    public Page<CustomerOrderResDTO> findAll(Pageable pageable) {
        return customerOrderRepository.findAll(pageable).map(customerOrderMapper::toResponse);
    }

    // Tạo hoa hồng cho toàn bộ các OrderDetail thuộc CustomerOder
    private void generateCommissions(Long orderId) {
        List<OrderDetail> details = orderDetailRepository.findByCustomerOrder_Id(orderId);
        for (OrderDetail detail : details) {
            if (detail.getProduct() == null || detail.getSeller() == null) {
                continue;
            }

            CommissionPolicy policy = commissionPolicyRepository.findFirstByProduct_Id(detail.getProduct().getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.POLICY_002));

            BigDecimal gross = detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));

            // Lấy role của người bán
            Employee seller = detail.getSeller();
            EmployeeEnum sellerRole = (seller.getAccount() != null) ? seller.getAccount().getRole() : null;

            if (sellerRole == EmployeeEnum.SALE_CHILD) {
                // Case 1: Sale Child bán -> Child nhận childRate, Parent nhận parentRate
                createCommissionIfNeeded(seller, detail, policy, gross, policy.getChildRate(), EmployeeEnum.SALE_CHILD);

                if (detail.getParent() != null) {
                    createCommissionIfNeeded(detail.getParent(), detail, policy, gross, policy.getParentRate(), EmployeeEnum.SALE_PARENT);
                }
            } else if (sellerRole == EmployeeEnum.SALE_PARENT) {
                // Case 2: Sale Parent bán -> Parent nhận gộp (childRate + parentRate)
                BigDecimal childRate = (policy.getChildRate() != null) ? policy.getChildRate() : BigDecimal.ZERO;
                BigDecimal parentRate = (policy.getParentRate() != null) ? policy.getParentRate() : BigDecimal.ZERO;
                BigDecimal combinedRate = childRate.add(parentRate);

                createCommissionIfNeeded(seller, detail, policy, gross, combinedRate, EmployeeEnum.SALE_PARENT);
            }
        }
    }

    // Kiểm tra xem sale đã được tính hoa hồng cho đơn hàng này hay chưa
    private void createCommissionIfNeeded(Employee employee, OrderDetail detail, CommissionPolicy policy, BigDecimal gross, BigDecimal rate, EmployeeEnum role) {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        if (commissionTransactionRepository.existsByOrderDetail_IdAndCommissionRole(detail.getId(), role)) {
            return;
        }

        commissionTransactionRepository.save(CommissionTransaction.builder()
                .employee(employee)
                .orderDetail(detail)
                .commissionPolicy(policy)
                .commissionRole(role)
                .commissionRate(rate)
                .commissionAmount(calcAmount(gross, rate))
                .status(CommissionEnum.PENDING)
                .createAt(Instant.now())
                .build());
    }

    // Tính làm tròn
    private BigDecimal calcAmount(BigDecimal gross, BigDecimal rate) {
        if (rate == null) {
            return BigDecimal.ZERO;
        }
        return gross.multiply(rate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
