package com.comission.system.service.impl;

import com.comission.system.dto.request.orderDetail.OrderDetailReqDTO;
import com.comission.system.dto.response.orderDetail.OrderDetailResDTO;
import com.comission.system.entity.AffiliateLink;
import com.comission.system.entity.Employee;
import com.comission.system.entity.OrderDetail;
import com.comission.system.exception.BusinessException;
import com.comission.system.exception.ErrorCode;
import com.comission.system.mapper.OrderDetailMapper;
import com.comission.system.repository.AffiliateLinkRepository;
import com.comission.system.repository.EmployeeRepository;
import com.comission.system.repository.OrderDetailRepository;
import com.comission.system.service.OrderDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final AffiliateLinkRepository affiliateLinkRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    public OrderDetailResDTO create(OrderDetailReqDTO reqDTO) {
        OrderDetail orderDetail = orderDetailMapper.toEntity(reqDTO);
        applyAffiliateHierarchy(orderDetail, reqDTO);
        validateHierarchy(orderDetail);
        OrderDetail saved = orderDetailRepository.save(orderDetail);
        return orderDetailMapper.toResponse(saved);
    }

    @Override
    public OrderDetailResDTO update(Long id, OrderDetailReqDTO reqDTO) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_002));
        orderDetailMapper.updateFromReq(reqDTO, orderDetail);
        applyAffiliateHierarchy(orderDetail, reqDTO);
        validateHierarchy(orderDetail);
        OrderDetail saved = orderDetailRepository.save(orderDetail);
        return orderDetailMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!orderDetailRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.ORDER_002);
        }
        orderDetailRepository.deleteById(id);
    }

    @Override
    public Page<OrderDetailResDTO> findAll(Pageable pageable) {
        return orderDetailRepository.findAll(pageable).map(orderDetailMapper::toResponse);
    }

    private void applyAffiliateHierarchy(OrderDetail orderDetail, OrderDetailReqDTO reqDTO) {
        AffiliateLink affiliateLink = resolveAffiliateLink(reqDTO);
        if (affiliateLink == null) {
            return;
        }

        Employee seller = affiliateLink.getEmployee();
        orderDetail.setAffiliateLink(affiliateLink);
        orderDetail.setSeller(seller);

        if (seller != null && seller.getParentId() != null) {
            Employee parent = employeeRepository.findById(seller.getParentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_001));
            orderDetail.setParent(parent);
        } else {
            orderDetail.setParent(null);
        }
    }

    private AffiliateLink resolveAffiliateLink(OrderDetailReqDTO reqDTO) {
        if (reqDTO.getAffCode() != null && !reqDTO.getAffCode().isBlank()) {
            return affiliateLinkRepository.findByAffCode(reqDTO.getAffCode())
                    .orElseThrow(() -> new BusinessException(ErrorCode.LINK_003));
        }

        if (reqDTO.getAffiliateLinkId() != null) {
            return affiliateLinkRepository.findById(reqDTO.getAffiliateLinkId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.LINK_003));
        }

        return null;
    }

    private void validateHierarchy(OrderDetail orderDetail) {
        if (orderDetail.getParent() != null
                && orderDetail.getSeller() != null
                && orderDetail.getSeller().getParentId() != null
                && !orderDetail.getSeller().getParentId().equals(orderDetail.getParent().getId())) {
            throw new BusinessException(ErrorCode.EMPLOYEE_001);
        }
    }
}
