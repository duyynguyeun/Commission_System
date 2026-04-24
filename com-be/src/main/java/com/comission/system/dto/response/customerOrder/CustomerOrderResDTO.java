package com.comission.system.dto.response.customerOrder;

import com.comission.system.enums.OrderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerOrderResDTO {
    private Long id;
    private BigDecimal totalPrice;
    private OrderEnum status;
    private Long customerId;
    private Instant createdAt;
    private Instant updatedAt;
}