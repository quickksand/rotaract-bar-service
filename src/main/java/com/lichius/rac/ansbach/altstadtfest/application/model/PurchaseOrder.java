package com.lichius.rac.ansbach.altstadtfest.application.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
@Builder
@AllArgsConstructor
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

}
