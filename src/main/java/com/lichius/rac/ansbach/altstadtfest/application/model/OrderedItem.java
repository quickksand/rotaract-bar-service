package com.lichius.rac.ansbach.altstadtfest.application.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ordered_items")
public class OrderedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
//    @JsonBackReference
    private PurchaseOrder purchaseOrder;

    @ManyToOne(
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean bottleSale = false;

    @Column
    private Double customPrice;


    // Konstruktoren
    public OrderedItem() {
    }

    // GETTER & SETTER
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public boolean isBottleSale() {
        return bottleSale;
    }

    public void setBottleSale(boolean bottleSale) {
        this.bottleSale = bottleSale;
    }

    public Double getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(Double customPrice) {
        this.customPrice = customPrice;
    }

    //    public double getTotalPrice() {
//        return quantity * drink.getPrice();
//    }
}

