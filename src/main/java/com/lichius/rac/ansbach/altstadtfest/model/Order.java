package com.lichius.rac.ansbach.altstadtfest.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private LocalDateTime orderedAt;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "order"
    )
    private List<OrderedItem> items = new ArrayList<>();


    // Konstruktoren
    public Order() {
        this.orderedAt = LocalDateTime.now();
    }

    public Order(List<OrderedItem> items) {
        this();
        this.addOrderedItems(items);
    }


    // HELPER
    public void addItem(OrderedItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderedItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    // For bulk operations
    public void addOrderedItems(Collection<OrderedItem> items) {
        items.forEach(this::addItem);
    }

    public Long getId() {
        return id;
    }

    // GETTER & SETTER
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
