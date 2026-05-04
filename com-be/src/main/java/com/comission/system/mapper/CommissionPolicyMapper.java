package com.comission.system.mapper;

import com.comission.system.config.MapperConfiguaration;
import com.comission.system.dto.request.commissionPolicy.CommissionPolicyReqDTO;
import com.comission.system.dto.response.commissionPolicy.CommissionPolicyResDTO;
import com.comission.system.entity.CommissionPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguaration.class)
public interface CommissionPolicyMapper {

    @Mapping(source = "productId", target = "product.id")
    CommissionPolicy toEntity(CommissionPolicyReqDTO req);

    @Mapping(source = "product.id", target = "productId")
    CommissionPolicyResDTO toResponse(CommissionPolicy commissionPolicy);

    @Mapping(source = "productId", target = "product.id")
    void updateCommissionPolicyFromReq(CommissionPolicyReqDTO req, @MappingTarget CommissionPolicy commissionPolicy);
}
