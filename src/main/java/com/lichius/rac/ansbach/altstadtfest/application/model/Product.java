package com.lichius.rac.ansbach.altstadtfest.application.model;

import jakarta.persistence.*;
import rotaract.bar.infrastructure.api.controller.model.ProductDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column
    private BigDecimal originalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductDto.CategoryEnum category;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "product_ingredients",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> ingredients = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean outOfStock = false;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean requiresDeposit = false;

    public Product() {
    }

    public Product(String name, BigDecimal price, ProductDto.CategoryEnum category) {
        this.name = name;
        this.price = price;
        this.originalPrice = price;
        this.category = category;
    }

    public Product(String name, BigDecimal price, ProductDto.CategoryEnum category, boolean requiresDeposit) {
        this.name = name;
        this.price = price;
        this.originalPrice = price;
        this.category = category;
        this.requiresDeposit = requiresDeposit;
    }


    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public ProductDto.CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(ProductDto.CategoryEnum category) {
        this.category = category;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public boolean isRequiresDeposit() {
        return requiresDeposit;
    }

    public void setRequiresDeposit(boolean requiresDeposit) {
        this.requiresDeposit = requiresDeposit;
    }

}
