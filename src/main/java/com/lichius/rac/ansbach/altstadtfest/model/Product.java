package com.lichius.rac.ansbach.altstadtfest.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
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

//    @OneToMany(mappedBy = "drink")
//    private List<OrderedItem> orderedItemList = new ArrayList<>();

    public Product() {
    }

    public Product(String name, BigDecimal price, List<OrderedItem> orderedItemList) {
        this.name = name;
        this.price = price;
//        this.orderedItemList = orderedItemList;
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
}
