package com.lichius.rac.ansbach.altstadtfest.application.service;

import com.lichius.rac.ansbach.altstadtfest.application.model.Ingredient;
import com.lichius.rac.ansbach.altstadtfest.application.repository.IngredientRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    private List<Ingredient> getDefaultIngredients() {
        return Arrays.asList(
                // Spirituosen
                new Ingredient("Rum"),
                new Ingredient("Gin"),
                new Ingredient("Vodka"),
                new Ingredient("Aperol"),
                new Ingredient("Sarti"),
                new Ingredient("Prosecco"),
                new Ingredient("Lillet"),

                // Mixer & Säfte
                new Ingredient("Cola"),
                new Ingredient("Tonic Water"),
                new Ingredient("Ginger Beer"),
                new Ingredient("Red Bull"),
                new Ingredient("Wasser spritzig"),
                new Ingredient("Sprite"),
                new Ingredient("Wildberry"),

                // Garnituren
                new Ingredient("Limette"),
                new Ingredient("Limettensaft"),
                new Ingredient("Zitrone"),
                new Ingredient("Orange"),
                new Ingredient("Gurke"),
                new Ingredient("Beeren-Mix"),

                // Wein
                new Ingredient("Weißwein"),
                new Ingredient("Mineralwasser"),

                // Shots
                new Ingredient("Brause"),
                new Ingredient("Mexikaner"),
                // Bier & Wein
                new Ingredient("Bier"),
                new Ingredient("Alkoholfreies Bier"),
                new Ingredient("Radler")

        );
    }

    @PostConstruct
    public void initializeIngredients() {
        if (ingredientRepository.count() == 0) {
            ingredientRepository.saveAll(getDefaultIngredients());
            log.info("✅ Standard Ingredients initialized");
        }
    }
}
