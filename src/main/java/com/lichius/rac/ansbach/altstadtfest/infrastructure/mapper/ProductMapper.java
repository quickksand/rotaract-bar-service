package com.lichius.rac.ansbach.altstadtfest.infrastructure.mapper;

import com.lichius.rac.ansbach.altstadtfest.application.model.Ingredient;
import com.lichius.rac.ansbach.altstadtfest.application.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rotaract.bar.infrastructure.api.controller.model.ProductDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "ingredients", target = "ingredientIds")
    ProductDto toDto(Product entity);

    default List<Long> mapIngredientsToIds(List<Ingredient> ingredients) {
        if (ingredients == null) {
            return new ArrayList<>();
        }
        return ingredients.stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList());
    }
}
