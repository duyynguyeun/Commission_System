package com.comission.system.mapper;

import com.comission.system.config.MapperConfiguaration;
import com.comission.system.dto.request.customerOrder.CustomerOrderReqDTO;
import com.comission.system.dto.response.customerOrder.CustomerOrderResDTO;
import com.comission.system.entity.CustomerOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguaration.class)
public interface CustomerOrderMapper {
    @Mapping(target = "customer.id", source = "customerId")
    CustomerOrder toEntity(CustomerOrderReqDTO req);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "createdAt", source = "createAt")
    @Mapping(target = "updatedAt", source = "updateAt")
    CustomerOrderResDTO toResponse(CustomerOrder customerOrder);

    @Mapping(target = "customer.id", source = "customerId")
    void updateFromReq(CustomerOrderReqDTO req, @MappingTarget CustomerOrder customerOrder);
}
