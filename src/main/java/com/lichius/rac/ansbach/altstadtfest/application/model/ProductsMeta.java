package com.lichius.rac.ansbach.altstadtfest.application.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products_meta")
public class ProductsMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime productsUpdatedAt;

    public ProductsMeta() {
    }

    public ProductsMeta(LocalDateTime productsUpdatedAt) {
        this.productsUpdatedAt = productsUpdatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getProductsUpdatedAt() {
        return productsUpdatedAt;
    }

    public void setProductsUpdatedAt(LocalDateTime productsUpdatedAt) {
        this.productsUpdatedAt = productsUpdatedAt;
    }
}

