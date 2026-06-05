package com.lichius.rac.ansbach.altstadtfest.infrastructure.mapper;

import com.lichius.rac.ansbach.altstadtfest.application.model.OrderedItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rotaract.bar.infrastructure.api.controller.model.OrderedItemDto;

@Mapper(componentModel = "spring")
public interface OrderedItemMapper {

    @Mapping(source = "product.id", target = "productId")
    OrderedItemDto toDto(OrderedItem entity);

}
