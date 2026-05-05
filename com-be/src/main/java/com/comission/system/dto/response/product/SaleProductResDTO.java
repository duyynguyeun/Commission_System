package com.comission.system.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleProductResDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal maxCommissionRate;
    private BigDecimal maxCommissionAmount;
    private String urlImage;
}
