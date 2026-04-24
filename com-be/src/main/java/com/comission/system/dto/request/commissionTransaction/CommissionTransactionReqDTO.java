package com.comission.system.dto.request.commissionTransaction;

import com.comission.system.entity.CommissionPolicy;
import com.comission.system.entity.Employee;
import com.comission.system.entity.OrderDetail;
import com.comission.system.enums.CommissionEnum;
import com.comission.system.enums.EmployeeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageContext;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommissionTransactionReqDTO {

    @NotNull(message = "Vai trò của người nhận hoa hồng này không để trống")
    private EmployeeEnum commissionRole;

    @NotNull(message = "Tỷ lệ hoa hồng không được để trống")
    private BigDecimal commissionRate;

    @NotNull(message = "Tiền hoa hồng không được để trống")
    private BigDecimal commissionAmount;

    @NotNull(message = "Trạng thái không được để trống")
    private CommissionEnum status;

    @NotNull(message = "Mã chính sách hoa hồng không được để trống")
    private Long commissionPolicyId;

    @NotNull(message = "Mã đơn hàng chi tiết không được để trống")
    private Long orderDetailId;

    @NotNull(message = "Mã nhân viên không được để trống")
    private Long employeeId;
}
