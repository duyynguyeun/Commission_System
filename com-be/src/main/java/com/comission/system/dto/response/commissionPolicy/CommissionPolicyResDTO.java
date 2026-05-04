package com.comission.system.dto.response.commissionPolicy;

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
public class CommissionPolicyResDTO {
    private Long id;
    private BigDecimal companyRate;
    private BigDecimal parentRate;
    private BigDecimal childRate;
    private Long productId;
    private Instant createAt;
    private Instant updateAt;
}
