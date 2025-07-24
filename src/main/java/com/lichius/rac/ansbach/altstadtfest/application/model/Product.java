package com.lichius.rac.ansbach.altstadtfest.application.model;

import jakarta.persistence.*;
import rotaract.bar.infrastructure.api.controller.model.ProductDto;

import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductDto.CategoryEnum category;

    public Product() {
    }

    public Product(String name, BigDecimal price, ProductDto.CategoryEnum category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }


    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
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
}
