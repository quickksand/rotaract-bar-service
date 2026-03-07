package com.lichius.rac.ansbach.altstadtfest.application.service;

import com.lichius.rac.ansbach.altstadtfest.application.model.Ingredient;
import com.lichius.rac.ansbach.altstadtfest.application.model.Product;
import com.lichius.rac.ansbach.altstadtfest.application.repository.IngredientRepository;
import com.lichius.rac.ansbach.altstadtfest.application.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rotaract.bar.infrastructure.api.controller.model.ProductDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@DependsOn("ingredientService")
public class ProductService {

    private final ProductRepository productRepository;
    private final IngredientService ingredientService;

    public ProductService(ProductRepository productRepository,
                          IngredientRepository ingredientRepository, IngredientService ingredientService) {
        this.productRepository = productRepository;
        this.ingredientService = ingredientService;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    private List<Product> getDefaultProducts() {
        return List.of(
                // DRINKS
                new Product("Cuba Libre", new BigDecimal("6.50"), ProductDto.CategoryEnum.DRINKS),
                new Product("Gin Tonic", new BigDecimal("7.00"), ProductDto.CategoryEnum.DRINKS),
                new Product("Moscow Mule", new BigDecimal("7.00"), ProductDto.CategoryEnum.DRINKS),
                new Product("Munich Mule", new BigDecimal("7.00"), ProductDto.CategoryEnum.DRINKS),
                new Product("Wodka Bull", new BigDecimal("7.00"), ProductDto.CategoryEnum.DRINKS),
                new Product("Aperol Spritz", new BigDecimal("6.00"), ProductDto.CategoryEnum.DRINKS),
                new Product("Sarti Spritz", new BigDecimal("6.00"), ProductDto.CategoryEnum.DRINKS),
                new Product("Wildberry Lillet", new BigDecimal("6.00"), ProductDto.CategoryEnum.DRINKS),
                // Bier, Wein & Nonalkoholisches
                new Product("Bier 0,33", new BigDecimal("3.50"), ProductDto.CategoryEnum.BEER_WINE_NONALC),
                new Product("Bier Alkfrei", new BigDecimal("3.50"), ProductDto.CategoryEnum.BEER_WINE_NONALC),
                new Product("Radler 0,5", new BigDecimal("4.00"), ProductDto.CategoryEnum.BEER_WINE_NONALC),
                new Product("Weinschorle", new BigDecimal("4.00"), ProductDto.CategoryEnum.BEER_WINE_NONALC),
                new Product("Cola", new BigDecimal("3.00"), ProductDto.CategoryEnum.BEER_WINE_NONALC),
                new Product("Sprite", new BigDecimal("3.00"), ProductDto.CategoryEnum.BEER_WINE_NONALC),
                new Product("Wasser", new BigDecimal("2.50"), ProductDto.CategoryEnum.BEER_WINE_NONALC),
                // SHOTS
                new Product("Brausegeist / Wodka Ahoi!", new BigDecimal("2.00"), ProductDto.CategoryEnum.SHOTS),
                new Product("Mexikaner", new BigDecimal("2.00"), ProductDto.CategoryEnum.SHOTS)
        );
    }

    @PostConstruct
    @Transactional
    public void initializeProducts() {
        if (productRepository.count() == 0) {
            // 1. Products speichern
            List<Product> products = productRepository.saveAll(getDefaultProducts());
            log.info("✅ Standard Products initialized");

            // 2. Ingredients zuweisen
            assignIngredientsToProducts(products);
            log.info("✅ Ingredients assigned to Products");
        }
    }

    private void assignIngredientsToProducts(List<Product> products) {
        List<Ingredient> ingredients = ingredientService.getIngredients();

        Map<String, Ingredient> ingredientMap = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            ingredientMap.put(ingredient.getName(), ingredient);
        }

        for (Product product : products) {
            switch (product.getName()) {
                case "Cuba Libre":
                    product.getIngredients().addAll(Arrays.asList(
                            getIngredientOrThrow(ingredientMap, "Rum"),
                            getIngredientOrThrow(ingredientMap, "Cola"),
                            getIngredientOrThrow(ingredientMap, "Limette"),
                            getIngredientOrThrow(ingredientMap, "Limettensaft")
                    ));
                    break;

                case "Gin Tonic":
                    product.getIngredients().addAll(Arrays.asList(
                            getIngredientOrThrow(ingredientMap, "Gin"),
                            getIngredientOrThrow(ingredientMap, "Tonic Water"),
                            getIngredientOrThrow(ingredientMap, "Gurke")
                    ));
                    break;

                case "Moscow Mule":
                    product.getIngredients().addAll(Arrays.asList(
                            getIngredientOrThrow(ingredientMap, "Vodka"),
                            getIngredientOrThrow(ingredientMap, "Ginger Beer"),
                            getIngredientOrThrow(ingredientMap, "Limette")
                    ));
                    break;

                case "Munich Mule":
                    product.getIngredients().addAll(Arrays.asList(
                            getIngredientOrThrow(ingredientMap, "Gin"),
                            getIngredientOrThrow(ingredientMap, "Ginger Beer"),
                            getIngredientOrThrow(ingredientMap, "Limettensaft"),
                            getIngredientOrThrow(ingredientMap, "Gurke")
                    ));
                    break;

                case "Wodka Bull":
                    product.getIngredients().addAll(Arrays.asList(
                            getIngredientOrThrow(ingredientMap, "Vodka"),
                            getIngredientOrThrow(ingredientMap, "Red Bull")
                    ));
                    break;

                case "Aperol Spritz":
                    product.getIngredients().addAll(Arrays.asList(
                            getIngredientOrThrow(ingredientMap, "Aperol"),
                            getIngredientOrThrow(ingredientMap, "Prosecco"),
                            getIngredientOrThrow(ingredientMap, "Wasser spritzig"),
                            getIngredientOrThrow(ingredientMap, "Orange")
                    ));
                    break;

                case "Sarti Spritz":
                    product.getIngredients().addAll(Arrays.asList(
                            getIngredientOrThrow(ingredientMap, "Sarti"),
                            getIngredientOrThrow(ingredientMap, "Prosecco"),
                            getIngredientOrThrow(ingredientMap, "Wasser spritzig"),
                            getIngredientOrThrow(ingredientMap, "Zitrone")
                    ));
                    break;

                case "Wildberry Lillet":
                    product.getIngredients().addAll(Arrays.asList(
                            getIngredientOrThrow(ingredientMap, "Lillet"),
                            getIngredientOrThrow(ingredientMap, "Wildberry"),
                            getIngredientOrThrow(ingredientMap, "Beeren-Mix")
                    ));
                    break;

                default:
                    break;
            }
        }

        productRepository.saveAll(products);
    }

    private Ingredient getIngredientOrThrow(Map<String, Ingredient> ingredientMap, String name) {
        Ingredient ingredient = ingredientMap.get(name);
        if (ingredient == null) {
            throw new RuntimeException("Ingredient not found: " + name);
        }
        return ingredient;
    }
}