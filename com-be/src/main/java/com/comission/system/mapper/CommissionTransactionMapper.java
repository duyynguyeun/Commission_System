package com.comission.system.mapper;

import com.comission.system.config.MapperConfiguaration;
import com.comission.system.dto.request.commissionTransaction.CommissionTransactionReqDTO;
import com.comission.system.dto.response.commissionTransaction.CommissionTransactionResDTO;
import com.comission.system.entity.CommissionTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguaration.class)
public interface CommissionTransactionMapper {
    @Mapping(target = "commissionPolicy.id", source = "commissionPolicyId")
    @Mapping(target = "orderDetail.id", source = "orderDetailId")
    @Mapping(target = "employee.id", source = "employeeId")
    CommissionTransaction toEntity(CommissionTransactionReqDTO req);

    @Mapping(target = "commissionPolicyId", source = "commissionPolicy.id")
    @Mapping(target = "orderDetailId", source = "orderDetail.id")
    @Mapping(target = "empolyeeId", source = "employee.id")
    CommissionTransactionResDTO toResponse(CommissionTransaction commissionTransaction);

    @Mapping(target = "commissionPolicy.id", source = "commissionPolicyId")
    @Mapping(target = "orderDetail.id", source = "orderDetailId")
    @Mapping(target = "employee.id", source = "employeeId")
    void updateFromReq(CommissionTransactionReqDTO req, @MappingTarget CommissionTransaction commissionTransaction);
}
