package com.comission.system.mapper;

import com.comission.system.config.MapperConfiguaration;
import com.comission.system.dto.request.orderDetail.OrderDetailReqDTO;
import com.comission.system.dto.response.orderDetail.OrderDetailResDTO;
import com.comission.system.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguaration.class)
public interface OrderDetailMapper {
    @Mapping(target = "customerOrder.id", source = "customerOrderId")
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "seller.id", source = "sellerId")
    @Mapping(target = "parent.id", source = "parentId")
    @Mapping(target = "affiliateLink.id", source = "affiliateLinkId")
    OrderDetail toEntity(OrderDetailReqDTO req);

    @Mapping(target = "customerOrderId", source = "customerOrder.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "sellerId", source = "seller.id")
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "affiliateLinkId", source = "affiliateLink.id")
    OrderDetailResDTO toResponse(OrderDetail orderDetail);

    @Mapping(target = "customerOrder.id", source = "customerOrderId")
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "seller.id", source = "sellerId")
    @Mapping(target = "parent.id", source = "parentId")
    @Mapping(target = "affiliateLink.id", source = "affiliateLinkId")
    void updateFromReq(OrderDetailReqDTO req, @MappingTarget OrderDetail orderDetail);
}
