package com.lichius.rac.ansbach.altstadtfest.application.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "ordered_items")
public class OrderedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    @JsonBackReference
    private PurchaseOrder purchaseOrder;

    @ManyToOne(
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantity;


    // Konstruktoren
    public OrderedItem() {
    }

    public OrderedItem(PurchaseOrder purchase_order, Product product, Integer quantity) {
        this.purchaseOrder = purchase_order;
        this.product = product;
        this.quantity = quantity;
    }


    // GETTER & SETTER
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder order) {
        this.purchaseOrder = order;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    //    public double getTotalPrice() {
//        return quantity * drink.getPrice();
//    }
}

