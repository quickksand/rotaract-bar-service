package com.lichius.rac.ansbach.altstadtfest.infrastructure.mapper;

import com.lichius.rac.ansbach.altstadtfest.application.model.PurchaseOrder;
import org.mapstruct.Mapper;
import rotaract.bar.infrastructure.api.controller.model.PurchaseOrderDto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring", uses = {OrderedItemMapper.class, ProductMapper.class})
public interface PurchaseOrderMapper {

    //    @Mapping(source = "orderedAt", target = "orderedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    PurchaseOrderDto toDto(PurchaseOrder entity);

    // Custom Mapping-Methoden:
    default OffsetDateTime map(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atOffset(ZoneOffset.UTC);
    }

//    default LocalDateTime map(OffsetDateTime offsetDateTime) {
//        return offsetDateTime == null ? null : offsetDateTime.toLocalDateTime();
//    }

}
