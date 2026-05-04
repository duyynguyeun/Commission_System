package com.comission.system.mapper;

import com.comission.system.config.MapperConfiguaration;
import com.comission.system.dto.request.employee.EmployeeReqDTO;
import com.comission.system.dto.response.employee.EmployeeResDTO;
import com.comission.system.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguaration.class)
public interface EmployeeMapper {
    @Mapping(target = "account.username", source = "username")
    @Mapping(target = "account.password", source = "password")
    @Mapping(target = "account.role", source = "role")
    Employee toEntity(EmployeeReqDTO req);

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "username", source = "account.username")
    @Mapping(target = "role", source = "account.role")
    EmployeeResDTO toResponse(Employee employee);

    @Mapping(target = "account.username", source = "username")
    @Mapping(target = "account.password", source = "password")
    @Mapping(target = "account.role", source = "role")
    void updateFromReq(EmployeeReqDTO req, @MappingTarget Employee employee);
}
