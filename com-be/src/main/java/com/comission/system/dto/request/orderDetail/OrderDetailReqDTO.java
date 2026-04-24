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

    @NotNull(message = "Giá không được để trống")
    private BigDecimal price;

    @NotNull(message = "Số lượng không được để trống")
    private Integer quantity;

    @NotNull(message = "Mã đơn hàng không được để trống")
    private Long customerOrderId;

    @NotNull(message = "Mã sản phẩm không được để trống")
    private Long productId;

    @NotNull(message = "Mã người bán không được để trống")
    private Long sellerId;

    private Long parentId;

    @NotNull(message = "Mã link sản phẩm không được để trống")
    private Long affiliateLinkId;
}
