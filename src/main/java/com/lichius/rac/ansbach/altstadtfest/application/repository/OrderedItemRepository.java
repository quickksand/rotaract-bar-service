package com.lichius.rac.ansbach.altstadtfest.application.repository;

import com.lichius.rac.ansbach.altstadtfest.application.model.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long> {

    List<OrderedItem> findByProduct_Name(String productName);

    @Query("SELECT oi.product.id, oi.product.name, oi.product.category, " +
            "SUM(oi.quantity), " +
            "SUM(CASE WHEN oi.bottleSale = true AND oi.customPrice IS NOT NULL " +
            "         THEN oi.customPrice " +
            "         ELSE oi.quantity * oi.product.price END) " +
            "FROM OrderedItem oi " +
            "GROUP BY oi.product.id, oi.product.name, oi.product.category " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> aggregateProductSalesAll();

    @Query("SELECT oi.product.id, oi.product.name, oi.product.category, " +
            "SUM(oi.quantity), " +
            "SUM(CASE WHEN oi.bottleSale = true AND oi.customPrice IS NOT NULL " +
            "         THEN oi.customPrice " +
            "         ELSE oi.quantity * oi.product.price END) " +
            "FROM OrderedItem oi JOIN oi.purchaseOrder po " +
            "WHERE po.orderedAt >= :from AND po.orderedAt <= :to " +
            "GROUP BY oi.product.id, oi.product.name, oi.product.category " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> aggregateProductSalesInRange(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
