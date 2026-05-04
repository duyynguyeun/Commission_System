package com.comission.system.dto.request.commissionPolicy;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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
public class CommissionPolicyReqDTO {

    @NotNull(message = "Company rate is required")
    @DecimalMin(value = "70")
    @DecimalMax(value = "100")
    private BigDecimal companyRate;

    @NotNull(message = "Parent sale rate is required")
    @DecimalMin(value = "0")
    @DecimalMax(value = "30")
    private BigDecimal parentRate;

    @NotNull(message = "Child sale rate is required")
    @DecimalMin(value = "0")
    @DecimalMax(value = "30")
    private BigDecimal childRate;

    @NotNull(message = "Product id is required")
    private Long productId;
}