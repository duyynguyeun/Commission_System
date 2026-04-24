package com.comission.system.dto.request.affiliateLink;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AffiliateLinkReqDTO {
    @NotBlank(message = "Mã link affiliate không được để trống")
    private String affCode;

    @NotBlank(message = "Url affiliate không được để trống")
    private String affUrl;

    @NotNull(message = "Mã sản phẩm không đươc để trống")
    private Long productId;

    @NotNull(message = "Mã nhân viên không được để trống")
    private Long employeeId;
}
