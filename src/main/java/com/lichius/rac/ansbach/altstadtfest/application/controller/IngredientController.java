package com.lichius.rac.ansbach.altstadtfest.application.controller;

import com.lichius.rac.ansbach.altstadtfest.application.model.Ingredient;
import com.lichius.rac.ansbach.altstadtfest.application.service.IngredientService;
import com.lichius.rac.ansbach.altstadtfest.infrastructure.mapper.IngredientMapper;
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
    private final IngredientMapper ingredientMapper;

    public IngredientController(IngredientService ingredientService, IngredientMapper ingredientMapper) {
        this.ingredientService = ingredientService;
        this.ingredientMapper = ingredientMapper;
    }

    @GetMapping
    public ResponseEntity<List<IngredientDto>> getIngredients() {
        List<Ingredient> ingredients = ingredientService.getIngredients();
        return new ResponseEntity<>(ingredients.stream()
                .map(ingredientMapper::toDto)
                .toList(), HttpStatus.OK);
    }

}
