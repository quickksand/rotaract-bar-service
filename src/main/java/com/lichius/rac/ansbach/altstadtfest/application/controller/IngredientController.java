package com.lichius.rac.ansbach.altstadtfest.application.controller;

import com.lichius.rac.ansbach.altstadtfest.application.model.Ingredient;
import com.lichius.rac.ansbach.altstadtfest.application.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rotaract.bar.infrastructure.api.controller.IngredientControllerApi;
import rotaract.bar.infrastructure.api.controller.model.IngredientDto;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController implements IngredientControllerApi {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<List<IngredientDto>> getIngredients() {
        List<Ingredient> ingredients = ingredientService.getIngredients();
        return new ResponseEntity<>(ingredients.stream()
                .map(this::mapToDto)
                .toList(), HttpStatus.OK);
    }

    //TODO Extract to Mapper
    private IngredientDto mapToDto(Ingredient ingredient) {
        IngredientDto dto = new IngredientDto();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        return dto;
    }

}
