package com.comission.system.dto.response.report;

import com.comission.system.enums.EmployeeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleHistoryResDTO {
    private Long orderDetailId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal lineRevenue;
    private EmployeeEnum commissionRole;
    private BigDecimal commissionRate;
    private BigDecimal commissionAmount;
    private String sellerName;
    private BigDecimal relatedCommissionRate;
    private BigDecimal relatedCommissionAmount;
    private Instant transactionAt;
}
