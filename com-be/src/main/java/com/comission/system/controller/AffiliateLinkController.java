package com.comission.system.controller;

import com.comission.system.dto.request.affiliateLink.AffiliateLinkRenderReqDTO;
import com.comission.system.dto.request.affiliateLink.AffiliateLinkReqDTO;
import com.comission.system.dto.response.ApiResponse;
import com.comission.system.dto.response.affiliateLink.AffiliateLinkResDTO;
import com.comission.system.dto.response.page.PageResponse;
import com.comission.system.service.AffiliateLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/affiliate-links")
@RequiredArgsConstructor
public class AffiliateLinkController {
    private final AffiliateLinkService affiliateLinkService;
    @PostMapping
    public ApiResponse<AffiliateLinkResDTO> create(@Valid @RequestBody AffiliateLinkReqDTO reqDTO) {
        return ApiResponse.success(affiliateLinkService.create(reqDTO));
    }

    @PostMapping("{id}")
    public ApiResponse<AffiliateLinkResDTO> update(@PathVariable Long id, @Valid @RequestBody AffiliateLinkReqDTO reqDTO) {
        return ApiResponse.success(affiliateLinkService.update(id, reqDTO));
    }

    @PostMapping("/render")
    public ApiResponse<AffiliateLinkResDTO> renderMyLink(@AuthenticationPrincipal Jwt jwt,
                                                          @Valid @RequestBody AffiliateLinkRenderReqDTO reqDTO) {
        Number saleIdClaim = jwt.getClaim("userId");
        Long saleId = saleIdClaim.longValue();
        return ApiResponse.success(affiliateLinkService.renderForSale(saleId, reqDTO));
    }

    @GetMapping("/me")
    public ApiResponse<List<AffiliateLinkResDTO>> myLinks(@AuthenticationPrincipal Jwt jwt) {
        Number saleIdClaim = jwt.getClaim("userId");
        Long saleId = saleIdClaim.longValue();
        return ApiResponse.success(affiliateLinkService.findMyLinks(saleId));
    }

    @GetMapping("track/{affCode}")
    public ApiResponse<AffiliateLinkResDTO> trackByCode(@PathVariable String affCode) {
        return ApiResponse.success(affiliateLinkService.trackByCode(affCode));
    }

    @DeleteMapping("{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        affiliateLinkService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<PageResponse<AffiliateLinkResDTO>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<AffiliateLinkResDTO> data = affiliateLinkService.findAll(pageable);
        return ApiResponse.success(new PageResponse<>(data));
    }
}
