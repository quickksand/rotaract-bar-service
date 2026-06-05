package com.lichius.rac.ansbach.altstadtfest.application.service;

import com.lichius.rac.ansbach.altstadtfest.application.model.Ingredient;
import com.lichius.rac.ansbach.altstadtfest.application.model.Product;
import com.lichius.rac.ansbach.altstadtfest.application.model.ProductsMeta;
import com.lichius.rac.ansbach.altstadtfest.application.repository.ProductRepository;
import com.lichius.rac.ansbach.altstadtfest.application.repository.ProductsMetaRepository;
import com.lichius.rac.ansbach.altstadtfest.exception.NotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rotaract.bar.infrastructure.api.controller.model.ProductDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@DependsOn("ingredientService")
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductsMetaRepository productsMetaRepository;
    private final IngredientService ingredientService;

    public ProductService(ProductRepository productRepository,
                          ProductsMetaRepository productsMetaRepository,
                          IngredientService ingredientService) {
        this.productRepository = productRepository;
        this.productsMetaRepository = productsMetaRepository;
        this.ingredientService = ingredientService;
    }

    public List<Product> getProducts() {
        return productRepository.findAllByOrderByIdAsc();
    }

    public ProductsMeta getProductsMeta() {
        return productsMetaRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("ProductsMeta nicht initialisiert"));
    }

    @Transactional
    public Product updateProduct(Long id, Double price, Boolean outOfStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product nicht gefunden: id=" + id));

        if (price != null) {
            product.setPrice(BigDecimal.valueOf(price));
        }
        if (outOfStock != null) {
            product.setOutOfStock(outOfStock);
        }

        Product saved = productRepository.save(product);
        touchProductsMeta();
        return saved;
    }

    @Transactional
    public List<Product> resetProducts() {
        List<Product> existing = productRepository.findAllByOrderByIdAsc();
        for (Product product : existing) {
            product.setPrice(product.getOriginalPrice());
            product.setOutOfStock(false);
        }

        List<Product> saved = productRepository.saveAll(existing);
        touchProductsMeta();
        log.info("✅ Alle Produkte auf Standardpreise zurückgesetzt");
        return saved;
    }

    private List<Product> getDefaultProducts() {
        return List.of(
                // DRINKS — alle Longdrinks werden im Becher serviert (requiresDeposit = true)
                new Product("Cuba Libre", new BigDecimal("6.50"), ProductDto.CategoryEnum.DRINKS, true),
                new Product("Gin Tonic", new BigDecimal("7.00"), ProductDto.CategoryEnum.DRINKS, true),
                new Product("Moscow Mule", new BigDecimal("7.00"), ProductDto.CategoryEnum.DRINKS, true),
                new Product("Munich Mule", new BigDecimal("7.00"), ProductDto.CategoryEnum.DRINKS, true),
                new Product("Wodka Bull", new BigDecimal("7.00"), ProductDto.CategoryEnum.DRINKS, true),
                new Product("Aperol Spritz", new BigDecimal("6.00"), ProductDto.CategoryEnum.DRINKS, true),
                new Product("Sarti Spritz", new BigDecimal("6.00"), ProductDto.CategoryEnum.DRINKS, true),
                new Product("Wildberry Lillet", new BigDecimal("6.00"), ProductDto.CategoryEnum.DRINKS, true),
                // Bier, Wein & Nonalkoholisches
                new Product("Bier 0,33", new BigDecimal("3.50"), ProductDto.CategoryEnum.BEER_WINE_NONALC),
                new Product("Bier Alkfrei", new BigDecimal("3.50"), ProductDto.CategoryEnum.BEER_WINE_NONALC),
                new Product("Radler 0,5", new BigDecimal("4.00"), ProductDto.CategoryEnum.BEER_WINE_NONALC),
                new Product("Weinschorle", new BigDecimal("4.00"), ProductDto.CategoryEnum.BEER_WINE_NONALC, true),
                new Product("Cola", new BigDecimal("3.00"), ProductDto.CategoryEnum.BEER_WINE_NONALC, true),
                new Product("Sprite", new BigDecimal("3.00"), ProductDto.CategoryEnum.BEER_WINE_NONALC, true),
                new Product("Wasser", new BigDecimal("2.50"), ProductDto.CategoryEnum.BEER_WINE_NONALC, true),
                // SHOTS — kein Becher
                new Product("Brausegeist / Wodka Ahoi!", new BigDecimal("2.00"), ProductDto.CategoryEnum.SHOTS),
                new Product("Mexikaner", new BigDecimal("2.00"), ProductDto.CategoryEnum.SHOTS)
        );
    }

    @PostConstruct
    @Transactional
    public void initializeProducts() {
        // ProductsMeta initialisieren (Singleton-Row mit id=1)
        if (productsMetaRepository.count() == 0) {
            productsMetaRepository.save(new ProductsMeta(LocalDateTime.now()));
            log.info("✅ ProductsMeta initialized");
        }

        if (productRepository.count() == 0) {
            // 1. Products speichern
            List<Product> products = productRepository.saveAll(getDefaultProducts());
            log.info("✅ Standard Products initialized");

            // 2. Ingredients zuweisen
            assignIngredientsToProducts(products);
            log.info("✅ Ingredients assigned to Products");
        } else {
            // Migration: originalPrice für bestehende Produkte befüllen
            List<Product> products = productRepository.findAllByOrderByIdAsc();
            boolean migrated = false;
            Map<String, BigDecimal> defaultPrices = new HashMap<>();
            Map<String, Boolean> defaultDeposit = new HashMap<>();
            for (Product p : getDefaultProducts()) {
                defaultPrices.put(p.getName(), p.getOriginalPrice());
                defaultDeposit.put(p.getName(), p.isRequiresDeposit());
            }
            for (Product product : products) {
                if (product.getOriginalPrice() == null) {
                    BigDecimal defPrice = defaultPrices.get(product.getName());
                    product.setOriginalPrice(defPrice != null ? defPrice : product.getPrice());
                    migrated = true;
                }
                // Migration: requiresDeposit aus Default-Produkten übernehmen
                Boolean shouldRequireDeposit = defaultDeposit.get(product.getName());
                if (shouldRequireDeposit != null && shouldRequireDeposit != product.isRequiresDeposit()) {
                    product.setRequiresDeposit(shouldRequireDeposit);
                    migrated = true;
                }
            }
            if (migrated) {
                productRepository.saveAll(products);
                log.info("✅ Bestehende Produkte migriert (originalPrice / requiresDeposit)");
            }
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
                            getIngredientOrThrow(ingredientMap, "Limettensaft"),
                            getIngredientOrThrow(ingredientMap, "Limette")
                    ));
                    break;

                case "Gin Tonic":
                    product.getIngredients().addAll(Arrays.asList(
                            getIngredientOrThrow(ingredientMap, "Gin"),
                            getIngredientOrThrow(ingredientMap, "Tonic Water"),
                            getIngredientOrThrow(ingredientMap, "Gurke/Zitrone")
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

    private void touchProductsMeta() {
        ProductsMeta meta = productsMetaRepository.findById(1L)
                .orElse(new ProductsMeta(LocalDateTime.now()));
        meta.setProductsUpdatedAt(LocalDateTime.now());
        productsMetaRepository.save(meta);
    }
}