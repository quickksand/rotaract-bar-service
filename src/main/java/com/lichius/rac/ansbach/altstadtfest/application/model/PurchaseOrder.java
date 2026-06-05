package com.lichius.rac.ansbach.altstadtfest.application.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderedAt;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "purchaseOrder",
            orphanRemoval = true
    )
    private List<OrderedItem> items = new ArrayList<>();

    private Integer returnedCupsCount = 0;

    private Double tipAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Integer freeDrinksEarned;

    private Double freeDrinkDiscount;

    // Konstruktoren
    public PurchaseOrder() {
        this.orderedAt = LocalDateTime.now();
    }


    // HELPER

    public void addOrderedItem(OrderedItem item) {
        item.setPurchaseOrder(this);
        this.items.add(item);
    }


    // GETTER & SETTER
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderedItem> getItems() {
        return items;
    }
    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public Integer getReturnedCupsCount() {
        return returnedCupsCount;
    }

    public void setReturnedCupsCount(Integer returnedCupsCount) {
        this.returnedCupsCount = returnedCupsCount;
    }

    public Double getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(Double tipAmount) {
        this.tipAmount = tipAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getFreeDrinksEarned() {
        return freeDrinksEarned;
    }

    public void setFreeDrinksEarned(Integer freeDrinksEarned) {
        this.freeDrinksEarned = freeDrinksEarned;
    }

    public Double getFreeDrinkDiscount() {
        return freeDrinkDiscount;
    }

    public void setFreeDrinkDiscount(Double freeDrinkDiscount) {
        this.freeDrinkDiscount = freeDrinkDiscount;
    }

}
