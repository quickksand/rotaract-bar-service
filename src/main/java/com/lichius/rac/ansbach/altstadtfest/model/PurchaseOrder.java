package com.lichius.rac.ansbach.altstadtfest.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
//    @NotEmpty(message = "Eine Bestellung muss mindestens ein Item enthalten")
//    @Valid
    private List<OrderedItem> items = new ArrayList<>();


    // Konstruktoren
    public PurchaseOrder() {
        this.orderedAt = LocalDateTime.now();
    }
//    public PurchaseOrder(List<OrderedItem> items) {
//        this();
//        this.addOrderedItems(items);
//    }


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
    public void setItems(List<OrderedItem> items) {
        this.items = items;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

//    public double getTotalPrice() {
//        return items.stream().mapToDouble(OrderedItem::getTotalPrice).sum();
//    }


}
