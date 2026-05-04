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
public class AdminProductRevenueResDTO {
    private Long productId;
    private String productName;
    private BigDecimal revenue;
}
