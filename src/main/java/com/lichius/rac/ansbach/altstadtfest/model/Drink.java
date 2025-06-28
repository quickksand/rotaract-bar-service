package com.lichius.rac.ansbach.altstadtfest.model;

import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;

//@Entity
public class Drink {

//    @jakarta.persistence.Id
//    @Id
    private Long id;
    private String name;
    private double price;


    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
