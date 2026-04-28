package com.comission.system.dto.response.employee;

import com.comission.system.enums.EmployeeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResDTO {
    private Long id;
    private Long parentId;
    private String fullName;
    private Instant createAt;
    private Instant updateAt;

    private Long accountId;
    private String username;
    private EmployeeEnum role;
}