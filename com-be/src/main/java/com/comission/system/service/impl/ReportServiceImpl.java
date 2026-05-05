package com.comission.system.service.impl;

import com.comission.system.dto.response.report.AdminEmployeeRevenueResDTO;
import com.comission.system.dto.response.report.AdminProductRevenueResDTO;
import com.comission.system.dto.response.report.SaleHistoryResDTO;
import com.comission.system.dto.response.report.SaleOverviewResDTO;
import com.comission.system.entity.CommissionTransaction;
import com.comission.system.entity.Employee;
import com.comission.system.entity.OrderDetail;
import com.comission.system.entity.Product;
import com.comission.system.enums.EmployeeEnum;
import com.comission.system.exception.BusinessException;
import com.comission.system.exception.ErrorCode;
import com.comission.system.repository.CommissionTransactionRepository;
import com.comission.system.repository.EmployeeRepository;
import com.comission.system.repository.OrderDetailRepository;
import com.comission.system.repository.ProductRepository;
import com.comission.system.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {
    private final EmployeeRepository employeeRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CommissionTransactionRepository commissionTransactionRepository;
    private final ProductRepository productRepository;

    @Override
    public List<SaleHistoryResDTO> getSaleHistory(Long employeeId) {
        ensureEmployeeExists(employeeId);
        List<CommissionTransaction> txs = commissionTransactionRepository.findByEmployee_Id(employeeId);
        return txs.stream().map(tx -> SaleHistoryResDTO.builder()
                        .orderDetailId(tx.getOrderDetail().getId())
                        .productId(tx.getOrderDetail().getProduct().getId())
                        .productName(tx.getOrderDetail().getProduct().getName())
                        .quantity(tx.getOrderDetail().getQuantity())
                        .lineRevenue(lineRevenue(tx.getOrderDetail()))
                        .commissionRole(tx.getCommissionRole())
                        .commissionRate(tx.getCommissionRate())
                        .commissionAmount(tx.getCommissionAmount())
                        .transactionAt(tx.getCreateAt())
                        .build())
                .toList();
    }

    @Override
    public SaleOverviewResDTO getSaleOverview(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_001));

        List<OrderDetail> ownOrderDetails = orderDetailRepository.findBySeller_IdOrParent_Id(employeeId, employeeId);
        Set<Long> ownIds = ownOrderDetails.stream().map(OrderDetail::getId).collect(Collectors.toSet());
        List<CommissionTransaction> ownTx = ownIds.isEmpty()
                ? List.of()
                : commissionTransactionRepository.findByOrderDetail_IdIn(new ArrayList<>(ownIds));

        BigDecimal ownRevenue = ownOrderDetails.stream()
                .filter(o -> o.getSeller() != null && employeeId.equals(o.getSeller().getId()))
                .map(this::lineRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ownCommission = ownTx.stream()
                .filter(tx -> tx.getEmployee() != null && employeeId.equals(tx.getEmployee().getId()))
                .map(CommissionTransaction::getCommissionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Long> childIds = employeeRepository.findByParentId(employeeId).stream().map(Employee::getId).toList();
        BigDecimal relatedLevelRevenue = BigDecimal.ZERO;
        BigDecimal relatedLevelCommission = BigDecimal.ZERO;

        if (!childIds.isEmpty()) {
            List<OrderDetail> childOrderDetails = childIds.stream()
                    .flatMap(id -> orderDetailRepository.findBySeller_IdOrParent_Id(id, id).stream())
                    .toList();
            relatedLevelRevenue = childOrderDetails.stream()
                    .filter(o -> o.getSeller() != null && childIds.contains(o.getSeller().getId()))
                    .map(this::lineRevenue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Set<Long> childOrderIds = childOrderDetails.stream().map(OrderDetail::getId).collect(Collectors.toSet());
            List<CommissionTransaction> childTx = childOrderIds.isEmpty()
                    ? List.of()
                    : commissionTransactionRepository.findByOrderDetail_IdIn(new ArrayList<>(childOrderIds));
            relatedLevelCommission = childTx.stream()
                    .filter(tx -> tx.getEmployee() != null && childIds.contains(tx.getEmployee().getId()))
                    .map(CommissionTransaction::getCommissionAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else if (employee.getParentId() != null) {
            Long parentId = employee.getParentId();
            List<OrderDetail> parentOrderDetails = orderDetailRepository.findBySeller_IdOrParent_Id(parentId, parentId);
            relatedLevelRevenue = parentOrderDetails.stream()
                    .filter(o -> o.getSeller() != null && parentId.equals(o.getSeller().getId()))
                    .map(this::lineRevenue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Set<Long> parentOrderIds = parentOrderDetails.stream().map(OrderDetail::getId).collect(Collectors.toSet());
            List<CommissionTransaction> parentTx = parentOrderIds.isEmpty()
                    ? List.of()
                    : commissionTransactionRepository.findByOrderDetail_IdIn(new ArrayList<>(parentOrderIds));
            relatedLevelCommission = parentTx.stream()
                    .filter(tx -> tx.getEmployee() != null && parentId.equals(tx.getEmployee().getId()))
                    .map(CommissionTransaction::getCommissionAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return SaleOverviewResDTO.builder()
                .employeeId(employeeId)
                .ownRevenue(ownRevenue)
                .ownCommission(ownCommission)
                .relatedLevelRevenue(relatedLevelRevenue)
                .relatedLevelCommission(relatedLevelCommission)
                .totalRevenue(ownRevenue.add(relatedLevelRevenue))
                .totalCommission(ownCommission.add(relatedLevelCommission))
                .build();
    }

    @Override
    public List<AdminProductRevenueResDTO> getProductRevenue() {
        Map<Long, AdminProductRevenueResDTO> map = new LinkedHashMap<>();
        
        // Khoi tao map voi tat ca san pham
        for (Product product : productRepository.findAll()) {
            map.put(product.getId(), AdminProductRevenueResDTO.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .price(product.getPrice())
                    .quantitySold(0)
                    .revenue(BigDecimal.ZERO)
                    .build());
        }

        // Tinh toan tu cac don hang
        for (OrderDetail orderDetail : orderDetailRepository.findAll()) {
            if (orderDetail.getProduct() == null) continue;
            
            AdminProductRevenueResDTO dto = map.get(orderDetail.getProduct().getId());
            if (dto != null) {
                BigDecimal line = lineRevenue(orderDetail);
                dto.setQuantitySold(dto.getQuantitySold() + orderDetail.getQuantity());
                dto.setRevenue(dto.getRevenue().add(line));
            }
        }
        
        return new ArrayList<>(map.values());
    }

    @Override
    public BigDecimal getCompanyRevenue() {
        return orderDetailRepository.findAll().stream()
                .map(this::lineRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<AdminEmployeeRevenueResDTO> getEmployeeRevenue() {
        Map<Long, AdminEmployeeRevenueResDTO> map = new LinkedHashMap<>();
        for (Employee employee : employeeRepository.findAll()) {
            if (employee.getAccount() == null) continue;
            EmployeeEnum role = employee.getAccount().getRole();
            
            // Chỉ lấy các vai trò SALE
            if (role == EmployeeEnum.SALE_PARENT || role == EmployeeEnum.SALE_CHILD) {
                map.put(employee.getId(), AdminEmployeeRevenueResDTO.builder()
                        .employeeId(employee.getId())
                        .employeeName(employee.getFullName())
                        .role(role.name())
                        .salesRevenue(BigDecimal.ZERO)
                        .totalCommission(BigDecimal.ZERO)
                        .build());
            }
        }

        for (OrderDetail orderDetail : orderDetailRepository.findAll()) {
            if (orderDetail.getSeller() == null) {
                continue;
            }
            AdminEmployeeRevenueResDTO dto = map.get(orderDetail.getSeller().getId());
            if (dto != null) {
                dto.setSalesRevenue(dto.getSalesRevenue().add(lineRevenue(orderDetail)));
            }
        }

        for (CommissionTransaction tx : commissionTransactionRepository.findAll()) {
            if (tx.getEmployee() == null) {
                continue;
            }
            AdminEmployeeRevenueResDTO dto = map.get(tx.getEmployee().getId());
            if (dto != null) {
                dto.setTotalCommission(dto.getTotalCommission().add(tx.getCommissionAmount()));
            }
        }

        return new ArrayList<>(map.values());
    }

    private BigDecimal lineRevenue(OrderDetail orderDetail) {
        return orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity()));
    }

    private void ensureEmployeeExists(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new BusinessException(ErrorCode.EMPLOYEE_001);
        }
    }
}
