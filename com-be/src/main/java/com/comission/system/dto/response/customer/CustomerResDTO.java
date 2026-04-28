package com.comission.system.dto.response.customer;

import com.comission.system.enums.EmployeeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResDTO {
    private Long id;
    private String fullName;
    private LocalDate dob;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;

    private Long accountId;
    private String username;
    private EmployeeEnum role;
}