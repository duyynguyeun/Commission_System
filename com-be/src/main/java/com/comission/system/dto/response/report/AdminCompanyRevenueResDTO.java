package com.comission.system.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCompanyRevenueResDTO {
    private BigDecimal totalSalesRevenue;
    private BigDecimal directSalesRevenue;
    private BigDecimal affiliateSalesRevenue;
    private BigDecimal totalCommissionPaid;
    private BigDecimal netCompanyRevenue;
}
