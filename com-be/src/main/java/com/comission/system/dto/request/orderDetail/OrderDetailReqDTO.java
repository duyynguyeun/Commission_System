package com.comission.system.dto.request.orderDetail;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderDetailReqDTO {

    @NotNull(message = "Gia khong duoc de trong")
    private BigDecimal price;

    @NotNull(message = "So luong khong duoc de trong")
    private Integer quantity;

    @NotNull(message = "Ma don hang khong duoc de trong")
    private Long customerOrderId;

    @NotNull(message = "Ma san pham khong duoc de trong")
    private Long productId;

    private Long sellerId;

    private Long parentId;

    private Long affiliateLinkId;

    private String affCode;
}
