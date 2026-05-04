package com.comission.system.service;

import com.comission.system.dto.request.affiliateLink.AffiliateLinkReqDTO;
import com.comission.system.dto.request.affiliateLink.AffiliateLinkRenderReqDTO;
import com.comission.system.dto.response.affiliateLink.AffiliateLinkResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AffiliateLinkService {
    AffiliateLinkResDTO create(AffiliateLinkReqDTO reqDTO);
    AffiliateLinkResDTO update(Long id, AffiliateLinkReqDTO reqDTO);
    AffiliateLinkResDTO trackByCode(String affCode);
    AffiliateLinkResDTO renderForSale(Long saleId, AffiliateLinkRenderReqDTO reqDTO);
    List<AffiliateLinkResDTO> findMyLinks(Long saleId);
    void delete(Long id);
    Page<AffiliateLinkResDTO> findAll(Pageable pageable);
}
