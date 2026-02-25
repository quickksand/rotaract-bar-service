package com.lichius.rac.ansbach.altstadtfest.application.controller;

import com.lichius.rac.ansbach.altstadtfest.application.service.IngredientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IngredientController.class)
class IngredientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    IngredientService ingredientService;

    @Test
    void getIngredients() throws Exception {
//        TODO vervollständigen
//        Erwartet Liste an IngredientDTO

        mockMvc.perform(get("/api/ingredients")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}