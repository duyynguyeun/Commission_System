package com.comission.system.mapper;

import com.comission.system.config.MapperConfiguaration;
import com.comission.system.dto.request.commissionPolicy.CommissionPolicyReqDTO;
import com.comission.system.dto.response.commissionPolicy.CommissionPolicyResDTO;
import com.comission.system.entity.CommissionPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguaration.class)
public interface CommissionPolicyMapper {
    CommissionPolicy toEntity(CommissionPolicyReqDTO req);

    CommissionPolicyResDTO toResponse(CommissionPolicy commissionPolicy);

    void updateCommissionPolicyFromReq(CommissionPolicyReqDTO req, @MappingTarget CommissionPolicy commissionPolicy);
}
