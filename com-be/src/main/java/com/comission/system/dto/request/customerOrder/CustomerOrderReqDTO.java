package com.comission.system.dto.request.customerOrder;

import com.comission.system.enums.OrderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CustomerOrderReqDTO {

    private BigDecimal totalPrice;

    private OrderEnum status;

    private Long customerId;
}
