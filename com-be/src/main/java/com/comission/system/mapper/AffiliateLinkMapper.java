package com.comission.system.mapper;

import com.comission.system.config.MapperConfiguaration;
import com.comission.system.dto.request.affiliateLink.AffiliateLinkReqDTO;
import com.comission.system.dto.response.affiliateLink.AffiliateLinkResDTO;
import com.comission.system.entity.AffiliateLink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguaration.class)
public interface AffiliateLinkMapper {
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "employee.id", source = "employeeId")
    AffiliateLink toEntity(AffiliateLinkReqDTO req);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "employeeId", source = "employee.id")
    AffiliateLinkResDTO toResponse(AffiliateLink affiliateLink);

    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "employee.id", source = "employeeId")
    void updateFromReq(AffiliateLinkReqDTO req, @MappingTarget AffiliateLink affiliateLink);
}
