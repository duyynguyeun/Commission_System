package com.comission.system.service.impl;

import com.comission.system.dto.request.customerOrder.CustomerOrderReqDTO;
import com.comission.system.dto.response.customerOrder.CustomerOrderResDTO;
import com.comission.system.entity.CommissionPolicy;
import com.comission.system.entity.CommissionTransaction;
import com.comission.system.entity.CustomerOrder;
import com.comission.system.entity.OrderDetail;
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

    private void generateCommissions(Long orderId) {
        List<OrderDetail> details = orderDetailRepository.findByCustomerOrder_Id(orderId);
        for (OrderDetail detail : details) {
            if (detail.getProduct() == null) {
                continue;
            }

            CommissionPolicy policy = commissionPolicyRepository.findFirstByProduct_Id(detail.getProduct().getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.POLICY_002));

            BigDecimal gross = detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
            createCommissionIfNeeded(detail, policy, gross, EmployeeEnum.SALE_CHILD);
            createCommissionIfNeeded(detail, policy, gross, EmployeeEnum.SALE_PARENT);
        }
    }

    private void createCommissionIfNeeded(OrderDetail detail, CommissionPolicy policy, BigDecimal gross, EmployeeEnum role) {
        if (commissionTransactionRepository.existsByOrderDetail_IdAndCommissionRole(detail.getId(), role)) {
            return;
        }

        if (role == EmployeeEnum.SALE_CHILD && detail.getSeller() != null) {
            commissionTransactionRepository.save(CommissionTransaction.builder()
                    .employee(detail.getSeller())
                    .orderDetail(detail)
                    .commissionPolicy(policy)
                    .commissionRole(role)
                    .commissionRate(policy.getChildRate())
                    .commissionAmount(calcAmount(gross, policy.getChildRate()))
                    .status(CommissionEnum.PENDING)
                    .createAt(Instant.now())
                    .build());
        }

        if (role == EmployeeEnum.SALE_PARENT && detail.getParent() != null) {
            commissionTransactionRepository.save(CommissionTransaction.builder()
                    .employee(detail.getParent())
                    .orderDetail(detail)
                    .commissionPolicy(policy)
                    .commissionRole(role)
                    .commissionRate(policy.getParentRate())
                    .commissionAmount(calcAmount(gross, policy.getParentRate()))
                    .status(CommissionEnum.PENDING)
                    .createAt(Instant.now())
                    .build());
        }
    }

    private BigDecimal calcAmount(BigDecimal gross, BigDecimal rate) {
        if (rate == null) {
            return BigDecimal.ZERO;
        }
        return gross.multiply(rate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
