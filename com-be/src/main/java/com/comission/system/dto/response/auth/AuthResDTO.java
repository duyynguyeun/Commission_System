package com.comission.system.dto.response.auth;

import com.comission.system.enums.EmployeeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResDTO {
    private String accessToken;
    private String tokenType;
    private Long accountId;
    private Long userId;
    private String username;
    private EmployeeEnum role;
}
