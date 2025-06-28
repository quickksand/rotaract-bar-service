package com.lichius.rac.ansbach.altstadtfest.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id")
//    private List<OrderedItem> items;
    private LocalDateTime orderedAt;

//    public void setItems(List<OrderedItem> items) {
//        this.items = items;
//    }

    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

//    public List<OrderedItem> getItems() {
//        return items;
//    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

//    public double getTotalPrice() {
//        return items.stream().mapToDouble(OrderedItem::getTotalPrice).sum();
//    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
