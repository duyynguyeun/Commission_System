package com.comission.system.dto.response.affiliateLink;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffiliateLinkResDTO {
    private Long id;
    private String affCode;
    private String affUrl;
    private Long productId;
    private Long employeeId;
    private Instant createAt;
    private Instant updateAt;
}
