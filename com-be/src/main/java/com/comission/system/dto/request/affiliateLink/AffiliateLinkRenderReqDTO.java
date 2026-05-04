package com.comission.system.dto.request.affiliateLink;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffiliateLinkRenderReqDTO {
    @NotNull(message = "Product id is required")
    private Long productId;
}
