package com.lichius.rac.ansbach.altstadtfest.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

//@Entity
public class OrderedItem {

//    @jakarta.persistence.Id
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @ManyToOne
//    @JoinColumn(name = "drink_id")
//    private Drink drink;
    private int quantity;

//    public Drink getDrink() {
//        return drink;
//    }

    public int getQuantity() {
        return quantity;
    }

//    public void setDrink(Drink drink) {
//        this.drink = drink;
//    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

//    public double getTotalPrice() {
//        return quantity * drink.getPrice();
//    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

