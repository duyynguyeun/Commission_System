package com.comission.system.dto.request.commissionPolicy;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommissionPolicyReqDTO {

    @NotNull(message = "Tỷ lệ của công ty không được để trống")
    private BigDecimal companyRate;

    @NotNull(message = "Tỷ lệ của sale cấp cha không được để trống")
    private BigDecimal parentRate;

    @NotNull(message = "Tỷ lệ của sale cấp con không được để trống")
    private BigDecimal childRate;

    @NotNull(message = "Trạng thái của chính sách hoa hồng không được để trống")
    private Boolean isActive;

    @NotNull(message = "Thời gian chính sách bắt đầu không được để trống")
    private Instant effectiveFrom;

    @NotNull(message = "Thời gian chính sách kết thúc không được để trống")
    private Instant effectiveTo;

    @NotNull(message = "Mã sản phẩm không được để trống")
    private Long productId;
}
