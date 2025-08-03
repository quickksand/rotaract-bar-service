package com.lichius.rac.ansbach.altstadtfest.application.service;

import com.lichius.rac.ansbach.altstadtfest.application.model.Ingredient;
import com.lichius.rac.ansbach.altstadtfest.application.model.Product;
import com.lichius.rac.ansbach.altstadtfest.application.repository.IngredientRepository;
import com.lichius.rac.ansbach.altstadtfest.application.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import rotaract.bar.infrastructure.api.controller.model.ProductDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
@DependsOn("ingredientService")
public class ProductService {

    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;

    public ProductService(ProductRepository productRepository,
                          IngredientRepository ingredientRepository) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    private List<Product> getDefaultProducts() {
        return Arrays.asList(
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
    public void initalizeProducts() {
        if (productRepository.count() == 0) {
            // 1. Products speichern
            List<Product> products = productRepository.saveAll(getDefaultProducts());
            System.out.println("✅ Standard Products initialized");

            // 2. Ingredients zuweisen
            assignIngredientsToProducts(products);
            System.out.println("✅ Ingredients assigned to Products");
        }
    }

    private void assignIngredientsToProducts(List<Product> products) {
        // Helper Method um Ingredient per Name zu finden
        Function<String, Ingredient> findIngredient = (String name) ->
                ingredientRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Ingredient not found: " + name));

        for (Product product : products) {
            switch (product.getName()) {
                case "Cuba Libre":
                    product.getIngredients().addAll(Arrays.asList(
                            findIngredient.apply("Rum"),
                            findIngredient.apply("Cola"),
                            findIngredient.apply("Limette"),
                            findIngredient.apply("Limettensaft")
                    ));
                    break;

                case "Gin Tonic":
                    product.getIngredients().addAll(Arrays.asList(
                            findIngredient.apply("Gin"),
                            findIngredient.apply("Tonic Water"),
                            findIngredient.apply("Gurke")
                    ));
                    break;

                case "Moscow Mule":
                    product.getIngredients().addAll(Arrays.asList(
                            findIngredient.apply("Vodka"),
                            findIngredient.apply("Ginger Beer"),
                            findIngredient.apply("Limette")
                    ));
                    break;

                case "Munich Mule":
                    product.getIngredients().addAll(Arrays.asList(
                            findIngredient.apply("Gin"),
                            findIngredient.apply("Ginger Beer"),
                            findIngredient.apply("Limettensaft"),
                            findIngredient.apply("Gurke")
                    ));
                    break;

                case "Wodka Bull":
                    product.getIngredients().addAll(Arrays.asList(
                            findIngredient.apply("Vodka"),
                            findIngredient.apply("Red Bull")
                    ));
                    break;

                case "Aperol Spritz":
                    product.getIngredients().addAll(Arrays.asList(
                            findIngredient.apply("Aperol"),
                            findIngredient.apply("Prosecco"),
                            findIngredient.apply("Wasser spritzig"),
                            findIngredient.apply("Orange")
                    ));
                    break;

                case "Sarti Spritz":
                    product.getIngredients().addAll(Arrays.asList(
                            findIngredient.apply("Sarti"),
                            findIngredient.apply("Prosecco"),
                            findIngredient.apply("Wasser spritzig"),
                            findIngredient.apply("Zitrone")
                    ));
                    break;

                case "Wildberry Lillet":
                    product.getIngredients().addAll(Arrays.asList(
                            findIngredient.apply("Lillet"),
                            findIngredient.apply("Wildberry"),
                            findIngredient.apply("Beeren-Mix")
                    ));
                    break;

                // Andere Products haben keine Ingredients (Bier, etc.)
                default:
                    // Keine Ingredients
                    break;
            }
        }

        // Alle Products mit neuen Ingredients speichern
        productRepository.saveAll(products);
    }
}
