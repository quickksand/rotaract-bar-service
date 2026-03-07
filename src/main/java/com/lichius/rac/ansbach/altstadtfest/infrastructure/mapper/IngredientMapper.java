package com.lichius.rac.ansbach.altstadtfest.infrastructure.mapper;

import com.lichius.rac.ansbach.altstadtfest.application.model.Ingredient;
import org.mapstruct.Mapper;
import rotaract.bar.infrastructure.api.controller.model.IngredientDto;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    IngredientDto toDto(Ingredient ingredientEntity);

}
