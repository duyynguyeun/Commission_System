package com.comission.system.dto.response.commissionTransaction;

import com.comission.system.enums.CommissionEnum;
import com.comission.system.enums.EmployeeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommissionTransactionResDTO {
    private Long id;
    private EmployeeEnum commissionRole;
    private BigDecimal commissionRate;
    private BigDecimal commissionAmount;
    private CommissionEnum status;
    private Instant createAt;
    private Long commissionPolicyId;
    private Long orderDetailId;
    private Long empolyeeId;
}
