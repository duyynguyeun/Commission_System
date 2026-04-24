package com.comission.system.dto.response.orderDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResDTO {
    private Long id;
    private BigDecimal price;
    private Integer quantity;
    private Long customerOrderId;
    private Long productId;
    private Long sellerId;
    private Long parentId;
    private Long affiliateLinkId;
}