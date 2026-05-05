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
public class AdminEmployeeRevenueResDTO {
    private Long employeeId;
    private String employeeName;
    private String role;
    private BigDecimal salesRevenue;
    private BigDecimal totalCommission;
}
