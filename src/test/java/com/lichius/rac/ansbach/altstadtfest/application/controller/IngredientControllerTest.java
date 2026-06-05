package com.lichius.rac.ansbach.altstadtfest.application.controller;

import com.lichius.rac.ansbach.altstadtfest.application.controller.IngredientController;
import com.lichius.rac.ansbach.altstadtfest.application.model.Ingredient;
import com.lichius.rac.ansbach.altstadtfest.application.service.IngredientService;
import com.lichius.rac.ansbach.altstadtfest.infrastructure.mapper.IngredientMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rotaract.bar.infrastructure.api.controller.model.IngredientDto;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngredientController.class)
class IngredientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    IngredientService ingredientService;

    @MockitoBean
    IngredientMapper ingredientMapper;

    @Test
    void getIngredients() throws Exception {
        List<Ingredient> ingredients = List.of(
                new Ingredient("Rum"),
                new Ingredient("Gin"),
                new Ingredient("Vodka")
        );

        given(ingredientService.getIngredients()).willReturn(ingredients);
        given(ingredientMapper.toDto(ingredients.get(0)))
                .willReturn(new IngredientDto().id(1L).name("Rum"));
        given(ingredientMapper.toDto(ingredients.get(1)))
                .willReturn(new IngredientDto().id(2L).name("Gin"));
        given(ingredientMapper.toDto(ingredients.get(2)))
                .willReturn(new IngredientDto().id(3L).name("Vodka"));

        mockMvc.perform(get("/api/ingredients")
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("Rum")))
                .andExpect(jsonPath("$[1].name", is("Gin")))
                .andExpect(jsonPath("$[2].name", is("Vodka")));
    }
}