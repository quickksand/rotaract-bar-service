package com.lichius.rac.ansbach.altstadtfest.application.service;

import com.lichius.rac.ansbach.altstadtfest.application.model.Product;
import com.lichius.rac.ansbach.altstadtfest.application.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import rotaract.bar.infrastructure.api.controller.model.ProductDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
            productRepository.saveAll(getDefaultProducts());
        }
    }
}
