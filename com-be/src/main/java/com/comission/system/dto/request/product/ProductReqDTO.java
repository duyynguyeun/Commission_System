package com.comission.system.dto.request.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductReqDTO {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Giá của sản phẩm không được để trống")
    private BigDecimal price;

    @NotNull(message = "Số ượng hàng hóa không được để trống")
    private Integer stockQuantity;

    private String urlImage;

    @Size(max = 500, message = "Giới thiệu sản phẩm không được quá 500 kí tự")
    private String description;
}
