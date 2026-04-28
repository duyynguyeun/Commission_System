package com.comission.system.mapper;

import com.comission.system.config.MapperConfiguaration;
import com.comission.system.dto.request.product.ProductReqDTO;
import com.comission.system.dto.response.product.ProductResDTO;
import com.comission.system.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguaration.class)
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "affiliateLinks", ignore = true)
    Product toEntity(ProductReqDTO req);

    ProductResDTO toResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "affiliateLinks", ignore = true)
    void updateProductFromReq(ProductReqDTO req, @MappingTarget Product product);
}
