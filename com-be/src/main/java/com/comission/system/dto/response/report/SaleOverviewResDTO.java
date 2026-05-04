package com.comission.system.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleOverviewResDTO {
    private Long employeeId;
    private BigDecimal ownRevenue;
    private BigDecimal ownCommission;
    private BigDecimal relatedLevelRevenue;
    private BigDecimal relatedLevelCommission;
    private BigDecimal totalRevenue;
    private BigDecimal totalCommission;
}
