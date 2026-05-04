package com.comission.system.mapper;

import com.comission.system.config.MapperConfiguaration;
import com.comission.system.dto.request.customer.CustomerReqDTO;
import com.comission.system.dto.response.customer.CustomerResDTO;
import com.comission.system.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguaration.class)
public interface CustomerMapper {
    @Mapping(target = "account.username", source = "username")
    @Mapping(target = "account.password", source = "password")
    @Mapping(target = "account.role", source = "role")
    Customer toEntity(CustomerReqDTO req);

    @Mapping(target = "createdAt", source = "createAt")
    @Mapping(target = "updatedAt", source = "updateAt")
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "username", source = "account.username")
    @Mapping(target = "role", source = "account.role")
    CustomerResDTO toResponse(Customer customer);

    @Mapping(target = "account.username", source = "username")
    @Mapping(target = "account.password", source = "password")
    @Mapping(target = "account.role", source = "role")
    void updateFromReq(CustomerReqDTO req, @MappingTarget Customer customer);
}
