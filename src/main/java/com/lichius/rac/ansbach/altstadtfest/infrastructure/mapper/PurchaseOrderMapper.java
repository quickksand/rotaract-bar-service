package com.lichius.rac.ansbach.altstadtfest.infrastructure.mapper;

import com.lichius.rac.ansbach.altstadtfest.application.model.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rotaract.bar.infrastructure.api.controller.model.PurchaseOrderDto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring", uses = {OrderedItemMapper.class, ProductMapper.class})
public interface PurchaseOrderMapper {

    @Mapping(target = "returnedCupsCount", source = "returnedCupsCount")
    PurchaseOrderDto toDto(PurchaseOrder entity);

    default OffsetDateTime map(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atOffset(ZoneOffset.UTC);
    }

}
